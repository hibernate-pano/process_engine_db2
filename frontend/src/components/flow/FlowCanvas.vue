<template>
  <div class="flow-canvas h-full w-full">
    <VueFlow
      v-model="elements"
      :default-zoom="1"
      :min-zoom="0.2"
      :max-zoom="4"
      :node-draggable="true"
      :elementsSelectable="true"
      class="w-full h-full bg-gray-50"
      @connect="onConnect"
      @connection-start="onConnectionStart"
      @connection-end="onConnectionEnd"
      @node-drag-stop="onNodeDragStop"
      @pane-click="onPaneClick"
      @pane-ready="onPaneReady"
      @edge-update-start="onEdgeUpdateStart"
      @edge-update-end="onEdgeUpdateEnd"
      @edge-update="onEdgeUpdate"
      :node-types="nodeTypes"
      :connection-mode="ConnectionMode.Loose"
    >
      <template #node-default="nodeProps">
        <FlowNode
          :data="nodeProps.data"
          :selected="nodeProps.selected"
          :type="nodeProps.type"
          :id="nodeProps.id"
          @click="onNodeClick(nodeProps)"
          @dblclick="onNodeDbClick(nodeProps)"
          @updateNodeInternals="updateNodeInternals([nodeProps.id])"
        />
      </template>

      <template #edge-default="edgeProps">
        <FlowEdge
          :id="edgeProps.id"
          :data="edgeProps.data"
          :selected="edgeProps.selected"
          @click="onEdgeClick(edgeProps)"
        />
      </template>

      <Background :pattern-color="'#aaa'" :gap="8" />
      <Controls />
      <MiniMap class="bg-white rounded shadow-md" />
    </VueFlow>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch, shallowRef, markRaw } from 'vue'
import { VueFlow, useVueFlow, ConnectionMode } from '@vue-flow/core'
import type { NodeDragEvent, Connection, Edge } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { MiniMap } from '@vue-flow/minimap'
import '@vue-flow/core/dist/style.css'
import '@vue-flow/controls/dist/style.css'
import '@vue-flow/minimap/dist/style.css'
import FlowNode from './FlowNode.vue'
import FlowEdge from './FlowEdge.vue'
import { useFlowStore } from '../../stores/flowStore'

// 连线模式 - Loose 允许任意类型的连接，Strict 只允许 source->target 的连接
const connectionMode = ConnectionMode.Strict

// 定义节点类型映射
const nodeTypes = markRaw({
  default: FlowNode, // 默认节点类型
  start: FlowNode,   // 开始节点类型
  end: FlowNode,     // 结束节点类型
  action: FlowNode,  // 动作节点类型
  condition: FlowNode, // 条件节点类型
  delay: FlowNode,    // 延迟节点类型
  timer: FlowNode,    // 定时器节点类型
})

const flowStore = useFlowStore()
const {
  findNode,
  getNodes,
  getEdges,
  addNodes,
  addEdges,
  onConnect: vfOnConnect,
  getIntersectingNodes,
  project,
  setTransform,
  updateNodeInternals,
} = useVueFlow()

// 流程元素（节点和边）
const elements = ref<any[]>([])

// 选中的节点和边ID
const selectedNodeId = ref('')
const selectedEdgeId = ref('')

// 是否正在创建连接
const isCreatingConnection = ref(false)

// 流程图数据
const flowData = ref({
  id: '',
  name: '',
  nodes: [],
  edges: [],
})

// 定义 emit 以向父组件通知选中元素的变化
const emit = defineEmits(['update:selectedElementId', 'flow-node-selected', 'flow-node-dbclick'])

// 监听选中节点和边的变化
watch([selectedNodeId, selectedEdgeId], ([nodeId, edgeId]) => {
  // 当有节点被选中时，通知父组件
  if (nodeId) {
    emit('update:selectedElementId', nodeId)
  }
  // 当有边被选中时，通知父组件
  else if (edgeId) {
    emit('update:selectedElementId', edgeId)
  }
  // 当都没有选中时，清除选中状态
  else {
    emit('update:selectedElementId', '')
  }
})

// 生成唯一ID
const generateId = (prefix = 'node') => {
  return `${prefix}_${Date.now()}_${Math.floor(Math.random() * 1000)}`
}

// 连接开始时触发
const onConnectionStart = (event: any) => {
  console.log('开始连接事件:', event)
  isCreatingConnection.value = true
  
  // 记录到控制台以便调试
  const { source, sourceHandle } = event
  console.log(`连接开始: 源节点 ${source} (handle: ${sourceHandle})`)
}

// 连接结束时触发
const onConnectionEnd = (event: any) => {
  console.log('结束连接事件:', event)
  isCreatingConnection.value = false
  
  // 记录到控制台以便调试
  if (event.target) {
    const { target, targetHandle } = event
    console.log(`连接结束: 目标节点 ${target} (handle: ${targetHandle})`)
  } else {
    console.log('连接结束: 未连接到目标')
  }
}

// 验证连接是否有效
const isValidConnection = (connection: Connection): boolean => {
  console.log('验证连接:', connection)
  
  // 规则1: 源节点和目标节点必须存在
  if (!connection.source || !connection.target) {
    console.log('连接无效: 源节点或目标节点不存在')
    return false
  }
  
  // 规则2: 源节点和目标节点不能是同一个节点
  if (connection.source === connection.target) {
    console.log('连接无效: 不能连接到自己')
    return false
  }

  // 规则3: 检查是否已存在相同的连接
  const existingEdges = getEdges.value
  const duplicateConnection = existingEdges.some((edge: Edge) => 
    edge.source === connection.source && 
    edge.target === connection.target &&
    edge.sourceHandle === connection.sourceHandle &&
    edge.targetHandle === connection.targetHandle
  )

  if (duplicateConnection) {
    console.log('连接无效: 已存在相同的连接')
    return false
  }

  // 规则4: 根据节点类型限制连接
  const sourceNode = findNode(connection.source)
  const targetNode = findNode(connection.target)

  if (!sourceNode || !targetNode) {
    console.log('连接无效: 找不到源节点或目标节点')
    return false
  }
  
  console.log('源节点类型:', sourceNode.data?.type)
  console.log('目标节点类型:', targetNode.data?.type)

  // 规则5: 起始节点只能作为源，不能作为目标
  if (targetNode.data?.type === 'start') {
    console.log('连接无效: 开始节点不能作为连接目标')
    return false
  }

  // 规则6: 结束节点只能作为目标，不能作为源
  if (sourceNode.data?.type === 'end') {
    console.log('连接无效: 结束节点不能作为连接源')
    return false
  }

  // 如果检查通过，允许连接
  console.log('连接有效!')
  return true
}

// 连接节点时触发
const onConnect = (params: Connection) => {
  console.log('尝试连接:', params)
  
  // 验证连接有效性
  if (!isValidConnection(params)) {
    return
  }

  const id = generateId('edge')
  const newEdge: Edge = {
    id,
    source: params.source!,
    target: params.target!,
    sourceHandle: params.sourceHandle,
    targetHandle: params.targetHandle,
    type: 'default',
    data: {
      label: '连线',
      isConditional: false,
      conditionExpression: ''
    },
    zIndex: 0, // 确保边在节点下方
  }
  
  // 添加新边
  console.log('添加新边:', newEdge)
  addEdges([newEdge])
  
  // 更新节点内部结构，确保连接点位置正确
  setTimeout(() => {
    updateAllNodeInternals()
  }, 50)
  
  // 这里可以发送API请求保存新连线
  console.log('创建连线成功:', newEdge)
}

// 帮助方法 - 更新所有节点内部结构
const updateAllNodeInternals = () => {
  const nodeIds = getNodes.value.map((node: any) => node.id)
  console.log('更新所有节点内部结构:', nodeIds)
  if (nodeIds.length > 0) {
    updateNodeInternals(nodeIds)
  }
}

// 节点拖拽停止时触发
const onNodeDragStop = (dragEvent: NodeDragEvent) => {
  const { node } = dragEvent
  console.log('节点位置更新:', node.id, node.position)
  // 这里可以发送API请求保存节点位置
  
  // 更新节点内部结构，确保连接点位置正确
  setTimeout(() => {
    updateNodeInternals([node.id])
  }, 0)
}

// 点击画布空白处
const onPaneClick = () => {
  selectedNodeId.value = ''
  selectedEdgeId.value = ''
  // 可以触发取消选中事件
}

// 画布就绪
const onPaneReady = (instance: any) => {
  console.log('画布就绪')
  // 可以进行初始化操作，例如居中显示
  instance.fitView()
  
  // 延迟更新节点内部结构，确保连接点正确渲染
  setTimeout(() => {
    updateAllNodeInternals()
  }, 200)
}

// 点击节点
const onNodeClick = (nodeProps: any) => {
  selectedNodeId.value = nodeProps.id
  selectedEdgeId.value = ''
  console.log('选中节点:', nodeProps)
  
  // 更新v-model绑定
  emit('update:selectedElementId', nodeProps.id)
  
  // 触发属性面板更新 - 发送 flow-node-selected 事件
  emit('flow-node-selected', {
    id: nodeProps.id,
    type: 'node',
    data: nodeProps.data
  })
}

// 双击节点
const onNodeDbClick = (nodeProps: any) => {
  console.log('双击节点:', nodeProps)
  
  // 触发节点双击事件 - 用于配置节点属性
  emit('flow-node-dbclick', {
    id: nodeProps.id,
    type: 'node',
    data: nodeProps.data
  })
}

// 点击边
const onEdgeClick = (edgeProps: any) => {
  selectedEdgeId.value = edgeProps.id
  selectedNodeId.value = ''
  console.log('选中连线:', edgeProps)
}

// 开始更新边
const onEdgeUpdateStart = (event: any) => {
  console.log('开始更新边:', event)
}

// 更新边
const onEdgeUpdate = (event: any) => {
  console.log('更新边:', event)
}

// 结束更新边
const onEdgeUpdateEnd = (event: any) => {
  console.log('结束更新边:', event)
}

// 添加新节点
const addNode = (type: string, position: { x: number; y: number }) => {
  const id = generateId('node')
  const newNode = {
    id,
    type,
    position,
    data: {
      label: `${type}节点`,
      type,
      properties: {}
    },
    zIndex: 1, // 确保节点在边的上方
  }
  
  addNodes([newNode])
  return newNode
}

// 从外部导入流程图数据
const importFlow = (data: any) => {
  if (!data) return
  
  flowData.value = { ...data }
  
  // 清空现有元素
  elements.value = []
  
  // 添加节点
  if (data.nodes && data.nodes.length > 0) {
    elements.value.push(...data.nodes)
  }
  
  // 添加边
  if (data.edges && data.edges.length > 0) {
    elements.value.push(...data.edges)
  }
  
  // 确保更新节点内部结构
  setTimeout(() => {
    const nodeIds = getNodes.value.map((node: any) => node.id)
    updateNodeInternals(nodeIds)
  }, 100)
}

// 导出流程图数据
const exportFlow = () => {
  const nodes = getNodes.value
  const edges = getEdges.value
  
  return {
    id: flowData.value.id,
    name: flowData.value.name,
    nodes,
    edges
  }
}

// 提供给父组件的方法
defineExpose({
  addNode,
  importFlow,
  exportFlow
})
</script>

<style scoped>
.flow-canvas :deep(.vue-flow__node) {
  border-radius: 6px;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 500;
  border-width: 2px;
  z-index: 1;
}

.flow-canvas :deep(.vue-flow__node.selected) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
  z-index: 2;
}

.flow-canvas :deep(.vue-flow__edge.selected) .vue-flow__edge-path {
  stroke: #3b82f6;
  stroke-width: 3;
}

.flow-canvas :deep(.vue-flow__edge-path) {
  stroke: #555;
  stroke-width: 2;
}

.flow-canvas :deep(.vue-flow__edge-text) {
  font-size: 12px;
}

.flow-canvas :deep(.vue-flow__handle) {
  width: 12px;
  height: 12px;
  background-color: #3b82f6;
  border: 2px solid white;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.flow-canvas :deep(.vue-flow__handle:hover) {
  width: 14px;
  height: 14px;
  transform: translate(-50%, -50%) scale(1.2);
  cursor: crosshair;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
}

.flow-canvas :deep(.vue-flow__connection-path) {
  stroke: #3b82f6;
  stroke-width: 3;
}

.flow-canvas :deep(.vue-flow__connection) {
  z-index: 1000;
}

/* 设置连接线样式 */
.flow-canvas :deep(.vue-flow__connectionline) {
  z-index: 1001;
}

.flow-canvas :deep(.vue-flow__connectionline-path) {
  stroke: #3b82f6;
  stroke-width: 3;
  stroke-dasharray: 5;
  animation: dashdraw 0.5s linear infinite;
}

@keyframes dashdraw {
  from {
    stroke-dashoffset: 10;
  }
}
</style>
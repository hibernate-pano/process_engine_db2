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
      @node-drag-stop="onNodeDragStop"
      @pane-click="onPaneClick"
      @pane-ready="onPaneReady"
      :node-types="nodeTypes"
    >
      <template #node-default="nodeProps">
        <FlowNode
          :data="nodeProps.data"
          :selected="nodeProps.selected"
          :type="nodeProps.type"
          :id="nodeProps.id"
          @click="onNodeClick(nodeProps)"
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
import { ref, onMounted, computed, watch, shallowRef } from 'vue'
import { VueFlow, useVueFlow } from '@vue-flow/core'
import type { NodeDragEvent } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { MiniMap } from '@vue-flow/minimap'
import '@vue-flow/core/dist/style.css'
import '@vue-flow/controls/dist/style.css'
import '@vue-flow/minimap/dist/style.css'
import FlowNode from './FlowNode.vue'
import FlowEdge from './FlowEdge.vue'
import { useFlowStore } from '../../stores/flowStore'

// 定义节点类型映射
const nodeTypes = {
  default: FlowNode, // 默认节点类型
  start: FlowNode,   // 开始节点类型
  end: FlowNode,     // 结束节点类型
  action: FlowNode,  // 动作节点类型
  condition: FlowNode, // 条件节点类型
  delay: FlowNode,    // 延迟节点类型
  timer: FlowNode,    // 定时器节点类型
}

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
} = useVueFlow()

// 流程元素（节点和边）
const elements = ref<any[]>([])

// 当前选中的节点ID
const selectedNodeId = ref('')

// 当前选中的边ID
const selectedEdgeId = ref('')

// 流程图数据
const flowData = ref({
  id: '',
  name: '',
  nodes: [],
  edges: [],
})

// 生成唯一ID
const generateId = (prefix = 'node') => {
  return `${prefix}_${Date.now()}_${Math.floor(Math.random() * 1000)}`
}

// 连接节点时触发
const onConnect = (params: any) => {
  const id = generateId('edge')
  const newEdge = {
    id,
    source: params.source,
    target: params.target,
    sourceHandle: params.sourceHandle,
    targetHandle: params.targetHandle,
    type: 'default',
    data: {
      label: '连线',
      isConditional: false,
      conditionExpression: ''
    }
  }
  
  // 直接使用addEdges添加边，而不是通过vfOnConnect
  addEdges([newEdge])
  
  // 这里可以发送API请求保存新连线
  console.log('创建连线:', newEdge)
}

// 节点拖拽停止时触发
const onNodeDragStop = (dragEvent: NodeDragEvent) => {
  const { node } = dragEvent
  console.log('节点位置更新:', node.id, node.position)
  // 这里可以发送API请求保存节点位置
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
}

// 点击节点
const onNodeClick = (nodeProps: any) => {
  selectedNodeId.value = nodeProps.id
  selectedEdgeId.value = ''
  console.log('选中节点:', nodeProps)
  // 可以触发选中节点事件，显示节点属性面板
}

// 点击边
const onEdgeClick = (edgeProps: any) => {
  selectedEdgeId.value = edgeProps.id
  selectedNodeId.value = ''
  console.log('选中连线:', edgeProps)
  // 可以触发选中边事件，显示边属性面板
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
    }
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
}

.flow-canvas :deep(.vue-flow__node.selected) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
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
  fill: #333;
  background: white;
  padding: 2px 4px;
  border-radius: 4px;
}

.flow-canvas :deep(.vue-flow__minimap) {
  opacity: 0.9;
}
</style>
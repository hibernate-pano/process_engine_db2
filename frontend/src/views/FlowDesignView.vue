<template>
  <div class="flow-design h-screen w-full flex flex-col">
    <div class="h-12 px-4 flex items-center justify-between border-b">
      <h1 class="text-lg font-bold">预案设计器</h1>
      <div class="flex gap-2">
        <button class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600" @click="saveFlow">保存</button>
        <button class="px-3 py-1 bg-green-500 text-white rounded hover:bg-green-600" @click="publishFlow">发布</button>
        <button 
          class="px-3 py-1 border border-gray-300 rounded hover:bg-gray-100 flex items-center" 
          @click="toggleHistoryPanel"
        >
          <span class="mr-1">历史版本</span>
          <svg v-if="showHistoryPanel" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
          <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </div>
    <div class="flex flex-1 w-full overflow-hidden">
      <!-- 节点面板 -->
      <div class="w-64 border-r overflow-y-auto">
        <NodePanel />
      </div>
      
      <!-- 流程画布 -->
      <div class="flex-1 relative" ref="flowWrapper" @drop="onDrop" @dragover="onDragOver">
        <FlowCanvas ref="flowCanvas" />
      </div>
      
      <!-- 侧边栏区域 -->
      <div class="w-80 border-l overflow-y-auto">
        <!-- 属性面板/历史面板切换 -->
        <div v-if="showHistoryPanel">
          <HistoryPanel 
            :flowDefinitionId="currentFlowDefinitionId" 
            @view="handleViewVersion"
            @setAsCurrent="handleSetAsCurrent"
            @rollback="handleRollbackVersion"
          />
        </div>
        <div v-else>
          <PropertyPanel 
            v-model:selectedElementId="selectedElementId"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { VueFlow, useVueFlow } from '@vue-flow/core'
import FlowCanvas from '../components/flow/FlowCanvas.vue'
import NodePanel from '../components/flow/NodePanel.vue'
import PropertyPanel from '../components/flow/PropertyPanel.vue'
import HistoryPanel from '../components/flow/HistoryPanel.vue'
import { useFlowStore } from '../stores/flowStore'
import type { FlowVersion } from '../api'

const flowStore = useFlowStore()
const flowCanvas = ref<InstanceType<typeof FlowCanvas> | null>(null)
const flowWrapper = ref<HTMLElement | null>(null)

// 选中的元素ID（节点或边）
const selectedElementId = ref<string>('')

// 显示历史面板
const showHistoryPanel = ref(false)

// 当前流程定义ID (实际项目中应从路由参数或状态管理中获取)
const currentFlowDefinitionId = ref<number | undefined>(1)

// Vue Flow相关
const { onConnect, addEdges, project, viewport } = useVueFlow()

// 切换历史面板显示
const toggleHistoryPanel = () => {
  showHistoryPanel.value = !showHistoryPanel.value
}

// 处理节点拖拽放置
const onDrop = (event: DragEvent) => {
  event.preventDefault()
  
  if (!event.dataTransfer) return
  
  const data = event.dataTransfer.getData('application/vueflow')
  if (!data) return
  
  try {
    const nodeType = JSON.parse(data)
    
    // 获取鼠标在画布中的位置
    const position = project({
      x: event.clientX - (flowWrapper.value?.getBoundingClientRect().left || 0),
      y: event.clientY - (flowWrapper.value?.getBoundingClientRect().top || 0)
    })
    
    // 添加新节点
    if (flowCanvas.value) {
      const newNode = flowCanvas.value.addNode(nodeType.type, position)
      selectedElementId.value = newNode.id
    }
  } catch (error) {
    console.error('无法解析拖拽节点数据:', error)
  }
}

// 处理拖拽悬停
const onDragOver = (event: DragEvent) => {
  event.preventDefault()
  if (event.dataTransfer) {
    event.dataTransfer.dropEffect = 'move'
  }
}

// 保存流程
const saveFlow = async () => {
  if (!flowCanvas.value) return
  
  try {
    const flowData = flowCanvas.value.exportFlow()
    console.log('保存流程数据:', flowData)
    
    // 调用API保存流程数据
    // 根据是新建还是编辑决定调用哪个API
    // const result = await flowStore.createFlowVersion({
    //   flowDefinitionId: flowData.id || 0,
    //   flowGraph: JSON.stringify(flowData),
    //   description: '流程设计更新'
    // })
    
    // 保存成功提示
    alert('流程保存成功!')
  } catch (error) {
    console.error('保存流程失败:', error)
    alert('保存失败: ' + (error instanceof Error ? error.message : '未知错误'))
  }
}

// 发布流程
const publishFlow = async () => {
  if (!flowCanvas.value) return
  
  try {
    const flowData = flowCanvas.value.exportFlow()
    console.log('发布流程数据:', flowData)
    
    // 调用API发布流程数据
    // 可能需要先保存，再发布
    // const result = await flowStore.publishFlowVersion(...)
    
    // 发布成功提示
    alert('流程发布成功!')
  } catch (error) {
    console.error('发布流程失败:', error)
    alert('发布失败: ' + (error instanceof Error ? error.message : '未知错误'))
  }
}

// 处理查看历史版本
const handleViewVersion = (version: FlowVersion) => {
  if (!flowCanvas.value) return
  
  try {
    // 假设我们需要从flowGraph字段加载流程图数据
    const flowData = JSON.parse(version.flowGraph)
    flowCanvas.value.importFlow(flowData)
    
    // 切换回属性面板以便查看和编辑
    showHistoryPanel.value = false
  } catch (error) {
    console.error('加载历史版本失败:', error)
    alert('加载历史版本失败: ' + (error instanceof Error ? error.message : '未知错误'))
  }
}

// 处理设置当前版本
const handleSetAsCurrent = async (version: FlowVersion) => {
  try {
    // 调用store中的设置当前版本方法
    const result = await flowStore.setFlowVersionAsCurrent(version.id)
    
    if (result) {
      alert(`已成功将版本 v${version.version} 设置为当前版本`)
      
      // 重新加载流程定义
      if (currentFlowDefinitionId.value) {
        await flowStore.fetchFlowDefinition(currentFlowDefinitionId.value)
      }
      
      // 加载新版本数据到画布
      try {
        const flowData = JSON.parse(version.flowGraph)
        if (flowCanvas.value) {
          flowCanvas.value.importFlow(flowData)
        }
      } catch (error) {
        console.error('解析流程图数据失败:', error)
      }
      
      // 切换回属性面板
      showHistoryPanel.value = false
    }
  } catch (error) {
    console.error('设置当前版本失败:', error)
    alert('设置失败: ' + (error instanceof Error ? error.message : '未知错误'))
  }
}

// 处理回滚版本
const handleRollbackVersion = async (version: FlowVersion) => {
  try {
    // 调用store中的回滚方法
    const result = await flowStore.rollbackToVersion(version.id, `回滚至版本v${version.version}`)
    
    if (result) {
      alert(`已成功回滚到版本 v${version.version}`)
      
      // 重新加载流程定义和版本列表
      if (currentFlowDefinitionId.value) {
        await flowStore.fetchFlowDefinition(currentFlowDefinitionId.value)
        
        // 如果回滚成功，加载新的版本数据
        const currentVersion = flowStore.currentFlowDefinition?.currentVersion
        if (currentVersion) {
          await flowStore.fetchFlowVersion(currentVersion)
          
          // 更新画布显示
          if (flowStore.currentFlowVersion && flowCanvas.value) {
            try {
              const flowData = JSON.parse(flowStore.currentFlowVersion.flowGraph)
              flowCanvas.value.importFlow(flowData)
            } catch (error) {
              console.error('解析流程图数据失败:', error)
            }
          }
        }
      }
      
      // 切换回属性面板
      showHistoryPanel.value = false
    }
  } catch (error) {
    console.error('回滚版本失败:', error)
    alert('回滚失败: ' + (error instanceof Error ? error.message : '未知错误'))
  }
}

// 加载流程数据
const loadFlowData = async (flowId?: number, versionId?: number) => {
  if (!flowCanvas.value) return
  
  try {
    // 从API加载流程数据
    // 这里模拟一个简单的流程图数据用于测试
    const demoFlowData = {
      id: flowId || 1,
      name: '测试流程',
      nodes: [
        {
          id: 'node_1',
          type: 'start',
          position: { x: 100, y: 100 },
          data: { label: '开始', type: 'start', properties: {} }
        },
        {
          id: 'node_2',
          type: 'action',
          position: { x: 100, y: 200 },
          data: { label: '执行动作', type: 'action', properties: { actionType: 'httpRequest' } }
        },
        {
          id: 'node_3',
          type: 'end',
          position: { x: 100, y: 300 },
          data: { label: '结束', type: 'end', properties: {} }
        }
      ],
      edges: [
        {
          id: 'edge_1_2',
          source: 'node_1',
          target: 'node_2',
          data: { label: '连线1', isConditional: false }
        },
        {
          id: 'edge_2_3',
          source: 'node_2',
          target: 'node_3',
          data: { label: '连线2', isConditional: false }
        }
      ]
    }
    
    // 导入流程数据到画布
    flowCanvas.value.importFlow(demoFlowData)
    
  } catch (error) {
    console.error('加载流程数据失败:', error)
  }
}

onMounted(() => {
  // 初始化时加载流程数据
  setTimeout(() => {
    loadFlowData()
  }, 500)
})
</script>

<style scoped>
.flow-design {
  background-color: #f9fafb;
  width: 100%;
}
</style> 
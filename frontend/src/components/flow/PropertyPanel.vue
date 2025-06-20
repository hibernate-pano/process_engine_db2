<template>
  <div class="property-panel bg-white border rounded shadow-sm p-4 h-full overflow-y-auto">
    <div v-if="!selectedElementId" class="text-gray-500 text-center py-4">
      <p>请选择一个节点或连线以编辑其属性</p>
    </div>
    
    <!-- 节点属性编辑 -->
    <div v-else-if="selectedNode" class="node-properties">
      <h3 class="text-lg font-bold mb-4">节点属性</h3>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">节点ID</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm disabled:bg-gray-100" 
          v-model="selectedNode.id" 
          disabled
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">节点名称</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedNode.data.label" 
          @blur="updateNodeData('label')"
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">节点类型</label>
        <select 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedNode.data.type"
          @change="updateNodeData('type')"
        >
          <option value="start">开始</option>
          <option value="end">结束</option>
          <option value="action">动作</option>
          <option value="condition">条件</option>
          <option value="delay">延时</option>
          <option value="timer">定时器</option>
        </select>
      </div>
      
      <!-- 节点属性 - 根据不同类型显示不同的属性编辑器 -->
      <div v-if="selectedNode.data.type === 'action'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">动作类型</label>
        <select 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedNode.data.properties.actionType"
          @change="updateNodeData('properties.actionType')"
        >
          <option value="httpRequest">HTTP请求</option>
          <option value="executeScript">执行脚本</option>
          <option value="sendNotification">发送通知</option>
          <option value="deviceControl">设备控制</option>
        </select>
      </div>
      
      <div v-if="selectedNode.data.type === 'condition'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">条件表达式</label>
        <textarea 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedNode.data.properties.condition"
          @blur="updateNodeData('properties.condition')"
          rows="3"
        ></textarea>
      </div>
      
      <div v-if="selectedNode.data.type === 'delay'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">延迟时间（秒）</label>
        <input 
          type="number" 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedNode.data.properties.delaySeconds"
          @blur="updateNodeData('properties.delaySeconds')"
        />
      </div>
      
      <div v-if="selectedNode.data.type === 'timer'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">CRON表达式</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedNode.data.properties.cronExpression"
          @blur="updateNodeData('properties.cronExpression')"
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
        <textarea 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedNode.data.description"
          @blur="updateNodeData('description')"
          rows="3"
        ></textarea>
      </div>
      
      <div class="flex justify-end">
        <button 
          class="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
          @click="removeNode"
        >
          删除节点
        </button>
      </div>
    </div>
    
    <!-- 边属性编辑 -->
    <div v-else-if="selectedEdge" class="edge-properties">
      <h3 class="text-lg font-bold mb-4">连线属性</h3>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">连线ID</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm disabled:bg-gray-100" 
          v-model="selectedEdge.id" 
          disabled
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">标签</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedEdge.data.label"
          @blur="updateEdgeData('label')"
        />
      </div>
      
      <div class="mb-4">
        <label class="flex items-center">
          <input 
            type="checkbox" 
            class="mr-2" 
            v-model="selectedEdge.data.isConditional"
            @change="updateEdgeData('isConditional')"
          />
          <span class="text-sm font-medium text-gray-700">是否条件分支</span>
        </label>
      </div>
      
      <div v-if="selectedEdge.data.isConditional" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">条件表达式</label>
        <textarea 
          class="w-full px-3 py-2 border rounded text-sm" 
          v-model="selectedEdge.data.conditionExpression"
          @blur="updateEdgeData('conditionExpression')"
          rows="3"
        ></textarea>
      </div>
      
      <div class="flex justify-end">
        <button 
          class="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
          @click="removeEdge"
        >
          删除连线
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useVueFlow } from '@vue-flow/core'

const props = defineProps<{
  selectedElementId?: string
}>()

const emit = defineEmits(['update:selectedElementId'])

const { findNode, getNodes, getEdges, removeNodes, removeEdges, updateNodeData: updateNode, updateEdgeData: updateEdge } = useVueFlow()

// 选中的节点
const selectedNode = computed(() => {
  if (!props.selectedElementId) return null
  return findNode(props.selectedElementId)
})

// 选中的边
const selectedEdge = computed(() => {
  if (!props.selectedElementId) return null
  return getEdges.value.find((edge: any) => edge.id === props.selectedElementId)
})

// 更新节点数据
const updateNodeData = (key: string) => {
  if (!selectedNode.value) return
  
  const node = selectedNode.value
  const path = key.split('.')
  
  // 确保node.data.properties存在
  if (key.startsWith('properties.') && !node.data.properties) {
    node.data.properties = {}
  }
  
  // 更新节点数据
  if (path.length > 1) {
    const mainProp = path[0]
    const subProp = path[1]
    
    // 确保主属性对象存在
    if (!node.data[mainProp]) {
      node.data[mainProp] = {}
    }
    
    // 创建数据的深拷贝以确保更新被检测到
    const updatedData = { ...node.data }
    updatedData[mainProp] = { ...updatedData[mainProp] }
    updatedData[mainProp][subProp] = node.data[mainProp][subProp]
    
    updateNode(node.id, updatedData)
  } else {
    updateNode(node.id, {
      ...node.data
    })
  }
}

// 更新边数据
const updateEdgeData = (key: string) => {
  if (!selectedEdge.value) return
  
  const edge = selectedEdge.value
  
  // 确保edge.data存在
  if (!edge.data) {
    edge.data = {}
  }
  
  // 更新边数据
  updateEdge(edge.id, {
    ...edge.data
  })
}

// 删除节点
const removeNode = () => {
  if (!selectedNode.value) return
  removeNodes([selectedNode.value.id])
  emit('update:selectedElementId', '')
}

// 删除边
const removeEdge = () => {
  if (!selectedEdge.value) return
  removeEdges([selectedEdge.value.id])
  emit('update:selectedElementId', '')
}
</script>

<style scoped>
.property-panel {
  width: 100%;
  min-width: 240px;
}
</style> 
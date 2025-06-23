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
          :value="selectedNode.id" 
          disabled
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">节点名称</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getNodeProperty('label')"
          @input="setNodeProperty('label', ($event.target as HTMLInputElement).value)"
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">节点类型</label>
        <select 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getNodeProperty('type')"
          @change="setNodeProperty('type', ($event.target as HTMLSelectElement).value)"
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
      <div v-if="getNodeProperty('type') === 'action'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">动作类型</label>
        <select 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getNodeProperty('properties.actionType')"
          @change="setNodeProperty('properties.actionType', ($event.target as HTMLSelectElement).value)"
        >
          <option value="httpRequest">HTTP请求</option>
          <option value="executeScript">执行脚本</option>
          <option value="sendNotification">发送通知</option>
          <option value="deviceControl">设备控制</option>
        </select>
      </div>
      
      <div v-if="getNodeProperty('type') === 'action' && getNodeProperty('properties.actionType') === 'deviceControl'" class="mb-4">
        <device-action-manager
          v-model="deviceActions"
          @update:modelValue="updateDeviceActions"
        />
      </div>
      
      <div v-if="getNodeProperty('type') === 'condition'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">条件表达式</label>
        <div class="flex justify-between items-center">
          <span class="text-xs text-gray-500">
            {{ getNodeProperty('properties.condition') ? '已配置条件表达式' : '未配置条件表达式' }}
          </span>
          <button 
            class="px-2 py-1 bg-blue-500 text-white text-xs rounded hover:bg-blue-600"
            @click="openNodeConditionEditor"
          >
            编辑表达式
          </button>
        </div>
      </div>
      
      <div v-if="getNodeProperty('type') === 'delay'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">延迟时间（秒）</label>
        <input 
          type="number" 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getNodeProperty('properties.delaySeconds', 0)"
          @input="setNodeProperty('properties.delaySeconds', Number(($event.target as HTMLInputElement).value))"
        />
      </div>
      
      <div v-if="getNodeProperty('type') === 'timer'" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">CRON表达式</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getNodeProperty('properties.cronExpression')"
          @input="setNodeProperty('properties.cronExpression', ($event.target as HTMLInputElement).value)"
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
        <textarea 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getNodeProperty('description')"
          @input="setNodeProperty('description', ($event.target as HTMLTextAreaElement).value)"
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
          :value="selectedEdge.id" 
          disabled
        />
      </div>
      
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">标签</label>
        <input 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getEdgeProperty('label')"
          @input="setEdgeProperty('label', ($event.target as HTMLInputElement).value)"
        />
      </div>
      
      <div class="mb-4">
        <label class="flex items-center">
          <input 
            type="checkbox" 
            class="mr-2" 
            :checked="getEdgeProperty('isConditional', false)"
            @change="setEdgeProperty('isConditional', ($event.target as HTMLInputElement).checked)"
          />
          <span class="text-sm font-medium text-gray-700">是否条件分支</span>
        </label>
      </div>
      
      <div v-if="getEdgeProperty('isConditional', false)" class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-1">条件表达式</label>
        <div class="flex justify-between items-center">
          <span class="text-xs text-gray-500">
            {{ getEdgeProperty('conditionExpression') ? '已配置条件表达式' : '未配置条件表达式' }}
          </span>
          <button 
            class="px-2 py-1 bg-blue-500 text-white text-xs rounded hover:bg-blue-600"
            @click="openEdgeConditionEditor"
          >
            编辑表达式
          </button>
        </div>
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
    
    <!-- 条件表达式编辑对话框 -->
    <condition-dialog
      v-model:visible="conditionDialogVisible"
      :title="conditionDialogTitle"
      :expression="currentConditionExpression || undefined"
      @save="saveConditionExpression"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useVueFlow } from '@vue-flow/core'
import ConditionDialog from './condition/ConditionDialog.vue'
import DeviceActionManager from './device/DeviceActionManager.vue'
import type { ConditionExpression } from './condition/types'

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

// 条件表达式编辑对话框
const conditionDialogVisible = ref(false)
const conditionDialogTitle = ref('')
const editingConditionFor = ref<'node' | 'edge' | null>(null)
const currentConditionExpression = ref<ConditionExpression | null>(null)

const deviceActions = ref<any[]>([])

// 确保节点数据属性初始化
const ensureNodeData = () => {
  if (!selectedNode.value) return
  
  if (!selectedNode.value.data) {
    selectedNode.value.data = {}
  }
  
  if (!selectedNode.value.data.properties) {
    selectedNode.value.data.properties = {}
  }
}

// 确保边数据属性初始化
const ensureEdgeData = () => {
  if (!selectedEdge.value) return
  
  if (!selectedEdge.value.data) {
    selectedEdge.value.data = {}
  }
}

// 初始化数据
watch(selectedNode, () => {
  ensureNodeData()
  
  // 如果是设备控制节点，加载设备动作
  if (selectedNode.value && 
      getNodeProperty('type') === 'action' && 
      getNodeProperty('properties.actionType') === 'deviceControl') {
    const actions = getNodeProperty('properties.deviceActions', [])
    deviceActions.value = Array.isArray(actions) ? actions : []
  } else {
    deviceActions.value = []
  }
}, { immediate: true })

watch(selectedEdge, () => {
  ensureEdgeData()
}, { immediate: true })

// 监听selectedElementId变化，这对于双击节点交互尤其重要
watch(() => props.selectedElementId, (newId) => {
  console.log('属性面板: 选中元素ID变更为', newId)
  if (newId) {
    // 刷新节点/边数据
    const node = findNode(newId)
    if (node) {
      ensureNodeData()
    } else {
      const edge = getEdges.value.find((e: any) => e.id === newId)
      if (edge) {
        ensureEdgeData()
      }
    }
  }
}, { immediate: true })

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

// 获取节点属性值（安全访问）
const getNodeProperty = (property: string, defaultValue: any = '') => {
  if (!selectedNode.value || !selectedNode.value.data) return defaultValue
  
  const parts = property.split('.')
  if (parts.length === 1) {
    return selectedNode.value.data[property] ?? defaultValue
  } else if (parts.length === 2) {
    const [main, sub] = parts
    return selectedNode.value.data[main]?.[sub] ?? defaultValue
  }
  
  return defaultValue
}

// 获取边属性值（安全访问）
const getEdgeProperty = (property: string, defaultValue: any = '') => {
  if (!selectedEdge.value || !selectedEdge.value.data) return defaultValue
  
  return selectedEdge.value.data[property] ?? defaultValue
}

// 设置节点属性值
const setNodeProperty = (property: string, value: any) => {
  if (!selectedNode.value || !selectedNode.value.data) return
  
  const parts = property.split('.')
  if (parts.length === 1) {
    selectedNode.value.data[property] = value
  } else if (parts.length === 2) {
    const [main, sub] = parts
    if (!selectedNode.value.data[main]) {
      selectedNode.value.data[main] = {}
    }
    selectedNode.value.data[main][sub] = value
  }
  
  updateNodeData(property)
}

// 设置边属性值
const setEdgeProperty = (property: string, value: any) => {
  if (!selectedEdge.value || !selectedEdge.value.data) return
  
  selectedEdge.value.data[property] = value
  updateEdgeData(property)
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

// 打开节点条件表达式编辑器
const openNodeConditionEditor = () => {
  if (!selectedNode.value) return
  
  ensureNodeData()
  
  editingConditionFor.value = 'node'
  conditionDialogTitle.value = '编辑节点条件表达式'
  
  try {
    // 尝试解析现有条件表达式
    const conditionStr = getNodeProperty('properties.condition')
    if (conditionStr) {
      currentConditionExpression.value = JSON.parse(conditionStr)
    } else {
      currentConditionExpression.value = null
    }
  } catch (e) {
    console.error('解析条件表达式失败', e)
    currentConditionExpression.value = null
  }
  
  conditionDialogVisible.value = true
}

// 打开边条件表达式编辑器
const openEdgeConditionEditor = () => {
  if (!selectedEdge.value) return
  
  ensureEdgeData()
  
  editingConditionFor.value = 'edge'
  conditionDialogTitle.value = '编辑连线条件表达式'
  
  try {
    // 尝试解析现有条件表达式
    const conditionStr = getEdgeProperty('conditionExpression')
    if (conditionStr) {
      currentConditionExpression.value = JSON.parse(conditionStr)
    } else {
      currentConditionExpression.value = null
    }
  } catch (e) {
    console.error('解析条件表达式失败', e)
    currentConditionExpression.value = null
  }
  
  conditionDialogVisible.value = true
}

// 保存条件表达式
const saveConditionExpression = (expression: ConditionExpression) => {
  const expressionStr = JSON.stringify(expression)
  
  if (editingConditionFor.value === 'node' && selectedNode.value) {
    setNodeProperty('properties.condition', expressionStr)
  } else if (editingConditionFor.value === 'edge' && selectedEdge.value) {
    setEdgeProperty('conditionExpression', expressionStr)
  }
  
  // 重置状态
  editingConditionFor.value = null
  currentConditionExpression.value = null
}

// 更新设备动作
function updateDeviceActions(actions: any[]) {
  setNodeProperty('properties.deviceActions', actions)
}
</script>

<style scoped>
.property-panel {
  width: 100%;
  min-width: 240px;
}
</style> 
<template>
  <div class="device-action-manager">
    <h3 class="text-lg font-bold mb-4">设备动作配置</h3>
    
    <!-- 已配置的操作列表 -->
    <div v-if="actions.length > 0" class="mb-4">
      <h4 class="text-sm font-medium text-gray-700 mb-2">已配置操作</h4>
      
      <div 
        v-for="(action, index) in actions" 
        :key="index"
        class="bg-white border rounded mb-2 p-3"
      >
        <div class="flex justify-between items-start">
          <div>
            <div class="font-medium text-sm">
              {{ getDeviceName(action.deviceId) }} - {{ getOperationName(action.deviceId, action.operationId) }}
            </div>
            <div v-if="hasParameters(action)" class="mt-1 text-xs text-gray-500">
              <div v-for="(value, key) in action.parameters" :key="key" class="mb-1">
                {{ key }}: {{ formatParameterValue(value) }}
              </div>
            </div>
            <div v-else class="mt-1 text-xs text-gray-500">
              无参数
            </div>
          </div>
          
          <div class="flex space-x-1">
            <button 
              class="text-xs bg-blue-100 text-blue-700 px-2 py-1 rounded hover:bg-blue-200"
              @click="editAction(index)"
              title="编辑"
            >
              编辑
            </button>
            <button 
              class="text-xs bg-red-100 text-red-700 px-2 py-1 rounded hover:bg-red-200"
              @click="removeAction(index)"
              title="删除"
            >
              删除
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加新操作表单 -->
    <div v-if="isAdding || actions.length === 0" class="bg-gray-50 p-4 rounded border mb-4">
      <h4 class="text-sm font-medium text-gray-700 mb-3">
        {{ editingIndex !== null ? '编辑操作' : '添加新操作' }}
      </h4>
      
      <div class="mb-4">
        <!-- 设备选择 -->
        <device-selector
          v-model="selectedDeviceId"
          @select="onDeviceSelect"
        />
        
        <!-- 操作选择 -->
        <operation-selector
          v-model="selectedOperationId"
          :device-id="selectedDeviceId"
          @select="onOperationSelect"
          @update:parameters="onParametersUpdate"
        />
      </div>
      
      <div class="flex justify-end space-x-2">
        <button 
          v-if="editingIndex !== null"
          class="px-3 py-1 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
          @click="cancelEdit"
        >
          取消
        </button>
        <button 
          class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
          @click="saveAction"
          :disabled="!canSave"
        >
          {{ editingIndex !== null ? '更新' : '添加' }}
        </button>
      </div>
    </div>
    
    <!-- 添加操作按钮 -->
    <div v-if="!isAdding && actions.length > 0" class="mb-4">
      <button 
        class="px-3 py-2 bg-green-500 text-white rounded hover:bg-green-600 w-full"
        @click="startAddAction"
      >
        添加新操作
      </button>
    </div>
    
    <!-- 操作预览 -->
    <div v-if="actions.length > 0" class="mb-4">
      <h4 class="text-sm font-medium text-gray-700 mb-2">操作预览</h4>
      <div class="bg-gray-100 p-3 rounded text-xs font-mono whitespace-pre-wrap">{{ actionsPreview }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import DeviceSelector from './DeviceSelector.vue'
import OperationSelector from './OperationSelector.vue'
import { useDeviceStore } from '../../../stores/deviceStore'
import type { Device, DeviceOperation } from '../../../api'

// 定义设备动作接口
interface DeviceAction {
  deviceId: number
  operationId: number
  parameters: Record<string, any>
}

const props = defineProps<{
  modelValue: DeviceAction[] // 双向绑定的动作数组
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: DeviceAction[]): void
}>()

const deviceStore = useDeviceStore()

// 本地状态
const actions = computed({
  get: () => props.modelValue || [],
  set: (value) => emit('update:modelValue', value)
})

const isAdding = ref(false)
const editingIndex = ref<number | null>(null)
const selectedDeviceId = ref<number | null>(null)
const selectedOperationId = ref<number | null>(null)
const selectedParameters = ref<Record<string, any>>({})

// 计算属性
const canSave = computed(() => {
  return selectedDeviceId.value !== null && selectedOperationId.value !== null
})

const actionsPreview = computed(() => {
  return JSON.stringify(actions.value, null, 2)
})

// 操作方法
function startAddAction() {
  isAdding.value = true
  editingIndex.value = null
  selectedDeviceId.value = null
  selectedOperationId.value = null
  selectedParameters.value = {}
}

function editAction(index: number) {
  const action = actions.value[index]
  editingIndex.value = index
  isAdding.value = true
  
  // 设置编辑值
  selectedDeviceId.value = action.deviceId
  selectedOperationId.value = action.operationId
  selectedParameters.value = { ...action.parameters }
  
  // 加载设备操作列表
  deviceStore.loadMockOperations(action.deviceId)
}

function cancelEdit() {
  isAdding.value = false
  editingIndex.value = null
  selectedDeviceId.value = null
  selectedOperationId.value = null
  selectedParameters.value = {}
}

function saveAction() {
  if (!selectedDeviceId.value || !selectedOperationId.value) return
  
  const newAction: DeviceAction = {
    deviceId: selectedDeviceId.value,
    operationId: selectedOperationId.value,
    parameters: { ...selectedParameters.value }
  }
  
  const actionsCopy = [...actions.value]
  
  if (editingIndex.value !== null) {
    // 更新现有操作
    actionsCopy[editingIndex.value] = newAction
  } else {
    // 添加新操作
    actionsCopy.push(newAction)
  }
  
  actions.value = actionsCopy
  
  // 重置表单
  if (actionsCopy.length > 0) {
    isAdding.value = false
  }
  editingIndex.value = null
  selectedDeviceId.value = null
  selectedOperationId.value = null
  selectedParameters.value = {}
}

function removeAction(index: number) {
  const actionsCopy = [...actions.value]
  actionsCopy.splice(index, 1)
  actions.value = actionsCopy
}

// 事件处理
function onDeviceSelect(device: Device | null) {
  // 设备变更时清空操作选择
  selectedOperationId.value = null
  selectedParameters.value = {}
}

function onOperationSelect(operation: DeviceOperation | null) {
  // 操作选择变更时处理
  if (!operation) {
    selectedParameters.value = {}
  }
}

function onParametersUpdate(params: Record<string, any>) {
  selectedParameters.value = params
}

// 辅助方法
function getDeviceName(deviceId: number): string {
  const device = deviceStore.devices.find(d => d.id === deviceId)
  return device ? device.name : `设备 #${deviceId}`
}

function getOperationName(deviceId: number, operationId: number): string {
  // 先加载操作列表
  const operations = deviceStore.loadMockOperations(deviceId)
  const operation = operations.find(op => op.id === operationId)
  return operation ? operation.name : `操作 #${operationId}`
}

function hasParameters(action: DeviceAction): boolean {
  return Object.keys(action.parameters).length > 0
}

function formatParameterValue(value: any): string {
  if (typeof value === 'boolean') {
    return value ? '是' : '否'
  }
  return String(value)
}
</script> 
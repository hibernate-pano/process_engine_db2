<template>
  <div class="operation-selector">
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">操作</label>
      <select
        class="w-full px-3 py-2 border rounded text-sm"
        :value="modelValue"
        @change="onOperationChange"
        :disabled="!deviceId"
      >
        <option value="">请选择操作</option>
        <option 
          v-for="operation in operations" 
          :key="operation.id" 
          :value="operation.id"
        >
          {{ operation.name }}
        </option>
      </select>
      <p v-if="!deviceId" class="text-xs text-yellow-500 mt-1">请先选择设备</p>
    </div>

    <div v-if="selectedOperation" class="mb-4">
      <div class="bg-gray-50 p-3 rounded mb-3">
        <h4 class="text-sm font-medium text-gray-700 mb-1">操作详情</h4>
        <div class="text-xs text-gray-600">{{ selectedOperation.description }}</div>
      </div>

      <h4 class="text-sm font-medium text-gray-700 mb-2">参数配置</h4>
      
      <div v-if="selectedOperation.parameters.length === 0" class="text-xs text-gray-500 mb-3">
        此操作无需配置参数
      </div>
      
      <div 
        v-for="(param, index) in selectedOperation.parameters" 
        :key="index" 
        class="mb-3"
      >
        <div class="flex items-center mb-1">
          <label class="text-xs font-medium text-gray-700">
            {{ param.name }}
            <span v-if="param.required" class="text-red-500">*</span>
          </label>
          <span v-if="param.description" class="ml-1 text-xs text-gray-500">
            ({{ param.description }})
          </span>
        </div>
        
        <!-- 字符串参数 -->
        <input 
          v-if="param.type === 'string'" 
          type="text" 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getParameterValue(param.name, param.defaultValue)"
          @input="updateParameterValue(param.name, ($event.target as HTMLInputElement).value)"
        />
        
        <!-- 数字参数 -->
        <input 
          v-else-if="param.type === 'number'" 
          type="number" 
          class="w-full px-3 py-2 border rounded text-sm" 
          :value="getParameterValue(param.name, param.defaultValue)"
          @input="updateParameterValue(param.name, Number(($event.target as HTMLInputElement).value))"
        />
        
        <!-- 布尔参数 -->
        <div v-else-if="param.type === 'boolean'" class="flex items-center">
          <input 
            type="checkbox" 
            class="mr-2" 
            :checked="getParameterValue(param.name, param.defaultValue)"
            @change="updateParameterValue(param.name, ($event.target as HTMLInputElement).checked)"
          />
          <span class="text-xs">{{ getParameterValue(param.name, param.defaultValue) ? '是' : '否' }}</span>
        </div>
        
        <!-- 选择参数 -->
        <select 
          v-else-if="param.type === 'select'" 
          class="w-full px-3 py-2 border rounded text-sm"
          :value="getParameterValue(param.name, param.defaultValue)"
          @change="updateParameterValue(param.name, ($event.target as HTMLSelectElement).value)"
        >
          <option 
            v-for="(option, optIndex) in param.options" 
            :key="optIndex" 
            :value="option.value"
          >
            {{ option.label }}
          </option>
        </select>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useDeviceStore } from '../../../stores/deviceStore'
import type { DeviceOperation } from '../../../api'

const props = defineProps<{
  deviceId: number | null // 设备ID
  modelValue: number | null // 选中的操作ID
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: number | null): void
  (e: 'select', operation: DeviceOperation | null): void
  (e: 'update:parameters', parameters: Record<string, any>): void
}>()

const deviceStore = useDeviceStore()

// 操作参数
const parameters = ref<Record<string, any>>({})

// 从store获取操作列表
const operations = computed(() => deviceStore.operations)
const selectedOperation = computed(() => {
  if (!props.modelValue) return null
  return operations.value.find(op => op.id === props.modelValue) || null
})

// 获取参数值，如果未设置则使用默认值
function getParameterValue(name: string, defaultValue: any): any {
  return parameters.value[name] !== undefined ? parameters.value[name] : defaultValue
}

// 更新参数值
function updateParameterValue(name: string, value: any): void {
  parameters.value[name] = value
  emit('update:parameters', { ...parameters.value })
}

// 切换选中操作
function onOperationChange(event: Event) {
  const value = (event.target as HTMLSelectElement).value
  const operationId = value ? parseInt(value) : null
  
  emit('update:modelValue', operationId)
  
  if (operationId) {
    const operation = operations.value.find(op => op.id === operationId) || null
    deviceStore.selectOperation(operation)
    emit('select', operation)
    
    // 重置参数
    resetParameters(operation)
  } else {
    deviceStore.selectOperation(null)
    emit('select', null)
    parameters.value = {}
    emit('update:parameters', {})
  }
}

// 重置参数为默认值
function resetParameters(operation: DeviceOperation | null): void {
  if (!operation) {
    parameters.value = {}
    return
  }
  
  const newParameters: Record<string, any> = {}
  operation.parameters.forEach(param => {
    if (param.defaultValue !== undefined) {
      newParameters[param.name] = param.defaultValue
    }
  })
  
  parameters.value = newParameters
  emit('update:parameters', { ...parameters.value })
}

// 监听设备ID的变化，重新加载操作
watch(() => props.deviceId, async (newDeviceId) => {
  if (newDeviceId) {
    try {
      // 在真实环境中使用API加载
      // await deviceStore.fetchOperations(newDeviceId)
      
      // 使用模拟数据
      deviceStore.loadMockOperations(newDeviceId)
      
      // 重置选中的操作
      emit('update:modelValue', null)
      emit('select', null)
      parameters.value = {}
      emit('update:parameters', {})
    } catch (error) {
      console.error('加载操作失败:', error)
    }
  } else {
    // 清空操作列表
    deviceStore.operations.length = 0
    emit('update:modelValue', null)
    emit('select', null)
    parameters.value = {}
    emit('update:parameters', {})
  }
})

// 监听操作ID的变化
watch(() => props.modelValue, (newOperationId) => {
  if (newOperationId) {
    const operation = operations.value.find(op => op.id === newOperationId) || null
    deviceStore.selectOperation(operation)
    if (operation) {
      resetParameters(operation)
    }
  } else {
    deviceStore.selectOperation(null)
    parameters.value = {}
    emit('update:parameters', {})
  }
})

// 组件挂载时如果已有选中操作，触发一次选择
onMounted(() => {
  if (props.deviceId && props.modelValue) {
    const operation = operations.value.find(op => op.id === props.modelValue) || null
    if (operation) {
      deviceStore.selectOperation(operation)
      resetParameters(operation)
    }
  }
})
</script> 
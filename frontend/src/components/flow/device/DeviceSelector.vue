<template>
  <div class="device-selector">
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">设备</label>
      <select
        class="w-full px-3 py-2 border rounded text-sm"
        :value="modelValue"
        @change="onDeviceChange"
      >
        <option value="">请选择设备</option>
        <option 
          v-for="device in devices" 
          :key="device.id" 
          :value="device.id"
        >
          {{ device.name }} ({{ getDeviceTypeText(device.type) }})
        </option>
      </select>
    </div>

    <div v-if="selectedDevice" class="bg-gray-50 p-3 rounded mb-4">
      <h4 class="text-sm font-medium text-gray-700 mb-2">设备详情</h4>
      <div class="grid grid-cols-2 gap-2 text-xs">
        <div>
          <span class="text-gray-500">设备类型:</span>
          <span class="ml-1">{{ getDeviceTypeText(selectedDevice.type) }}</span>
        </div>
        <div>
          <span class="text-gray-500">状态:</span>
          <span 
            class="ml-1" 
            :class="{
              'text-green-500': selectedDevice.status === 'online',
              'text-red-500': selectedDevice.status === 'offline',
              'text-yellow-500': selectedDevice.status === 'warning'
            }"
          >
            {{ getDeviceStatusText(selectedDevice.status) }}
          </span>
        </div>
        <div v-if="selectedDevice.location">
          <span class="text-gray-500">位置:</span>
          <span class="ml-1">{{ selectedDevice.location }}</span>
        </div>
        <div v-if="selectedDevice.ipAddress">
          <span class="text-gray-500">IP地址:</span>
          <span class="ml-1">{{ selectedDevice.ipAddress }}</span>
        </div>
      </div>
      <div v-if="selectedDevice.description" class="mt-2 text-xs">
        <span class="text-gray-500">描述:</span>
        <span class="ml-1">{{ selectedDevice.description }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, watch } from 'vue'
import { useDeviceStore } from '../../../stores/deviceStore'
import type { Device } from '../../../api'

const props = defineProps<{
  modelValue: number | null // 选中的设备ID
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: number | null): void
  (e: 'select', device: Device | null): void
}>()

const deviceStore = useDeviceStore()

// 从store获取设备列表
const devices = computed(() => deviceStore.devices)
const selectedDevice = computed(() => {
  if (!props.modelValue) return null
  return devices.value.find(d => d.id === props.modelValue) || null
})

// 类型和状态文本映射
const deviceTypeMap: Record<string, string> = {
  'climate': '空调控制',
  'lighting': '照明控制',
  'security': '安防设备',
  'sensor': '传感器',
  'camera': '摄像设备',
  'audio': '音频设备',
  'door': '门禁控制',
  'other': '其他设备'
}

const deviceStatusMap: Record<string, string> = {
  'online': '在线',
  'offline': '离线',
  'warning': '告警',
  'error': '错误',
  'maintenance': '维护中'
}

function getDeviceTypeText(type: string): string {
  return deviceTypeMap[type] || type
}

function getDeviceStatusText(status: string): string {
  return deviceStatusMap[status] || status
}

// 切换选中设备
function onDeviceChange(event: Event) {
  const value = (event.target as HTMLSelectElement).value
  const deviceId = value ? parseInt(value) : null
  
  emit('update:modelValue', deviceId)
  
  if (deviceId) {
    const device = devices.value.find(d => d.id === deviceId) || null
    deviceStore.selectDevice(device)
    emit('select', device)
  } else {
    deviceStore.selectDevice(null)
    emit('select', null)
  }
}

// 监听model-value的变化
watch(() => props.modelValue, (newValue) => {
  if (newValue) {
    const device = devices.value.find(d => d.id === newValue) || null
    deviceStore.selectDevice(device)
  }
})

// 组件挂载时加载设备数据
onMounted(async () => {
  try {
    // 在真实环境中使用API加载
    // await deviceStore.fetchDevices()
    
    // 使用模拟数据
    deviceStore.loadMockDevices()
    
    // 如果已有选中设备，触发一次选择
    if (props.modelValue) {
      const device = devices.value.find(d => d.id === props.modelValue) || null
      deviceStore.selectDevice(device)
    }
  } catch (error) {
    console.error('加载设备失败:', error)
  }
})
</script> 
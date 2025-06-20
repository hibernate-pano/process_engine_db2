import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { deviceApi } from '../api'
import type { Device, DeviceOperation, DeviceOperationParameter, PageResponse } from '../api'

export const useDeviceStore = defineStore('device', () => {
  // 状态
  const devices = ref<Device[]>([])
  const operations = ref<DeviceOperation[]>([])
  const selectedDevice = ref<Device | null>(null)
  const selectedOperation = ref<DeviceOperation | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  // 计算属性
  const deviceOptions = computed(() => {
    return devices.value.map(device => ({
      value: device.id,
      label: device.name,
      device
    }))
  })

  const operationOptions = computed(() => {
    return operations.value.map(operation => ({
      value: operation.id,
      label: operation.name,
      operation
    }))
  })

  // 动作
  async function fetchDevices(params?: any) {
    loading.value = true
    error.value = null

    try {
      const response = await deviceApi.list(params)
      devices.value = response.content || []
      return response
    } catch (err: any) {
      error.value = err.message || '加载设备失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function fetchDevice(id: number) {
    loading.value = true
    error.value = null

    try {
      const device = await deviceApi.get(id)
      selectedDevice.value = device
      return device
    } catch (err: any) {
      error.value = err.message || '加载设备详情失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function fetchOperations(deviceId: number) {
    loading.value = true
    error.value = null

    try {
      const ops = await deviceApi.getOperations(deviceId)
      operations.value = ops || []
      return ops
    } catch (err: any) {
      error.value = err.message || '加载设备操作失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function fetchOperation(deviceId: number, operationId: number) {
    loading.value = true
    error.value = null

    try {
      const operation = await deviceApi.getOperation(deviceId, operationId)
      selectedOperation.value = operation
      return operation
    } catch (err: any) {
      error.value = err.message || '加载操作详情失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function executeOperation(deviceId: number, operationId: number, parameters: Record<string, any>) {
    loading.value = true
    error.value = null

    try {
      const result = await deviceApi.executeOperation(deviceId, operationId, parameters)
      return result
    } catch (err: any) {
      error.value = err.message || '执行操作失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  function selectDevice(device: Device | null) {
    selectedDevice.value = device
    // 清空选中的操作
    selectedOperation.value = null
    
    // 如果选择了设备，自动加载操作
    if (device) {
      fetchOperations(device.id)
    } else {
      operations.value = []
    }
  }

  function selectOperation(operation: DeviceOperation | null) {
    selectedOperation.value = operation
  }

  // 用于模拟数据
  function loadMockDevices() {
    const mockDevices: Device[] = [
      { id: 1, name: '空调控制器', type: 'climate', status: 'online', location: '1楼大厅' },
      { id: 2, name: '照明控制器', type: 'lighting', status: 'online', location: '1楼大厅' },
      { id: 3, name: '门禁系统', type: 'security', status: 'online', location: '1楼入口' },
      { id: 4, name: '温湿度传感器', type: 'sensor', status: 'online', location: '1楼会议室' },
      { id: 5, name: '摄像头', type: 'camera', status: 'online', location: '1楼走廊' }
    ]
    
    devices.value = mockDevices
    return mockDevices
  }

  function loadMockOperations(deviceId: number) {
    let mockOperations: DeviceOperation[] = []
    
    switch(deviceId) {
      case 1: // 空调
        mockOperations = [
          {
            id: 101,
            deviceId: 1,
            name: '设置温度',
            description: '设置空调目标温度',
            type: 'control',
            parameters: [
              {
                name: 'temperature',
                type: 'number',
                required: true,
                defaultValue: 24,
                description: '目标温度 (16-30°C)'
              }
            ]
          },
          {
            id: 102,
            deviceId: 1,
            name: '设置模式',
            description: '设置空调工作模式',
            type: 'control',
            parameters: [
              {
                name: 'mode',
                type: 'select',
                required: true,
                defaultValue: 'cool',
                description: '工作模式',
                options: [
                  { value: 'cool', label: '制冷' },
                  { value: 'heat', label: '制热' },
                  { value: 'fan', label: '送风' },
                  { value: 'dry', label: '除湿' },
                  { value: 'auto', label: '自动' }
                ]
              }
            ]
          },
          {
            id: 103,
            deviceId: 1,
            name: '开关控制',
            description: '打开或关闭空调',
            type: 'control',
            parameters: [
              {
                name: 'power',
                type: 'boolean',
                required: true,
                defaultValue: true,
                description: '开关状态'
              }
            ]
          }
        ]
        break
        
      case 2: // 照明
        mockOperations = [
          {
            id: 201,
            deviceId: 2,
            name: '开关控制',
            description: '打开或关闭照明',
            type: 'control',
            parameters: [
              {
                name: 'power',
                type: 'boolean',
                required: true,
                defaultValue: true,
                description: '开关状态'
              }
            ]
          },
          {
            id: 202,
            deviceId: 2,
            name: '调整亮度',
            description: '调整照明亮度',
            type: 'control',
            parameters: [
              {
                name: 'brightness',
                type: 'number',
                required: true,
                defaultValue: 80,
                description: '亮度百分比 (0-100)'
              }
            ]
          }
        ]
        break
        
      default:
        mockOperations = [
          {
            id: 901,
            deviceId,
            name: '查询状态',
            description: '查询设备当前状态',
            type: 'query',
            parameters: []
          }
        ]
    }
    
    operations.value = mockOperations
    return mockOperations
  }

  return {
    // 状态
    devices,
    operations,
    selectedDevice,
    selectedOperation,
    loading,
    error,
    
    // 计算属性
    deviceOptions,
    operationOptions,
    
    // 动作
    fetchDevices,
    fetchDevice,
    fetchOperations,
    fetchOperation,
    executeOperation,
    selectDevice,
    selectOperation,
    
    // 模拟数据
    loadMockDevices,
    loadMockOperations
  }
}) 
<template>
  <div 
    class="flow-node relative p-2 min-w-[120px] min-h-[40px] flex items-center justify-center"
    :class="[
      `node-type-${data.type || 'default'}`,
      { 'selected': selected }
    ]"
    :style="nodeStyle"
    @click="handleClick"
    @dblclick="handleDbClick"
  >
    <div class="node-content">
      <div class="node-label text-center">{{ data.label }}</div>
    </div>
    
    <!-- 连接点使用简单的 div 元素实现，保留必要的数据属性 -->
    <!-- 顶部连接点 -->
    <div
      v-if="isValidTopConnection"
      class="vue-flow__handle vue-flow__handle-top vue-flow__handle-connecting absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 cursor-crosshair"
      :class="{ 'handle-valid': isConnecting, 'handle-connecting': isConnecting }"
      style="opacity: 1; z-index: 10;"
      data-handleid="top"
      :data-nodeid="id"
      data-type="target"
    ></div>
    
    <!-- 右侧连接点 -->
    <div
      v-if="isValidRightConnection"
      class="vue-flow__handle vue-flow__handle-right vue-flow__handle-connecting absolute top-1/2 right-0 transform translate-x-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 cursor-crosshair"
      :class="{ 'handle-valid': isConnecting, 'handle-connecting': isConnecting }"
      style="opacity: 1; z-index: 10;"
      data-handleid="right"
      :data-nodeid="id"
      data-type="source"
    ></div>
    
    <!-- 底部连接点 -->
    <div
      v-if="isValidBottomConnection"
      class="vue-flow__handle vue-flow__handle-bottom vue-flow__handle-connecting absolute bottom-0 left-1/2 transform -translate-x-1/2 translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 cursor-crosshair"
      :class="{ 'handle-valid': isConnecting, 'handle-connecting': isConnecting }"
      style="opacity: 1; z-index: 10;"
      data-handleid="bottom"
      :data-nodeid="id"
      data-type="source"
    ></div>
    
    <!-- 左侧连接点 -->
    <div
      v-if="isValidLeftConnection"
      class="vue-flow__handle vue-flow__handle-left vue-flow__handle-connecting absolute top-1/2 left-0 transform -translate-x-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 cursor-crosshair"
      :class="{ 'handle-valid': isConnecting, 'handle-connecting': isConnecting }"
      style="opacity: 1; z-index: 10;"
      data-handleid="left"
      :data-nodeid="id"
      data-type="target"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useVueFlow } from '@vue-flow/core'
import type { Position } from '@vue-flow/core'

const props = defineProps<{
  id: string
  data: any
  selected: boolean
  type: string
}>()

const emit = defineEmits(['click', 'updateNodeInternals', 'dblclick'])

// 连接状态
const isConnecting = ref(false)

// 获取 Vue Flow 的状态和方法
const { findNode, getConnections, isNodeSelected, onConnect, onConnectStart, onConnectEnd } = useVueFlow()

// 检查不同节点类型的有效连接
const isValidTopConnection = computed(() => {
  // 根据节点类型决定是否允许顶部连接
  return props.data.type !== 'start' // 起始节点顶部不允许作为目标
})

const isValidRightConnection = computed(() => {
  // 大多数节点右侧允许作为源
  return props.data.type !== 'end' // 结束节点右侧不允许作为源
})

const isValidBottomConnection = computed(() => {
  // 根据节点类型决定是否允许底部连接
  return props.data.type !== 'end' // 结束节点底部不允许作为源
})

const isValidLeftConnection = computed(() => {
  // 大多数节点左侧允许作为目标
  return props.data.type !== 'start' // 开始节点左侧不允许作为目标
})

// 节点样式，根据节点类型设置不同的样式
const nodeStyle = computed(() => {
  const style: Record<string, string> = {}
  
  switch (props.data.type) {
    case 'start':
      Object.assign(style, {
        backgroundColor: '#e0f2fe', // 浅蓝色
        borderColor: '#0ea5e9',     // 蓝色边框
      })
      break
    case 'end':
      Object.assign(style, {
        backgroundColor: '#fee2e2', // 浅红色
        borderColor: '#ef4444',     // 红色边框
      })
      break
    case 'action':
      Object.assign(style, {
        backgroundColor: '#d1fae5', // 浅绿色
        borderColor: '#10b981',     // 绿色边框
      })
      break
    case 'condition':
      Object.assign(style, {
        backgroundColor: '#fef3c7', // 浅黄色
        borderColor: '#f59e0b',     // 黄色边框
      })
      break
    case 'delay':
      Object.assign(style, {
        backgroundColor: '#f3e8ff', // 浅紫色
        borderColor: '#a855f7',     // 紫色边框
      })
      break
    default:
      Object.assign(style, {
        backgroundColor: '#f9fafb', // 浅灰色
        borderColor: '#6b7280',     // 灰色边框
      })
  }
  
  return style
})

// 处理节点点击事件
const handleClick = (event: MouseEvent) => {
  emit('click', {
    id: props.id,
    data: props.data,
    type: props.type,
    event
  })
}

// 处理节点双击事件 - 添加专门的处理函数
const handleDbClick = (event: MouseEvent) => {
  event.stopPropagation() // 防止触发单击事件  
  console.log('节点被双击:', props.id, props.data)
  emit('dblclick', {
    id: props.id,
    data: props.data,
    type: props.type,
    event,
    nodeType: props.data.type // 确保节点类型传递给父组件
  })
}

// 组件挂载后通知 Vue Flow 更新节点内部结构
setTimeout(() => {
  emit('updateNodeInternals')
}, 0)

// 监听连接状态
onConnectStart(() => {
  console.log('连接开始')
  isConnecting.value = true
})

onConnectEnd(() => {
  console.log('连接结束')
  isConnecting.value = false
})
</script>

<style scoped>
.flow-node {
  border: 2px solid #ddd;
  border-radius: 4px;
  min-width: 120px;
}

.node-type-start {
  border-radius: 24px;
}

.node-type-end {
  border-radius: 24px;
}

.node-type-condition {
  border-radius: 8px;
}

.selected {
  border-width: 2px;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
}

/* 连接点样式 */
.vue-flow__handle {
  width: 8px;
  height: 8px;
  background-color: #3b82f6;
  border: 2px solid #fff;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
}

.vue-flow__handle:hover {
  width: 12px;
  height: 12px;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
  transform: translate(-50%, -50%) scale(1.2);
}

.vue-flow__handle-connecting {
  background-color: #3b82f6;
  border: 2px solid #fff;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
  z-index: 5;
}

.handle-valid {
  background-color: #10b981 !important; /* 有效连接点 */
}

/* 特定位置连接点样式 */
.vue-flow__handle-top {
  top: 0;
  left: 50%;
  transform: translate(-50%, -50%);
}

.vue-flow__handle-right {
  top: 50%;
  right: 0;
  transform: translate(50%, -50%);
}

.vue-flow__handle-bottom {
  bottom: 0;
  left: 50%;
  transform: translate(-50%, 50%);
}

.vue-flow__handle-left {
  top: 50%;
  left: 0;
  transform: translate(-50%, -50%);
}
</style> 
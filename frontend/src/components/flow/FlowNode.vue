<template>
  <div 
    class="flow-node relative p-2 min-w-[120px] min-h-[40px] flex items-center justify-center"
    :class="[
      `node-type-${data.type || 'default'}`,
      { 'selected': selected }
    ]"
    :style="nodeStyle"
    @click="handleClick"
  >
    <div class="node-content">
      <div class="node-label text-center">{{ data.label }}</div>
    </div>
    
    <!-- 连接点 -->
    <div class="handle-container absolute w-full h-full pointer-events-none">
      <!-- 顶部连接点 -->
      <div 
        class="handle handle-top absolute top-0 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 pointer-events-auto"
        :class="{ 'handle-connecting': isConnecting }"
        data-handle-id="top"
        data-handlepos="top"
      ></div>
      
      <!-- 右侧连接点 -->
      <div 
        class="handle handle-right absolute top-1/2 right-0 transform translate-x-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 pointer-events-auto"
        :class="{ 'handle-connecting': isConnecting }"
        data-handle-id="right"
        data-handlepos="right"
      ></div>
      
      <!-- 底部连接点 -->
      <div 
        class="handle handle-bottom absolute bottom-0 left-1/2 transform -translate-x-1/2 translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 pointer-events-auto"
        :class="{ 'handle-connecting': isConnecting }"
        data-handle-id="bottom"
        data-handlepos="bottom"
      ></div>
      
      <!-- 左侧连接点 -->
      <div 
        class="handle handle-left absolute top-1/2 left-0 transform -translate-x-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-blue-500 pointer-events-auto"
        :class="{ 'handle-connecting': isConnecting }"
        data-handle-id="left"
        data-handlepos="left"
      ></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'

const props = defineProps<{
  id: string
  data: any
  selected: boolean
  type: string
}>()

const emit = defineEmits(['click'])

// 是否正在连接中
const isConnecting = ref(false)

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
</script>

<style scoped>
.flow-node {
  background-color: white;
  border: 2px solid #6b7280;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
  z-index: 1;
}

.flow-node.selected {
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
  z-index: 2;
}

/* 节点类型样式 */
.node-type-start {
  background-color: #e0f2fe;
  border-color: #0ea5e9;
}

.node-type-end {
  background-color: #fee2e2;
  border-color: #ef4444;
}

.node-type-action {
  background-color: #d1fae5;
  border-color: #10b981;
}

.node-type-condition {
  background-color: #fef3c7;
  border-color: #f59e0b;
}

.node-type-delay {
  background-color: #f3e8ff;
  border-color: #a855f7;
}

/* 连接点样式 */
.handle {
  opacity: 0;
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.flow-node:hover .handle,
.flow-node.selected .handle {
  opacity: 1;
}

.handle:hover {
  transform: scale(1.5) translate(var(--tw-translate-x), var(--tw-translate-y));
  cursor: crosshair;
}

.handle-connecting {
  opacity: 1;
  background-color: #f97316;
}
</style> 
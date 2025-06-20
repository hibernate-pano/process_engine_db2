<template>
  <div class="node-panel">
    <h3 class="text-sm font-bold mb-2">节点类型</h3>
    <div class="grid grid-cols-2 gap-2">
      <div
        v-for="nodeType in nodeTypes"
        :key="nodeType.type"
        class="node-item p-2 border rounded text-center cursor-move"
        :class="`node-type-${nodeType.type}`"
        draggable="true"
        @dragstart="onDragStart($event, nodeType)"
      >
        {{ nodeType.label }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 节点类型列表
const nodeTypes = ref([
  {
    type: 'start',
    label: '开始',
    style: {
      backgroundColor: '#e0f2fe',
      borderColor: '#0ea5e9'
    }
  },
  {
    type: 'end',
    label: '结束',
    style: {
      backgroundColor: '#fee2e2',
      borderColor: '#ef4444'
    }
  },
  {
    type: 'action',
    label: '动作',
    style: {
      backgroundColor: '#d1fae5',
      borderColor: '#10b981'
    }
  },
  {
    type: 'condition',
    label: '条件',
    style: {
      backgroundColor: '#fef3c7',
      borderColor: '#f59e0b'
    }
  },
  {
    type: 'delay',
    label: '延时',
    style: {
      backgroundColor: '#f3e8ff',
      borderColor: '#a855f7'
    }
  },
  {
    type: 'timer',
    label: '定时器',
    style: {
      backgroundColor: '#e0f2fe',
      borderColor: '#0ea5e9'
    }
  }
])

// 拖拽开始事件
const onDragStart = (event: DragEvent, nodeType: any) => {
  if (event.dataTransfer) {
    event.dataTransfer.setData('application/vueflow', JSON.stringify(nodeType))
    event.dataTransfer.effectAllowed = 'move'
  }
}
</script>

<style scoped>
.node-panel {
  padding: 0.5rem;
  background-color: white;
  border-radius: 0.375rem;
}

.node-item {
  transition: all 0.2s ease;
  user-select: none;
}

.node-item:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
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

.node-type-timer {
  background-color: #e0f2fe;
  border-color: #0ea5e9;
}
</style> 
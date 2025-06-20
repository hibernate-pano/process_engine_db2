<template>
  <div class="flow-edge-wrapper">
    <BaseEdge
      :id="id"
      :path="edgePath"
      :marker-end="markerEnd"
      class="flow-edge"
      :class="{ 'selected': selected }"
      @click="onEdgeClick"
    />
    
    <!-- 边标签 -->
    <EdgeLabelRenderer>
      <div
        v-if="data && data.label"
        class="edge-label nodrag nopan absolute py-1 px-2 rounded bg-white text-sm border shadow"
        :style="labelStyles"
        @click="onLabelClick"
      >
        {{ data.label }}
        <div v-if="data.isConditional" class="text-xs text-gray-500">
          条件: {{ data.conditionExpression || '未设置' }}
        </div>
      </div>
    </EdgeLabelRenderer>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { BaseEdge, EdgeLabelRenderer, getSmoothStepPath, useVueFlow } from '@vue-flow/core'
import type { Position } from '@vue-flow/core'

const props = defineProps<{
  id: string
  data?: any
  selected: boolean
  sourceX?: number
  sourceY?: number
  targetX?: number
  targetY?: number
  sourcePosition?: Position
  targetPosition?: Position
  path?: string
  markerEnd?: string
}>()

const emit = defineEmits(['click'])

const { edges } = useVueFlow()

// 获取当前边的数据
const edge = computed(() => {
  return edges.value.find((e: any) => e.id === props.id)
})

// 计算边的路径
const edgePath = computed(() => {
  if (props.path) {
    return props.path
  }
  
  if (!edge.value || !edge.value.sourceNode || !edge.value.targetNode) {
    return ''
  }
  
  const { sourceX, sourceY, targetX, targetY, sourcePosition, targetPosition } = edge.value
  
  // 使用平滑曲线路径
  return getSmoothStepPath({
    sourceX,
    sourceY,
    sourcePosition,
    targetX,
    targetY,
    targetPosition,
    borderRadius: 10,
  })
})

// 边的终点标记
const markerEnd = computed(() => {
  return props.markerEnd || 'url(#edge-arrow)'
})

// 计算标签位置
const labelStyles = computed(() => {
  if (!edge.value || !edge.value.sourceNode || !edge.value.targetNode) {
    return {}
  }
  
  const { sourceX, sourceY, targetX, targetY } = edge.value
  
  // 计算标签在连线中间位置
  const centerX = (sourceX + targetX) / 2
  const centerY = (sourceY + targetY) / 2
  
  return {
    transform: `translate(-50%, -50%) translate(${centerX}px, ${centerY}px)`,
    pointerEvents: 'all' as const,
  }
})

// 边点击事件
const onEdgeClick = (event: MouseEvent) => {
  emit('click', {
    id: props.id,
    data: props.data,
    event
  })
}

// 标签点击事件
const onLabelClick = (event: MouseEvent) => {
  event.stopPropagation()
  emit('click', {
    id: props.id,
    data: props.data,
    isLabel: true,
    event
  })
}
</script>

<style scoped>
.flow-edge {
  stroke: #555;
  stroke-width: 2;
  transition: stroke 0.3s ease;
}

.flow-edge.selected {
  stroke: #3b82f6;
  stroke-width: 3;
}

.edge-label {
  background-color: white;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
  pointer-events: all;
  user-select: none;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.edge-label:hover {
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transform: translate(var(--tw-translate-x), var(--tw-translate-y)) scale(1.05);
}
</style>
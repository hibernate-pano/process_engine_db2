<template>
  <div class="condition-editor">
    <div class="text-lg font-medium mb-3">条件表达式编辑器</div>

    <div class="condition-wrapper">
      <expression-builder 
        v-model="expression" 
        :level="0"
        @update:model-value="handleExpressionChange" 
      />
    </div>

    <div class="mt-4 flex gap-2 justify-end">
      <button 
        class="px-3 py-1 bg-gray-100 hover:bg-gray-200 text-gray-800 rounded"
        @click="resetExpression"
      >
        重置
      </button>
      <button 
        class="px-3 py-1 bg-blue-500 hover:bg-blue-600 text-white rounded"
        @click="saveExpression"
      >
        确认
      </button>
    </div>

    <div class="mt-4">
      <div class="text-sm font-medium mb-1">条件表达式预览</div>
      <pre class="bg-gray-100 p-2 rounded text-xs overflow-auto max-h-32">{{ JSON.stringify(expression, null, 2) }}</pre>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, watch } from 'vue';
import ExpressionBuilder from './ExpressionBuilder.vue';
import { createEmptyCondition } from './types';
import type { ConditionExpression } from './types';

const props = defineProps<{
  modelValue?: ConditionExpression;
  editorTitle?: string;
}>();

const emit = defineEmits(['update:modelValue', 'save']);

// 本地表达式状态
const expression = ref<ConditionExpression>(
  props.modelValue ? JSON.parse(JSON.stringify(props.modelValue)) : createEmptyCondition()
);

// 监听外部传入的值变化
watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    expression.value = JSON.parse(JSON.stringify(newVal));
  } else {
    expression.value = createEmptyCondition();
  }
}, { deep: true });

// 处理表达式变更
const handleExpressionChange = (newExpression: ConditionExpression) => {
  expression.value = newExpression;
  emit('update:modelValue', newExpression);
};

// 重置表达式
const resetExpression = () => {
  expression.value = createEmptyCondition();
  emit('update:modelValue', expression.value);
};

// 保存表达式
const saveExpression = () => {
  emit('save', expression.value);
};
</script>

<style scoped>
.condition-editor {
  background-color: white;
  border-radius: 0.375rem;
  padding: 1rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.condition-wrapper {
  border: 1px solid #e5e7eb;
  border-radius: 0.375rem;
  padding: 1rem;
  background-color: #f9fafb;
}
</style> 
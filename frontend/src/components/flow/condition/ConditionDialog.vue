<template>
  <div 
    v-if="visible" 
    class="condition-dialog fixed inset-0 flex items-center justify-center z-50"
    @click.self="handleClose"
  >
    <!-- 遮罩层 -->
    <div class="fixed inset-0 bg-black opacity-50"></div>
    
    <!-- 对话框内容 -->
    <div class="condition-dialog-content bg-white rounded-lg shadow-xl relative w-3/4 max-w-4xl max-h-[90vh] overflow-auto p-4 z-10">
      <!-- 对话框标题 -->
      <div class="flex items-center justify-between mb-4">
        <h3 class="text-xl font-semibold">{{ title }}</h3>
        <button 
          class="text-gray-500 hover:text-gray-700"
          @click="handleClose"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>
      
      <!-- 对话框主体 -->
      <div class="condition-dialog-body">
        <condition-editor 
          v-model="localExpression" 
          @save="handleSave"
        />
      </div>
      
      <!-- 对话框底部 -->
      <div class="condition-dialog-footer mt-4 flex justify-end gap-2">
        <button 
          class="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded"
          @click="handleClose"
        >
          取消
        </button>
        <button 
          class="px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded"
          @click="handleSave"
        >
          确定
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, watch } from 'vue';
import ConditionEditor from './ConditionEditor.vue';
import { createEmptyCondition } from './types';
import type { ConditionExpression } from './types';

const props = defineProps<{
  visible: boolean;
  title?: string;
  expression?: ConditionExpression;
}>();

const emit = defineEmits(['update:visible', 'save', 'cancel']);

// 本地表达式状态
const localExpression = ref<ConditionExpression>(
  props.expression ? JSON.parse(JSON.stringify(props.expression)) : createEmptyCondition()
);

// 监听外部传入的表达式变化
watch(() => props.expression, (newVal) => {
  if (newVal) {
    localExpression.value = JSON.parse(JSON.stringify(newVal));
  } else {
    localExpression.value = createEmptyCondition();
  }
}, { deep: true });

// 监听对话框可见性变化
watch(() => props.visible, (newVal) => {
  if (newVal && props.expression) {
    localExpression.value = JSON.parse(JSON.stringify(props.expression));
  }
});

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false);
  emit('cancel');
};

// 保存表达式
const handleSave = () => {
  emit('save', localExpression.value);
  emit('update:visible', false);
};
</script>

<style scoped>
.condition-dialog-content {
  max-height: 90vh;
  overflow-y: auto;
}
</style> 
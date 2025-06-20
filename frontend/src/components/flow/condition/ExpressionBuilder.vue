<template>
  <div class="expression-builder" :class="{ 'nested': level > 0 }">
    <!-- 操作符选择 -->
    <div class="operator-selector mb-2">
      <label class="block text-sm font-medium text-gray-700 mb-1">操作符</label>
      <select 
        class="w-full px-2 py-1 border rounded text-sm"
        v-model="condition.operator"
        @change="handleOperatorChange"
      >
        <optgroup v-for="category in operatorCategories" :key="category.name" :label="category.label">
          <option 
            v-for="op in category.operators" 
            :key="op.value" 
            :value="op.value"
          >
            {{ op.label }} ({{ op.symbol }})
          </option>
        </optgroup>
      </select>
    </div>

    <!-- 操作数 -->
    <div class="operands-container">
      <div 
        v-for="(operand, index) in condition.operands" 
        :key="index" 
        class="operand-item mb-3"
      >
        <div class="flex items-center mb-1">
          <label class="text-sm font-medium text-gray-700">操作数 {{ index + 1 }}</label>
          <div class="flex-grow"></div>
          
          <!-- 对于有两个以上操作数的逻辑操作符，提供删除按钮 -->
          <button 
            v-if="canRemoveOperand(index)" 
            class="text-red-500 hover:text-red-700 text-sm ml-1"
            @click="removeOperand(index)"
          >
            删除
          </button>
        </div>

        <div class="flex items-start gap-2">
          <!-- 递归：如果操作数是嵌套条件 -->
          <template v-if="isNestedCondition(operand)">
            <div class="nested-condition w-full">
              <expression-builder 
                v-model="condition.operands[index]" 
                :level="level + 1"
                @update:model-value="updateOperand(index, $event)"
              />

              <!-- 替换为操作数按钮 -->
              <button 
                class="mt-1 px-2 py-1 bg-gray-100 hover:bg-gray-200 text-xs rounded"
                @click="convertToOperand(index)"
              >
                转换为值
              </button>
            </div>
          </template>

          <!-- 常规操作数 -->
          <template v-else>
            <div class="operand-config w-full">
              <div class="flex gap-2 mb-2">
                <!-- 操作数类型选择 -->
                <select 
                  class="px-2 py-1 border rounded text-sm flex-shrink-0"
                  :value="getOperandType(operand)"
                  @change="updateOperandType(index, operand, $event)"
                >
                  <option value="variable">变量</option>
                  <option value="constant">常量</option>
                  <option value="function">函数</option>
                </select>

                <!-- 数据类型选择 -->
                <select 
                  class="px-2 py-1 border rounded text-sm flex-shrink-0"
                  :value="getOperandDataType(operand)"
                  @change="updateOperandDataType(index, operand, $event)"
                >
                  <option value="string">字符串</option>
                  <option value="number">数字</option>
                  <option value="boolean">布尔值</option>
                  <option value="date">日期</option>
                  <option value="object">对象</option>
                  <option value="array">数组</option>
                </select>
              </div>

              <!-- 操作数值输入 -->
              <div class="value-input">
                <template v-if="getOperandDataType(operand) === 'boolean'">
                  <select
                    class="w-full px-2 py-1 border rounded text-sm"
                    :value="getOperandValue(operand)"
                    @change="updateOperandValue(index, operand, $event)"
                  >
                    <option :value="true">是 (true)</option>
                    <option :value="false">否 (false)</option>
                  </select>
                </template>
                <template v-else-if="getOperandDataType(operand) === 'date'">
                  <input 
                    type="datetime-local" 
                    class="w-full px-2 py-1 border rounded text-sm"
                    :value="getOperandValue(operand)"
                    @change="updateOperandValue(index, operand, $event)"
                  />
                </template>
                <template v-else>
                  <input 
                    :type="getOperandDataType(operand) === 'number' ? 'number' : 'text'" 
                    class="w-full px-2 py-1 border rounded text-sm"
                    :value="getOperandValue(operand)"
                    @input="updateOperandValue(index, operand, $event)"
                    :placeholder="getPlaceholder(operand)"
                  />
                </template>
              </div>

              <!-- 转换为条件按钮 -->
              <button 
                class="mt-1 px-2 py-1 bg-gray-100 hover:bg-gray-200 text-xs rounded"
                @click="convertToCondition(index)"
              >
                转换为条件
              </button>
            </div>
          </template>
        </div>
      </div>

      <!-- 添加操作数按钮（仅对逻辑操作符如AND/OR有效） -->
      <button 
        v-if="canAddOperand" 
        class="px-2 py-1 bg-gray-100 hover:bg-gray-200 text-sm rounded mt-2"
        @click="addOperand"
      >
        添加操作数
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, defineProps, defineEmits } from 'vue';
import { OPERATOR_INFO, createEmptyCondition, createEmptyOperand } from './types';
import type { ConditionExpression, OperandValue, OperatorType } from './types';

const props = defineProps<{
  modelValue: ConditionExpression | OperandValue;
  level: number;
}>();

const emit = defineEmits(['update:modelValue']);

// 本地条件状态
const condition = computed<ConditionExpression>({
  get: () => {
    if (isCondition(props.modelValue)) {
      return props.modelValue as ConditionExpression;
    }
    return createEmptyCondition();
  },
  set: (value) => {
    emit('update:modelValue', value);
  }
});

// 判断一个值是否是条件表达式
function isCondition(value: any): boolean {
  return value && 'operator' in value && 'operands' in value;
}

// 判断一个操作数是否是嵌套条件
function isNestedCondition(operand: any): boolean {
  return isCondition(operand);
}

// 获取操作数类型安全的方法
function getOperandType(operand: ConditionExpression | OperandValue): string {
  if (!isCondition(operand)) {
    return (operand as OperandValue).type;
  }
  return 'variable';
}

// 获取操作数数据类型安全的方法
function getOperandDataType(operand: ConditionExpression | OperandValue): string {
  if (!isCondition(operand)) {
    return (operand as OperandValue).dataType || 'string';
  }
  return 'string';
}

// 获取操作数值安全的方法
function getOperandValue(operand: ConditionExpression | OperandValue): any {
  if (!isCondition(operand)) {
    return (operand as OperandValue).value;
  }
  return '';
}

// 更新操作数类型
function updateOperandType(index: number, operand: ConditionExpression | OperandValue, event: Event) {
  if (!isCondition(operand)) {
    const target = event.target as HTMLSelectElement;
    const updatedOperand = { ...operand as OperandValue, type: target.value as any };
    updateOperand(index, updatedOperand);
  }
}

// 更新操作数数据类型
function updateOperandDataType(index: number, operand: ConditionExpression | OperandValue, event: Event) {
  if (!isCondition(operand)) {
    const target = event.target as HTMLSelectElement;
    const updatedOperand = { ...operand as OperandValue, dataType: target.value as any };
    updateOperand(index, updatedOperand);
  }
}

// 更新操作数值
function updateOperandValue(index: number, operand: ConditionExpression | OperandValue, event: Event) {
  if (!isCondition(operand)) {
    const target = event.target as HTMLInputElement | HTMLSelectElement;
    let value: any = target.value;
    
    // 处理特殊类型的值
    if ((operand as OperandValue).dataType === 'number') {
      value = Number(value);
    } else if ((operand as OperandValue).dataType === 'boolean') {
      value = value === 'true';
    }
    
    const updatedOperand = { ...operand as OperandValue, value };
    updateOperand(index, updatedOperand);
  }
}

// 获取操作符分类
const operatorCategories = computed(() => {
  return [
    {
      name: 'comparison',
      label: '比较操作符',
      operators: Object.values(OPERATOR_INFO).filter(op => op.category === 'comparison')
    },
    {
      name: 'logical',
      label: '逻辑操作符',
      operators: Object.values(OPERATOR_INFO).filter(op => op.category === 'logical')
    },
    {
      name: 'string',
      label: '字符串操作符',
      operators: Object.values(OPERATOR_INFO).filter(op => op.category === 'string')
    },
    {
      name: 'collection',
      label: '集合操作符',
      operators: Object.values(OPERATOR_INFO).filter(op => op.category === 'collection')
    },
    {
      name: 'null',
      label: '空值操作符',
      operators: Object.values(OPERATOR_INFO).filter(op => op.category === 'null')
    }
  ];
});

// 当前操作符信息
const currentOperatorInfo = computed(() => {
  return OPERATOR_INFO[condition.value.operator as OperatorType];
});

// 是否可以添加更多操作数
const canAddOperand = computed(() => {
  return ['and', 'or'].includes(condition.value.operator);
});

// 处理操作符变更
const handleOperatorChange = () => {
  const operatorInfo = currentOperatorInfo.value;
  
  // 调整操作数数量以匹配操作符要求
  if (operatorInfo) {
    const requiredCount = operatorInfo.operandCount;
    const currentCount = condition.value.operands.length;
    
    if (currentCount < requiredCount) {
      // 添加缺少的操作数
      for (let i = currentCount; i < requiredCount; i++) {
        condition.value.operands.push(createEmptyOperand());
      }
    } else if (currentCount > requiredCount) {
      // 移除多余的操作数
      condition.value.operands = condition.value.operands.slice(0, requiredCount);
    }
    
    emit('update:modelValue', condition.value);
  }
};

// 更新操作数
const updateOperand = (index: number, value: ConditionExpression | OperandValue) => {
  condition.value.operands[index] = value;
  emit('update:modelValue', condition.value);
};

// 添加新操作数
const addOperand = () => {
  condition.value.operands.push(createEmptyOperand());
  emit('update:modelValue', condition.value);
};

// 删除操作数
const removeOperand = (index: number) => {
  condition.value.operands.splice(index, 1);
  emit('update:modelValue', condition.value);
};

// 判断是否可以删除操作数
const canRemoveOperand = (index: number) => {
  // 对于逻辑操作符AND/OR，只要保留至少2个操作数就可以删除其他的
  if (['and', 'or'].includes(condition.value.operator)) {
    return condition.value.operands.length > 2;
  }
  return false;
};

// 将操作数转换为条件
const convertToCondition = (index: number) => {
  condition.value.operands[index] = createEmptyCondition();
  emit('update:modelValue', condition.value);
};

// 将条件转换为操作数
const convertToOperand = (index: number) => {
  condition.value.operands[index] = createEmptyOperand();
  emit('update:modelValue', condition.value);
};

// 获取输入框占位符
const getPlaceholder = (operand: ConditionExpression | OperandValue): string => {
  if (!isCondition(operand)) {
    switch ((operand as OperandValue).type) {
      case 'variable':
        return '变量名...';
      case 'constant':
        return '常量值...';
      case 'function':
        return '函数名...';
      default:
        return '';
    }
  }
  return '';
};
</script>

<style scoped>
.expression-builder {
  width: 100%;
}

.nested {
  border-left: 2px solid #3b82f6;
  padding-left: 1rem;
  margin-left: 0.5rem;
}

.operand-item {
  padding: 0.5rem;
  border: 1px dashed #e5e7eb;
  border-radius: 0.25rem;
}
</style> 
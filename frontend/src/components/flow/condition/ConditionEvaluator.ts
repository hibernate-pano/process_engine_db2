/**
 * 条件表达式求值器
 * 用于计算条件表达式的结果
 */
import type { ConditionExpression, OperandValue, OperatorType } from './types';

/**
 * 求值上下文
 * 包含变量值和函数
 */
export interface EvaluationContext {
  variables: Record<string, any>;
  functions?: Record<string, Function>;
}

/**
 * 求值操作符处理函数
 */
export type OperatorHandler = (
  operands: any[],
  context: EvaluationContext
) => any;

/**
 * 操作符处理函数映射
 */
const operatorHandlers: Record<OperatorType, OperatorHandler> = {
  // 比较操作符
  eq: (operands, context) => {
    const [left, right] = operands;
    return left === right;
  },
  neq: (operands, context) => {
    const [left, right] = operands;
    return left !== right;
  },
  gt: (operands, context) => {
    const [left, right] = operands;
    return left > right;
  },
  lt: (operands, context) => {
    const [left, right] = operands;
    return left < right;
  },
  gte: (operands, context) => {
    const [left, right] = operands;
    return left >= right;
  },
  lte: (operands, context) => {
    const [left, right] = operands;
    return left <= right;
  },

  // 逻辑操作符
  and: (operands, context) => {
    return operands.every(Boolean);
  },
  or: (operands, context) => {
    return operands.some(Boolean);
  },
  not: (operands, context) => {
    return !operands[0];
  },

  // 字符串操作符
  contains: (operands, context) => {
    const [str, substr] = operands;
    if (typeof str !== 'string' || typeof substr !== 'string') {
      return false;
    }
    return str.includes(substr);
  },
  startsWith: (operands, context) => {
    const [str, prefix] = operands;
    if (typeof str !== 'string' || typeof prefix !== 'string') {
      return false;
    }
    return str.startsWith(prefix);
  },
  endsWith: (operands, context) => {
    const [str, suffix] = operands;
    if (typeof str !== 'string' || typeof suffix !== 'string') {
      return false;
    }
    return str.endsWith(suffix);
  },

  // 集合操作符
  in: (operands, context) => {
    const [item, collection] = operands;
    if (!Array.isArray(collection)) {
      return false;
    }
    return collection.includes(item);
  },
  notIn: (operands, context) => {
    const [item, collection] = operands;
    if (!Array.isArray(collection)) {
      return false;
    }
    return !collection.includes(item);
  },

  // 空值操作符
  isNull: (operands, context) => {
    return operands[0] === null || operands[0] === undefined;
  },
  isNotNull: (operands, context) => {
    return operands[0] !== null && operands[0] !== undefined;
  },
};

/**
 * 计算操作数的值
 * @param operand 操作数
 * @param context 上下文
 * @returns 操作数的值
 */
export function evaluateOperand(
  operand: OperandValue,
  context: EvaluationContext
): any {
  switch (operand.type) {
    case 'variable':
      return context.variables[operand.value] !== undefined
        ? context.variables[operand.value]
        : null;

    case 'constant':
      return operand.value;

    case 'function':
      if (!context.functions || !context.functions[operand.value]) {
        console.error(`函数 "${operand.value}" 未定义`);
        return null;
      }
      try {
        return context.functions[operand.value]();
      } catch (e) {
        console.error(`执行函数 "${operand.value}" 时出错:`, e);
        return null;
      }

    default:
      return null;
  }
}

/**
 * 计算条件表达式
 * @param expression 条件表达式
 * @param context 上下文
 * @returns 条件表达式的值
 */
export function evaluateCondition(
  expression: ConditionExpression,
  context: EvaluationContext = { variables: {} }
): boolean {
  try {
    // 获取操作符处理函数
    const handler = operatorHandlers[expression.operator];
    if (!handler) {
      console.error(`不支持的操作符: ${expression.operator}`);
      return false;
    }

    // 计算操作数的值
    const operandValues = expression.operands.map((operand) => {
      if ('operator' in operand) {
        // 递归计算嵌套条件
        return evaluateCondition(operand as ConditionExpression, context);
      } else {
        // 计算操作数的值
        return evaluateOperand(operand as OperandValue, context);
      }
    });

    // 调用操作符处理函数
    return !!handler(operandValues, context);
  } catch (e) {
    console.error('计算条件表达式时出错:', e);
    return false;
  }
}

/**
 * 判断条件表达式是否有效
 * @param expression 条件表达式
 * @returns 是否有效
 */
export function isValidCondition(expression: ConditionExpression): boolean {
  try {
    // 检查操作符
    if (!expression.operator || !(expression.operator in operatorHandlers)) {
      return false;
    }

    // 检查操作数数组
    if (!Array.isArray(expression.operands)) {
      return false;
    }

    // 检查操作数数量
    const operandCount = expression.operands.length;
    if (operandCount === 0) {
      return false;
    }

    // 对于单操作数的操作符
    if (['not', 'isNull', 'isNotNull'].includes(expression.operator) && operandCount !== 1) {
      return false;
    }

    // 对于双操作数的操作符
    if (!['and', 'or', 'not', 'isNull', 'isNotNull'].includes(expression.operator) && operandCount !== 2) {
      return false;
    }

    // 递归检查嵌套条件
    for (const operand of expression.operands) {
      if ('operator' in operand) {
        if (!isValidCondition(operand as ConditionExpression)) {
          return false;
        }
      } else if ('type' in operand) {
        // 检查操作数类型
        const opVal = operand as OperandValue;
        if (!['variable', 'constant', 'function'].includes(opVal.type)) {
          return false;
        }
      } else {
        return false;
      }
    }

    return true;
  } catch (e) {
    console.error('验证条件表达式时出错:', e);
    return false;
  }
}

/**
 * 将条件表达式转换为可读文本
 * @param expression 条件表达式
 * @returns 可读文本
 */
export function conditionToText(expression: ConditionExpression): string {
  try {
    switch (expression.operator) {
      // 逻辑操作符
      case 'and':
        return expression.operands
          .map((op) => {
            const text = 'operator' in op 
              ? `(${conditionToText(op as ConditionExpression)})` 
              : operandToText(op as OperandValue);
            return text;
          })
          .join(' 且 ');

      case 'or':
        return expression.operands
          .map((op) => {
            const text = 'operator' in op 
              ? `(${conditionToText(op as ConditionExpression)})` 
              : operandToText(op as OperandValue);
            return text;
          })
          .join(' 或 ');

      case 'not':
        const operand = expression.operands[0];
        const operandText = 'operator' in operand 
          ? `(${conditionToText(operand as ConditionExpression)})` 
          : operandToText(operand as OperandValue);
        return `非 ${operandText}`;

      // 比较操作符
      case 'eq':
        return `${operandToText(expression.operands[0] as OperandValue)} 等于 ${operandToText(expression.operands[1] as OperandValue)}`;
      case 'neq':
        return `${operandToText(expression.operands[0] as OperandValue)} 不等于 ${operandToText(expression.operands[1] as OperandValue)}`;
      case 'gt':
        return `${operandToText(expression.operands[0] as OperandValue)} 大于 ${operandToText(expression.operands[1] as OperandValue)}`;
      case 'lt':
        return `${operandToText(expression.operands[0] as OperandValue)} 小于 ${operandToText(expression.operands[1] as OperandValue)}`;
      case 'gte':
        return `${operandToText(expression.operands[0] as OperandValue)} 大于等于 ${operandToText(expression.operands[1] as OperandValue)}`;
      case 'lte':
        return `${operandToText(expression.operands[0] as OperandValue)} 小于等于 ${operandToText(expression.operands[1] as OperandValue)}`;

      // 字符串操作符
      case 'contains':
        return `${operandToText(expression.operands[0] as OperandValue)} 包含 ${operandToText(expression.operands[1] as OperandValue)}`;
      case 'startsWith':
        return `${operandToText(expression.operands[0] as OperandValue)} 以 ${operandToText(expression.operands[1] as OperandValue)} 开始`;
      case 'endsWith':
        return `${operandToText(expression.operands[0] as OperandValue)} 以 ${operandToText(expression.operands[1] as OperandValue)} 结束`;

      // 集合操作符
      case 'in':
        return `${operandToText(expression.operands[0] as OperandValue)} 在 ${operandToText(expression.operands[1] as OperandValue)} 中`;
      case 'notIn':
        return `${operandToText(expression.operands[0] as OperandValue)} 不在 ${operandToText(expression.operands[1] as OperandValue)} 中`;

      // 空值操作符
      case 'isNull':
        return `${operandToText(expression.operands[0] as OperandValue)} 为空`;
      case 'isNotNull':
        return `${operandToText(expression.operands[0] as OperandValue)} 不为空`;

      default:
        return '无效的表达式';
    }
  } catch (e) {
    console.error('转换条件表达式为文本时出错:', e);
    return '无效的表达式';
  }
}

/**
 * 将操作数转换为可读文本
 * @param operand 操作数
 * @returns 可读文本
 */
function operandToText(operand: OperandValue): string {
  switch (operand.type) {
    case 'variable':
      return `变量[${operand.value}]`;
    case 'constant':
      if (operand.dataType === 'string') {
        return `"${operand.value}"`;
      }
      return `${operand.value}`;
    case 'function':
      return `函数[${operand.value}]`;
    default:
      return '未知操作数';
  }
} 
/**
 * 条件表达式类型定义
 */

/**
 * 支持的操作符类型
 */
export type OperatorType = 
  // 比较操作符
  | 'eq'         // 等于
  | 'neq'        // 不等于
  | 'gt'         // 大于
  | 'lt'         // 小于
  | 'gte'        // 大于等于
  | 'lte'        // 小于等于
  // 逻辑操作符
  | 'and'        // 与
  | 'or'         // 或
  | 'not'        // 非
  // 字符串操作符
  | 'contains'    // 包含
  | 'startsWith'  // 以...开始
  | 'endsWith'    // 以...结束
  // 集合操作符
  | 'in'          // 在集合中
  | 'notIn'       // 不在集合中
  // 空值操作符
  | 'isNull'      // 为空
  | 'isNotNull';  // 不为空

/**
 * 操作数类型
 */
export type OperandType = 'variable' | 'constant' | 'function';

/**
 * 数据类型
 */
export type DataType = 'string' | 'number' | 'boolean' | 'date' | 'object' | 'array';

/**
 * 操作数值接口
 */
export interface OperandValue {
  type: OperandType;
  value: any;
  dataType?: DataType;
}

/**
 * 条件表达式接口
 */
export interface ConditionExpression {
  operator: OperatorType;
  operands: (ConditionExpression | OperandValue)[];
}

/**
 * 操作符显示信息
 */
export interface OperatorInfo {
  value: OperatorType;
  label: string;
  symbol: string;
  operandCount: number;
  category: 'comparison' | 'logical' | 'string' | 'collection' | 'null';
}

/**
 * 操作符信息映射
 */
export const OPERATOR_INFO: Record<OperatorType, OperatorInfo> = {
  // 比较操作符
  eq: { value: 'eq', label: '等于', symbol: '=', operandCount: 2, category: 'comparison' },
  neq: { value: 'neq', label: '不等于', symbol: '≠', operandCount: 2, category: 'comparison' },
  gt: { value: 'gt', label: '大于', symbol: '>', operandCount: 2, category: 'comparison' },
  lt: { value: 'lt', label: '小于', symbol: '<', operandCount: 2, category: 'comparison' },
  gte: { value: 'gte', label: '大于等于', symbol: '≥', operandCount: 2, category: 'comparison' },
  lte: { value: 'lte', label: '小于等于', symbol: '≤', operandCount: 2, category: 'comparison' },

  // 逻辑操作符
  and: { value: 'and', label: '与', symbol: 'AND', operandCount: 2, category: 'logical' },
  or: { value: 'or', label: '或', symbol: 'OR', operandCount: 2, category: 'logical' },
  not: { value: 'not', label: '非', symbol: 'NOT', operandCount: 1, category: 'logical' },

  // 字符串操作符
  contains: { value: 'contains', label: '包含', symbol: 'contains', operandCount: 2, category: 'string' },
  startsWith: { value: 'startsWith', label: '以...开始', symbol: 'starts with', operandCount: 2, category: 'string' },
  endsWith: { value: 'endsWith', label: '以...结束', symbol: 'ends with', operandCount: 2, category: 'string' },

  // 集合操作符
  in: { value: 'in', label: '在集合中', symbol: 'in', operandCount: 2, category: 'collection' },
  notIn: { value: 'notIn', label: '不在集合中', symbol: 'not in', operandCount: 2, category: 'collection' },

  // 空值操作符
  isNull: { value: 'isNull', label: '为空', symbol: 'is null', operandCount: 1, category: 'null' },
  isNotNull: { value: 'isNotNull', label: '不为空', symbol: 'is not null', operandCount: 1, category: 'null' }
};

/**
 * 创建一个空的条件表达式
 */
export function createEmptyCondition(): ConditionExpression {
  return {
    operator: 'eq',
    operands: [
      { type: 'variable', value: '', dataType: 'string' },
      { type: 'constant', value: '', dataType: 'string' }
    ]
  };
}

/**
 * 创建一个空的操作数值
 */
export function createEmptyOperand(type: OperandType = 'variable'): OperandValue {
  return {
    type,
    value: '',
    dataType: 'string'
  };
} 
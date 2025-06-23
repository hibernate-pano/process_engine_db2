// 类型声明
declare module 'uuid' {
  export function v1(): string;
  export function v3(name: string | Buffer, namespace: string | Buffer): string;
  export function v4(): string;
  export function v5(name: string | Buffer, namespace: string | Buffer): string;
} 
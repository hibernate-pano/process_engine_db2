declare module '@vue-flow/core' {
  export const VueFlow: any;
  export function useVueFlow(): any;
  export type Position = 'top' | 'right' | 'bottom' | 'left';
  export interface NodeDragEvent {
    node: any;
    event: MouseEvent;
  }
  export const BaseEdge: any;
  export const EdgeLabelRenderer: any;
  export function getSmoothStepPath(params: {
    sourceX: number;
    sourceY: number;
    sourcePosition?: Position;
    targetX: number;
    targetY: number;
    targetPosition?: Position;
    borderRadius?: number;
  }): string;
  
  // 添加连线相关类型
  export enum ConnectionMode {
    Strict = 'strict',
    Loose = 'loose'
  }
  
  export interface Connection {
    source: string;
    target: string;
    sourceHandle?: string | null;
    targetHandle?: string | null;
  }
  
  export interface Edge {
    id: string;
    source: string;
    target: string;
    sourceHandle?: string | null;
    targetHandle?: string | null;
    type?: string;
    data?: any;
    style?: any;
    animated?: boolean;
    selected?: boolean;
    zIndex?: number;
  }
  
  export interface Handle {
    id?: string | null;
    nodeId: string;
    position: Position;
    type: 'source' | 'target';
  }
}

declare module '@vue-flow/background' {
  export const Background: any;
}

declare module '@vue-flow/controls' {
  export const Controls: any;
}

declare module '@vue-flow/minimap' {
  export const MiniMap: any;
}

declare module '*.vue' {
  import { DefineComponent } from 'vue';
  const component: DefineComponent<{}, {}, any>;
  export default component;
} 
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
import { defineStore } from 'pinia'
import { flowDefinitionApi, flowVersionApi, type FlowDefinition, type FlowVersion } from '../api'

export const useFlowStore = defineStore('flow', {
  state: () => ({
    flowDefinitions: [] as FlowDefinition[],
    currentFlowDefinition: null as FlowDefinition | null,
    flowVersions: [] as FlowVersion[],
    currentFlowVersion: null as FlowVersion | null,
    loading: false,
    error: null as string | null
  }),
  
  actions: {
    // 获取流程定义列表
    async fetchFlowDefinitions(params = {}) {
      this.loading = true
      try {
        const data = await flowDefinitionApi.list(params)
        this.flowDefinitions = data.content || []
        this.error = null
      } catch (error: any) {
        this.error = error.message || '加载流程定义失败'
        console.error('获取流程定义列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    // 获取流程定义详情
    async fetchFlowDefinition(id: number) {
      this.loading = true
      try {
        const data = await flowDefinitionApi.get(id)
        this.currentFlowDefinition = data
        this.error = null
        return data
      } catch (error: any) {
        this.error = error.message || '加载流程定义详情失败'
        console.error('获取流程定义详情失败:', error)
        return null
      } finally {
        this.loading = false
      }
    },
    
    // 获取流程版本列表
    async fetchFlowVersions(flowDefinitionId: number, params = {}) {
      this.loading = true
      try {
        const data = await flowVersionApi.list(flowDefinitionId, params)
        this.flowVersions = data.content || []
        this.error = null
      } catch (error: any) {
        this.error = error.message || '加载流程版本列表失败'
        console.error('获取流程版本列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    // 获取流程版本详情
    async fetchFlowVersion(id: number) {
      this.loading = true
      try {
        const data = await flowVersionApi.get(id)
        this.currentFlowVersion = data
        this.error = null
        return data
      } catch (error: any) {
        this.error = error.message || '加载流程版本详情失败'
        console.error('获取流程版本详情失败:', error)
        return null
      } finally {
        this.loading = false
      }
    },
    
    // 创建流程定义
    async createFlowDefinition(flowDefinition: Partial<FlowDefinition>) {
      this.loading = true
      try {
        const data = await flowDefinitionApi.create(flowDefinition)
        this.error = null
        return data
      } catch (error: any) {
        this.error = error.message || '创建流程定义失败'
        console.error('创建流程定义失败:', error)
        return null
      } finally {
        this.loading = false
      }
    },
    
    // 创建流程版本
    async createFlowVersion(flowVersion: Partial<FlowVersion>) {
      this.loading = true
      try {
        const data = await flowVersionApi.create(flowVersion)
        this.error = null
        return data
      } catch (error: any) {
        this.error = error.message || '创建流程版本失败'
        console.error('创建流程版本失败:', error)
        return null
      } finally {
        this.loading = false
      }
    }
  }
})
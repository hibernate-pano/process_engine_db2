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
    },
    
    // 设置当前版本
    async setFlowVersionAsCurrent(versionId: number) {
      this.loading = true
      try {
        // 调用API设置当前版本，这里假设API存在
        await flowVersionApi.setAsCurrent(versionId)
        this.error = null
        return true
      } catch (error: any) {
        this.error = error.message || '设置当前版本失败'
        console.error('设置当前版本失败:', error)
        return false
      } finally {
        this.loading = false
      }
    },
    
    // 回滚到指定版本
    async rollbackToVersion(versionId: number, description: string = '回滚版本') {
      this.loading = true
      try {
        // 调用API回滚版本，这里假设API存在
        await flowVersionApi.rollback(versionId, description)
        this.error = null
        return true
      } catch (error: any) {
        this.error = error.message || '回滚版本失败'
        console.error('回滚版本失败:', error)
        return false
      } finally {
        this.loading = false
      }
    }
  }
})
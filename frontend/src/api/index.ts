import axios from 'axios'
import type { AxiosResponse } from 'axios'

// API响应格式
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 分页响应格式
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
  empty: boolean
}

// 流程定义接口
export interface FlowDefinition {
  id: number
  name: string
  description: string
  type: string
  tags: string
  currentVersion: number
  status: string
}

// 流程版本接口
export interface FlowVersion {
  id: number
  flowDefinitionId: number
  version: number
  description: string
  flowData: string
  flowGraph: string
  status: string
  type: string
  publishTime: string
  publishBy: string
  isCurrent: boolean
}

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加认证信息等
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response: AxiosResponse<ApiResponse<any>>) => {
    // 处理响应数据
    const res = response.data
    if (res.code !== 200) {
      console.error('API错误:', res.message || '未知错误')
      return Promise.reject(new Error(res.message || '未知错误'))
    }
    return res.data
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

export default api

// 流程定义API
export const flowDefinitionApi = {
  // 获取流程定义列表
  list: (params: any): Promise<PageResponse<FlowDefinition>> => 
    api.get('/flow/definitions', { params }),
  
  // 获取流程定义详情
  get: (id: number): Promise<FlowDefinition> => 
    api.get(`/flow/definitions/${id}`),
  
  // 创建流程定义
  create: (data: Partial<FlowDefinition>): Promise<FlowDefinition> => 
    api.post('/flow/definitions', data),
  
  // 更新流程定义
  update: (id: number, data: Partial<FlowDefinition>): Promise<FlowDefinition> => 
    api.put(`/flow/definitions/${id}`, data),
  
  // 删除流程定义
  delete: (id: number): Promise<void> => 
    api.delete(`/flow/definitions/${id}`),
  
  // 更新流程定义状态
  updateStatus: (id: number, status: string): Promise<FlowDefinition> => 
    api.put(`/flow/definitions/${id}/status`, null, { params: { status } })
}

// 流程版本API
export const flowVersionApi = {
  // 获取流程版本列表
  list: (flowDefinitionId: number, params: any): Promise<PageResponse<FlowVersion>> => 
    api.get(`/flow/versions/by-definition/${flowDefinitionId}/page`, { params }),
  
  // 获取流程版本详情
  get: (id: number): Promise<FlowVersion> => 
    api.get(`/flow/versions/${id}`),
  
  // 创建流程版本
  create: (data: Partial<FlowVersion>): Promise<FlowVersion> => 
    api.post('/flow/versions', data),
  
  // 创建新版本
  createNewVersion: (flowDefinitionId: number, data: Partial<FlowVersion>): Promise<FlowVersion> => 
    api.post(`/flow/versions/by-definition/${flowDefinitionId}/new-version`, data),
  
  // 发布流程版本
  publish: (id: number): Promise<FlowVersion> => 
    api.put(`/flow/versions/${id}/publish`),
  
  // 设置为当前版本
  setAsCurrent: (id: number): Promise<FlowVersion> => 
    api.put(`/flow/versions/${id}/set-as-current`)
}
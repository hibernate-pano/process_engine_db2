<template>
  <div class="history-panel bg-white p-4 h-full overflow-y-auto">
    <h3 class="text-lg font-bold mb-4">历史版本</h3>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="py-4 text-center">
      <div class="inline-block animate-spin rounded-full h-6 w-6 border-t-2 border-gray-900 border-r-2"></div>
      <p class="mt-2 text-gray-500">加载中...</p>
    </div>
    
    <!-- 错误提示 -->
    <div v-else-if="error" class="py-4 text-center text-red-500">
      <p>{{ error }}</p>
      <button @click="loadVersions" class="mt-2 px-3 py-1 bg-blue-500 text-white rounded text-sm">
        重试
      </button>
    </div>
    
    <!-- 空数据提示 -->
    <div v-else-if="versions.length === 0" class="py-4 text-center text-gray-500">
      <p>暂无历史版本</p>
    </div>
    
    <!-- 版本列表 -->
    <div v-else class="mb-4">
      <div class="flex items-center justify-between mb-2">
        <h4 class="text-sm font-medium text-gray-700">版本列表</h4>
        <div class="flex gap-2">
          <button 
            v-if="selectedVersions.length === 2"
            @click="compareSelectedVersions"
            class="px-2 py-1 bg-blue-100 text-blue-700 rounded text-xs hover:bg-blue-200"
          >
            对比
          </button>
          <button 
            v-if="selectedVersions.length === 1"
            @click="rollbackSelectedVersion"
            class="px-2 py-1 bg-orange-100 text-orange-700 rounded text-xs hover:bg-orange-200"
          >
            回滚
          </button>
        </div>
      </div>
      
      <div class="border rounded overflow-hidden">
        <table class="min-w-full">
          <thead class="bg-gray-50 border-b">
            <tr>
              <th class="text-left py-2 px-3 text-xs font-medium text-gray-500 w-10">选择</th>
              <th class="text-left py-2 px-3 text-xs font-medium text-gray-500">版本</th>
              <th class="text-left py-2 px-3 text-xs font-medium text-gray-500">发布时间</th>
              <th class="text-left py-2 px-3 text-xs font-medium text-gray-500">状态</th>
              <th class="text-left py-2 px-3 text-xs font-medium text-gray-500">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y">
            <tr 
              v-for="version in versions" 
              :key="version.id"
              :class="{'bg-blue-50': isSelected(version.id)}"
              class="hover:bg-gray-50"
            >
              <td class="py-2 px-3">
                <input 
                  type="checkbox" 
                  :value="version.id" 
                  v-model="selectedVersionIds"
                  :disabled="selectedVersionIds.length >= 2 && !selectedVersionIds.includes(version.id)"
                  class="h-4 w-4 rounded border-gray-300"
                />
              </td>
              <td class="py-2 px-3 text-sm">
                <div class="font-medium">v{{ version.version }}</div>
                <div v-if="version.isCurrent" class="text-xs text-green-600 mt-1">当前版本</div>
              </td>
              <td class="py-2 px-3 text-sm">
                {{ formatDate(version.publishTime) }}
              </td>
              <td class="py-2 px-3">
                <span 
                  class="inline-flex px-2 py-1 text-xs rounded-full"
                  :class="{
                    'bg-green-100 text-green-800': version.status === 'PUBLISHED',
                    'bg-yellow-100 text-yellow-800': version.status === 'DRAFT',
                    'bg-gray-100 text-gray-800': version.status !== 'PUBLISHED' && version.status !== 'DRAFT'
                  }"
                >
                  {{ getStatusText(version.status) }}
                </span>
              </td>
              <td class="py-2 px-3">
                <div class="flex space-x-1">
                  <button 
                    @click="viewVersion(version)" 
                    class="text-xs bg-blue-100 text-blue-700 px-2 py-1 rounded hover:bg-blue-200"
                  >
                    查看
                  </button>
                  <button 
                    v-if="!version.isCurrent" 
                    @click="setAsCurrent(version)" 
                    class="text-xs bg-green-100 text-green-700 px-2 py-1 rounded hover:bg-green-200"
                  >
                    设为当前
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <!-- 版本详情 -->
    <div v-if="selectedVersion" class="mb-4">
      <div class="flex justify-between items-center mb-2">
        <h4 class="text-sm font-medium text-gray-700">版本详情</h4>
        <div class="flex space-x-1">
          <button 
            v-if="!selectedVersion.isCurrent" 
            @click="setAsCurrent(selectedVersion)" 
            class="text-xs bg-green-100 text-green-700 px-2 py-1 rounded hover:bg-green-200"
          >
            设为当前
          </button>
          <button 
            v-if="!selectedVersion.isCurrent"
            @click="rollbackSelectedVersion()" 
            class="text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded hover:bg-orange-200"
          >
            回滚到此版本
          </button>
        </div>
      </div>
      
      <div class="bg-gray-50 p-3 rounded">
        <div class="flex items-center mb-3">
          <div class="text-lg font-bold mr-2">v{{ selectedVersion.version }}</div>
          <div 
            v-if="selectedVersion.isCurrent" 
            class="px-2 py-1 text-xs rounded-full bg-green-100 text-green-800"
          >
            当前版本
          </div>
        </div>
        
        <div class="grid grid-cols-2 gap-y-2 text-sm">
          <div class="text-gray-500">版本ID:</div>
          <div>{{ selectedVersion.id }}</div>
          
          <div class="text-gray-500">发布时间:</div>
          <div>{{ formatDate(selectedVersion.publishTime) }}</div>
          
          <div class="text-gray-500">发布人:</div>
          <div>{{ selectedVersion.publishBy || '未知' }}</div>
          
          <div class="text-gray-500">状态:</div>
          <div>
            <span 
              class="inline-flex px-2 py-1 text-xs rounded-full"
              :class="{
                'bg-green-100 text-green-800': selectedVersion.status === 'PUBLISHED',
                'bg-yellow-100 text-yellow-800': selectedVersion.status === 'DRAFT',
                'bg-gray-100 text-gray-800': selectedVersion.status !== 'PUBLISHED' && selectedVersion.status !== 'DRAFT'
              }"
            >
              {{ getStatusText(selectedVersion.status) }}
            </span>
          </div>
          
          <div class="text-gray-500">类型:</div>
          <div>{{ selectedVersion.type || '未知' }}</div>
          
          <div class="text-gray-500">所属流程:</div>
          <div>ID: {{ selectedVersion.flowDefinitionId }}</div>
        </div>
        
        <div class="mt-3">
          <div class="text-gray-500 text-sm">描述:</div>
          <div class="mt-1 text-sm p-2 bg-white border rounded">
            {{ selectedVersion.description || '无描述信息' }}
          </div>
        </div>
        
        <div class="mt-3">
          <div class="text-gray-500 text-sm mb-1">版本信息:</div>
          <div class="flex flex-wrap gap-2">
            <div v-if="isLatestVersion(selectedVersion)" class="text-xs px-2 py-1 bg-blue-100 text-blue-800 rounded-full">
              最新版本
            </div>
            <div v-if="selectedVersion.isCurrent" class="text-xs px-2 py-1 bg-green-100 text-green-800 rounded-full">
              当前使用版本
            </div>
            <div v-if="isFromRollback(selectedVersion)" class="text-xs px-2 py-1 bg-orange-100 text-orange-800 rounded-full">
              从回滚创建
            </div>
          </div>
        </div>
        
        <div v-if="!selectedVersion.isCurrent" class="mt-4 text-center">
          <button 
            @click="previewVersion(selectedVersion)" 
            class="w-full px-3 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          >
            预览此版本
          </button>
        </div>
      </div>
    </div>
    
    <!-- 版本对比弹窗 -->
    <div 
      v-if="showCompareModal" 
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-xl w-4/5 h-4/5 flex flex-col">
        <div class="px-4 py-3 border-b flex justify-between items-center">
          <h3 class="text-lg font-bold">版本对比</h3>
          <button @click="showCompareModal = false" class="text-gray-500 hover:text-gray-700">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div class="flex-1 overflow-auto p-4">
          <div class="grid grid-cols-2 gap-4">
            <div class="border rounded overflow-hidden">
              <div class="bg-gray-100 px-3 py-2 font-medium text-sm border-b">
                版本 v{{ compareVersions[0]?.version || '' }}
                <span class="text-xs text-gray-500 ml-2">
                  {{ formatDate(compareVersions[0]?.publishTime || '') }}
                </span>
              </div>
              <div class="p-3 overflow-auto h-full max-h-[calc(100vh-300px)]">
                <pre class="text-xs whitespace-pre-wrap">{{ formatFlowData(compareVersions[0]?.flowGraph || '') }}</pre>
              </div>
            </div>
            
            <div class="border rounded overflow-hidden">
              <div class="bg-gray-100 px-3 py-2 font-medium text-sm border-b">
                版本 v{{ compareVersions[1]?.version || '' }}
                <span class="text-xs text-gray-500 ml-2">
                  {{ formatDate(compareVersions[1]?.publishTime || '') }}
                </span>
              </div>
              <div class="p-3 overflow-auto h-full max-h-[calc(100vh-300px)]">
                <pre class="text-xs whitespace-pre-wrap">{{ formatFlowData(compareVersions[1]?.flowGraph || '') }}</pre>
              </div>
            </div>
          </div>
          
          <!-- 差异视图 -->
          <div class="mt-4 border rounded overflow-hidden">
            <div class="bg-gray-100 px-3 py-2 font-medium text-sm border-b">
              版本差异对比
            </div>
            <div class="p-3 overflow-auto h-full max-h-[calc(100vh-300px)]">
              <div v-html="diffResult" class="text-xs font-mono"></div>
            </div>
          </div>
        </div>
        
        <div class="px-4 py-3 border-t flex justify-end">
          <button 
            @click="showCompareModal = false" 
            class="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300"
          >
            关闭
          </button>
        </div>
      </div>
    </div>
    
    <!-- 回滚确认弹窗 -->
    <div 
      v-if="showRollbackModal" 
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-xl w-96 flex flex-col">
        <div class="px-4 py-3 border-b flex justify-between items-center">
          <h3 class="text-lg font-bold">确认回滚</h3>
          <button @click="showRollbackModal = false" class="text-gray-500 hover:text-gray-700">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div class="p-4">
          <p class="text-sm text-gray-700">
            你确定要回滚到版本 <span class="font-medium">v{{ rollbackVersion?.version }}</span> 吗？
            此操作将创建一个新版本，内容与所选版本相同。
          </p>
        </div>
        
        <div class="px-4 py-3 border-t flex justify-end space-x-2">
          <button 
            @click="showRollbackModal = false" 
            class="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300"
          >
            取消
          </button>
          <button 
            @click="confirmRollback" 
            class="px-4 py-2 bg-orange-500 text-white rounded hover:bg-orange-600"
          >
            确认回滚
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useFlowStore } from '../../stores/flowStore'
import type { FlowVersion } from '../../api'
import * as diffLib from 'diff'

const props = defineProps<{
  flowDefinitionId?: number
}>()

const emit = defineEmits<{
  (e: 'view', version: FlowVersion): void
  (e: 'setAsCurrent', version: FlowVersion): void
  (e: 'rollback', version: FlowVersion): void
}>()

const flowStore = useFlowStore()

// 状态
const loading = ref(false)
const error = ref<string | null>(null)
const versions = ref<FlowVersion[]>([])
const selectedVersionIds = ref<number[]>([])
const selectedVersion = ref<FlowVersion | null>(null)
const showCompareModal = ref(false)
const compareVersions = ref<FlowVersion[]>([])
const showRollbackModal = ref(false)
const rollbackVersion = ref<FlowVersion | null>(null)

// 计算属性
const selectedVersions = computed(() => {
  return selectedVersionIds.value.map(id => 
    versions.value.find(v => v.id === id)
  ).filter(Boolean) as FlowVersion[]
})

// 检查版本是否被选中
function isSelected(versionId: number): boolean {
  return selectedVersionIds.value.includes(versionId)
}

// 格式化日期
function formatDate(dateStr?: string): string {
  if (!dateStr) return '未知'
  
  try {
    const date = new Date(dateStr)
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return dateStr
  }
}

// 获取状态文本
function getStatusText(status?: string): string {
  if (!status) return '未知'
  
  const statusMap: Record<string, string> = {
    'DRAFT': '草稿',
    'PUBLISHED': '已发布',
    'DEPRECATED': '已废弃',
    'PENDING_REVIEW': '待审核'
  }
  
  return statusMap[status] || status
}

// 格式化流程数据
function formatFlowData(jsonStr: string): string {
  try {
    const data = JSON.parse(jsonStr)
    return JSON.stringify(data, null, 2)
  } catch {
    return jsonStr
  }
}

// 计算版本差异
const diffResult = computed(() => {
  if (compareVersions.value.length !== 2) return ''
  
  try {
    const oldText = formatFlowData(compareVersions.value[0]?.flowGraph || '')
    const newText = formatFlowData(compareVersions.value[1]?.flowGraph || '')
    
    // 使用diff库生成差异
    const diff = diffLib.diffLines(oldText, newText)
    
    // 转换为HTML格式，使用不同颜色突出显示添加和删除的内容
    let html = ''
    diff.forEach((part) => {
      // 根据part的状态设置不同的样式
      const color = part.added ? 'bg-green-100 text-green-800' : 
                   part.removed ? 'bg-red-100 text-red-800' : ''
      
      if (color) {
        html += `<div class="${color} p-1 rounded">`
        html += part.value.replace(/\n/g, '<br>')
                          .replace(/ /g, '&nbsp;')
                          .replace(/</g, '&lt;')
                          .replace(/>/g, '&gt;')
        html += '</div>'
      } else {
        html += `<div>${part.value.replace(/\n/g, '<br>')
                                .replace(/ /g, '&nbsp;')
                                .replace(/</g, '&lt;')
                                .replace(/>/g, '&gt;')}</div>`
      }
    })
    
    return html
  } catch (error) {
    console.error('计算版本差异失败:', error)
    return '<div class="text-red-500">无法计算版本差异</div>'
  }
})

// 加载版本列表
async function loadVersions() {
  if (!props.flowDefinitionId) return
  
  loading.value = true
  error.value = null
  
  try {
    await flowStore.fetchFlowVersions(props.flowDefinitionId)
    versions.value = flowStore.flowVersions
  } catch (err: any) {
    error.value = err.message || '加载版本列表失败'
    console.error('加载版本列表失败:', err)
  } finally {
    loading.value = false
  }
}

// 查看版本
function viewVersion(version: FlowVersion) {
  selectedVersion.value = version
  emit('view', version)
}

// 设为当前版本
async function setAsCurrent(version: FlowVersion) {
  try {
    // 这里应该调用store中设置当前版本的方法，现在暂时直接发送事件
    // 后续需要在flowStore中实现setFlowVersionAsCurrent方法
    await flowStore.setFlowVersionAsCurrent(version.id)
    emit('setAsCurrent', version)
    loadVersions() // 重新加载列表
  } catch (error) {
    console.error('设置当前版本失败:', error)
  }
}

// 对比版本
function compareSelectedVersions() {
  if (selectedVersions.value.length !== 2) return
  
  compareVersions.value = [...selectedVersions.value]
  showCompareModal.value = true
}

// 执行回滚版本操作
function rollbackSelectedVersion() {
  if (selectedVersion.value) {
    rollbackVersion.value = selectedVersion.value
    showRollbackModal.value = true
  }
}

// 检查是否是最新版本
function isLatestVersion(version: FlowVersion): boolean {
  if (versions.value.length === 0) return false
  
  // 按版本号排序，找出最大的版本号
  const maxVersion = Math.max(...versions.value.map(v => v.version))
  return version.version === maxVersion
}

// 检查是否来自回滚操作
function isFromRollback(version: FlowVersion): boolean {
  // 这里根据描述判断是否是回滚版本
  // 实际项目中可能需要更可靠的方法，比如在版本中添加标记
  return version.description?.includes('回滚') || false
}

// 预览版本（不切换当前版本）
function previewVersion(version: FlowVersion) {
  emit('view', version)
}

// 确认回滚
async function confirmRollback() {
  if (!rollbackVersion.value) return
  
  try {
    // 这里调用实际的回滚API
    await flowStore.rollbackToVersion(rollbackVersion.value.id)
    emit('rollback', rollbackVersion.value)
    showRollbackModal.value = false
    
    // 回滚成功后重新加载
    await loadVersions()
  } catch (error) {
    console.error('回滚版本失败:', error)
  }
}

// 监听流程定义ID变化
watch(() => props.flowDefinitionId, (newId) => {
  if (newId) {
    selectedVersionIds.value = []
    selectedVersion.value = null
    loadVersions()
  }
})

// 组件挂载时加载数据
onMounted(() => {
  if (props.flowDefinitionId) {
    loadVersions()
  }
})
</script> 
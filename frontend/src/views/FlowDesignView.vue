<template>
  <div class="flow-design h-screen flex flex-col">
    <div class="h-12 px-4 flex items-center justify-between border-b">
      <h1 class="text-lg font-bold">预案设计器</h1>
      <div class="flex gap-2">
        <button class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600">保存</button>
        <button class="px-3 py-1 bg-green-500 text-white rounded hover:bg-green-600">发布</button>
      </div>
    </div>
    <div class="flex flex-1 overflow-hidden">
      <div class="w-64 border-r p-2 overflow-y-auto">
        <h2 class="text-sm font-bold mb-2">节点类型</h2>
        <div class="grid grid-cols-2 gap-2">
          <div class="p-2 border rounded text-center cursor-move bg-gray-50 hover:bg-gray-100">
            开始
          </div>
          <div class="p-2 border rounded text-center cursor-move bg-gray-50 hover:bg-gray-100">
            结束
          </div>
          <div class="p-2 border rounded text-center cursor-move bg-gray-50 hover:bg-gray-100">
            判断
          </div>
          <div class="p-2 border rounded text-center cursor-move bg-gray-50 hover:bg-gray-100">
            动作
          </div>
          <div class="p-2 border rounded text-center cursor-move bg-gray-50 hover:bg-gray-100">
            延时
          </div>
          <div class="p-2 border rounded text-center cursor-move bg-gray-50 hover:bg-gray-100">
            子流程
          </div>
        </div>
      </div>
      <div class="flex-1 relative">
        <!-- 这里将来是Vue Flow画布 -->
        <div class="w-full h-full bg-gray-100 flex items-center justify-center">
          <p class="text-gray-500">Vue Flow 流程设计画布区域</p>
        </div>
      </div>
      <div class="w-80 border-l p-4 overflow-y-auto">
        <h2 class="text-sm font-bold mb-2">节点配置</h2>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">节点名称</label>
            <input type="text" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">节点类型</label>
            <select class="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border">
              <option>开始节点</option>
              <option>结束节点</option>
              <option>判断节点</option>
              <option>动作节点</option>
              <option>延时节点</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">节点描述</label>
            <textarea class="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" rows="3"></textarea>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 未来将引入Vue Flow组件
</script> 
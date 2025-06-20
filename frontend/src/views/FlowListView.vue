<template>
  <div class="flow-list p-4">
    <h1 class="text-2xl font-bold mb-4">预案列表</h1>
    <div class="overflow-x-auto">
      <table class="min-w-full bg-white shadow-md rounded-lg overflow-hidden">
        <thead class="bg-gray-100">
          <tr>
            <th class="py-2 px-4 text-left">ID</th>
            <th class="py-2 px-4 text-left">名称</th>
            <th class="py-2 px-4 text-left">类型</th>
            <th class="py-2 px-4 text-left">状态</th>
            <th class="py-2 px-4 text-left">版本</th>
            <th class="py-2 px-4 text-left">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="i in 5" :key="i" class="border-b hover:bg-gray-50">
            <td class="py-2 px-4">{{ i }}</td>
            <td class="py-2 px-4">示例预案 {{ i }}</td>
            <td class="py-2 px-4">常规预案</td>
            <td class="py-2 px-4">
              <span class="px-2 py-1 rounded-full text-xs" 
                :class="i % 2 === 0 ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'">
                {{ i % 2 === 0 ? '已发布' : '草稿' }}
              </span>
            </td>
            <td class="py-2 px-4">v{{ i }}.0</td>
            <td class="py-2 px-4">
              <router-link :to="`/flow-design?id=${i}`" class="text-blue-600 hover:text-blue-800 mr-2">编辑</router-link>
              <button class="text-red-600 hover:text-red-800">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
</script> 
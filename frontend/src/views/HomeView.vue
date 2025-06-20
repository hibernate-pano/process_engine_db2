<template>
  <div class="home">
    <h1 class="text-3xl font-bold mb-4">预案管理系统</h1>
    <div class="mt-8 flex gap-4 justify-center">
      <router-link to="/flow-list" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md">
        预案列表
      </router-link>
      <router-link to="/flow-design" class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md">
        创建预案
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
</script> 
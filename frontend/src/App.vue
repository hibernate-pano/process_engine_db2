<script setup lang="ts">
// 主应用组件
</script>

<template>
  <div class="app min-h-screen w-full bg-gray-50">
    <header class="bg-white shadow">
      <div class="w-full px-4 py-4 flex justify-between items-center">
        <h1 class="text-xl font-bold text-gray-800">预案管理系统</h1>
        <nav>
          <ul class="flex space-x-4">
            <li><router-link to="/" class="text-gray-600 hover:text-blue-500">首页</router-link></li>
            <li><router-link to="/flow-list" class="text-gray-600 hover:text-blue-500">预案列表</router-link></li>
            <li><router-link to="/flow-design" class="text-gray-600 hover:text-blue-500">预案设计</router-link></li>
          </ul>
        </nav>
      </div>
    </header>
    <main class="w-full">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>

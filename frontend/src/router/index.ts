import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/flow-design',
      name: 'flowDesign',
      component: () => import('../views/FlowDesignView.vue')
    },
    {
      path: '/flow-list',
      name: 'flowList',
      component: () => import('../views/FlowListView.vue')
    }
  ]
})

export default router 
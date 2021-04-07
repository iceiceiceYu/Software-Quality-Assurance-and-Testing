import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Dashboard from '@/components/Dashboard'
import AccountFlow from '@/components/AccountFlow'
import DayEndProcessing from '@/components/DayEndProcessing'
import FinancialProducts from '@/components/FinancialProducts'
import ManageLoanAccount from '@/components/ManageLoanAccount'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: Dashboard
    },
    {
      path:'/AccountFlow',
      name:'AccountFlow',
      component:AccountFlow
    },
    {
      path:'/DayEndProcessing',
      name:'DayEndProcessing',
      component:DayEndProcessing
    },
    {
      path:'/FinancialProducts',
      name:'FinancialProducts',
      component:FinancialProducts
    },
    {
      path:'/ManageLoanAccount',
      name:'ManageLoanAccount',
      component:ManageLoanAccount
    }
  ]
})

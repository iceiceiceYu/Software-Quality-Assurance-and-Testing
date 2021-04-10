import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    id: localStorage.getItem('id') || null,
    username: localStorage.getItem('username') || null,

  },
  mutations: {
    login(state, data){
      localStorage.setItem('id', data.id)
      localStorage.setItem('username', data.username)

      state.id = data.id
      state.username = data.username

    },
    logout(state) {
      // 移除token
      localStorage.removeItem('id')
      localStorage.removeItem('username')

      state.id = null
      state.username = null

    }
  },
  actions: {
  }
})

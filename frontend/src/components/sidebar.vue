<template>
  <el-menu
    :default-active="activeIndex"
    :unique-opened=true
    class="el-menu-vertical-demo" style="margin-top: 5px"
  @select="handleSelect">
    <el-submenu index="1">
      <template #title>
        <i class="el-icon-location"></i>
        <span>贷款业务</span>
      </template>
      <el-menu-item-group>
        <el-menu-item index="1-1">贷款账户管理</el-menu-item>
        <el-menu-item index="1-2">日终处理</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="2">
      <template #title>
        <i class="el-icon-location"></i>
        <span>公共业务</span>
      </template>
      <el-menu-item-group>
        <el-menu-item index="2-1">账户流水</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="3">
      <template #title>
        <i class="el-icon-location"></i>
        <span>存款业务</span>
      </template>
      <el-menu-item-group>
        <el-menu-item index="3-1">理财产品购买</el-menu-item>
      </el-menu-item-group>
    </el-submenu>


  </el-menu>
</template>

<script>
    export default {
        name: "v-side",
      props:{
          activeIndex:{
            type: String,
          default: '1-1'
        }
      },data(){
          return{
            type:this.$store.state.type
          }
      },methods:{
          handleSelect(key, keyPath) {
              switch (key) {
                case '1':
                  this.$router.replace({path: '/doctor/checkPatients'})
                  break;
                case '2':
                  this.$router.replace({path: '/doctor/checkStaff'})
                  break;
                case '3':
                  this.$router.replace({path: '/doctor/newReport'})
                  break;
                case '4':
                  this.$router.replace({path: '/doctor/news'})
                  break;
              }
          },

      }
    }
</script>

<style scoped>

</style>

<template>
  <el-container>
    <el-header>
      <v-navbar :activeIndex="'1'"></v-navbar>
    </el-header>
    <el-container>
      <el-aside>
        <v-side :activeIndex="'1-2'"></v-side>
      </el-aside>
      <el-main>
        <h1>日终处理</h1>
        <el-button>开始日终处理</el-button>


      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  export default {
    name: "DayEndProcessing",
    data() {
      return {

      };
    },
    methods: {
    }
  }
</script>

<style scoped>

</style>

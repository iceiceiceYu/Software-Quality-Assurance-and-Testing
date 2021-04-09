<template>
  <el-container>
    <el-header>
      <v-navbar :activeIndex="'1'"></v-navbar>
    </el-header>
    <el-container>
      <el-aside>
        <v-side :activeIndex="'2-1'"></v-side>
      </el-aside>
      <el-main>
        <h1>流水信息</h1>
        <el-row>
          <el-table
            :data="Flow"
            stripe
            style="width: 100%">
            <el-table-column
              prop="id"
              label="流水号"
             sortable>
            </el-table-column>
            <el-table-column
              prop="name"
              label="账户姓名">
            </el-table-column>
            <el-table-column
              prop="AccountId"
              label="账户">
            </el-table-column>
            <el-table-column
              prop="date"
              label="日期"
            sortable>
            </el-table-column>

          </el-table>
        </el-row>


      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  export default {
    name: "AccountFlow",
    data() {
      return {
        Flow:[]

      };
    },
    methods: {
    }
  }
</script>

<style scoped>

</style>

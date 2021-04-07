<template>
  <el-container>
    <el-header>
      <v-navbar :activeIndex="'1'"></v-navbar>
    </el-header>
    <el-container>
      <el-aside>
        <v-side :activeIndex="'1-1'"></v-side>
      </el-aside>
      <el-main>
        <h1>贷款账户管理</h1>

        <el-row>
          <el-form :inline="true" :modal="loan" ref="loan"  label-width="80px">
            <el-form-item label="身份证号:">
              <el-input type="text" v-model="loan.id"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary">查询账单</el-button>
            </el-form-item>
          </el-form>
        </el-row>

        <el-row>
          <el-table
            :data="tableData"
            stripe
            style="width: 100%">
            <el-table-column
              prop="id"
              label="ID"
              width="50">
            </el-table-column>
            <el-table-column
              prop="name"
              label="姓名">
            </el-table-column>
            <el-table-column
              prop="gender"
              label="性别">
              <template slot-scope="scope">
                <span v-if="scope.row.gender==='male'">男</span>
                <span v-else-if="scope.row.gender==='female'">女</span>
              </template>
            </el-table-column>
          </el-table>
        </el-row>



      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  export default {
    name: "ManageLoanAccount",
    data() {
      return {
        loan:{
          id:'',
        },
        tableData:[],

      };
    },
    methods: {
    }
  }
</script>

<style scoped>

</style>

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
              <el-button type="primary">查询账户</el-button>
            </el-form-item>
          </el-form>
        </el-row>

        <el-row>
          <el-table
            :data="Account"
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

        <el-divider>账单信息</el-divider>
        <el-row>
          <el-table
            :data="Bill"
            stripe
            style="width: 100%">
            <el-table-column
              prop="id"
              label="账单流水号">
            </el-table-column>
            <el-table-column
              prop="name"
              label="姓名">
            </el-table-column>
            <el-table-column
              prop="due"
              label="是否逾期">
              <template slot-scope="scope">
                <span v-if="scope.row.due===true">已逾期</span>
                <span v-else-if="scope.row.due===false">未逾期</span>
              </template>
            </el-table-column>
            <el-table-column  label="操作" fixed="right">
              <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="partialReturn(scope.row)">部分归还</el-button>
                  <el-button size="mini" @click="totalReturn(scope.row)">全额归还</el-button>
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
        Account:[{id:1,name:'wfaarvza',gender:'male'}],
        Bill:[{id:123,name:'hahah',due:true}],
        selectedBill: {},

      };
    },
    methods: {
      totalReturn(row) {
        this.selectedBill = row
        this.$axios.post('/全额归还的接口', {      //TODO
          BillId: this.selectedBill.id,
        }).then(resp => {
          this.$notify({
            title: '归还成功！',
            type: 'success'
          });
        })
          .catch(error => {
            console.log(error);
            alert('网络连接失败')
          })
      },
    }
  }
</script>

<style scoped>

</style>

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
          <el-form :inline="true" :modal="loan" ref="loan" label-width="80px">
            <el-form-item label="身份证号:">
              <el-input type="text" v-model="loan.id"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="findAccount">查询账户</el-button>
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
              label="账户号"
            >
            </el-table-column>
            <el-table-column
              prop="name"
              label="姓名">
            </el-table-column>


            <el-table-column label="操作" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="findBill(scope.row)">查找账单</el-button>
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
            <el-table-column label="操作" fixed="right">
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
        loan: {
          id: '',
        },
        Account: [{id: 111111111111, name: 'wfaarvza'}],
        Bill: [{id: 123, name: 'hahah', due: true}],
        selectedAccount: {},
        selectedBill: {},
      };
    },
    methods: {
      findAccount() {
        this.$axios.post('/findAccount',
          this.loan.id, //身份证号
        ).then(resp => {
          if (resp.data != null) {
            this.Account = resp.data
          } else {
            this.$notify({
              title: '没有找到对应账户',
              type: 'warning'
            });
          }
        }).catch(error => {
          console.log(error);
          alert('网络连接失败')
        })
      },
      findBill(row) {
        this.selectedAccount = row
        this.$axios.post('/findBill',
          this.selectedAccount.id, //账户号
        ).then(resp => {
          this.Bill = resp.data
        }).catch(error => {
          console.log(error);
          alert('网络连接失败')
        })
      },


      totalReturn(row) {
        this.selectedBill = row
        this.$axios.post('/totalReturn',
          this.selectedBill.id,//账单号
        ).then(resp => {
          if (resp.data === 'success') {
            this.$notify({
              title: '归还成功！',
              type: 'success'
            });
          } else {
            this.$notify({
              title: '归还失败！',
              type: 'warning'
            });
          }
        }).catch(error => {
          console.log(error);
          alert('网络连接失败')
        })
      },

      partialReturn(row) {
        //TODO
      }
    }
  }
</script>

<style scoped>

</style>

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
                    <el-form :inline="true" :modal="messageForm" ref="message" label-width="80px">
                        <el-form-item label="身份证号:">
                            <el-input type="text" v-model="messageForm.id"></el-input>
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
                                label="账户号">
                        </el-table-column>
                        <el-table-column
                                prop="IDCode"
                                label="归属身份证">
                        </el-table-column>
                        <el-table-column
                                prop="total"
                                label="余额">
                        </el-table-column>


                        <el-table-column label="操作" fixed="right">
                            <template slot-scope="scope">
                                <el-button size="mini" type="primary" @click="findLoan(scope.row)">查找账单</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-row>


                <el-divider>账单信息</el-divider>
                <el-row>
                    <el-table
                            :data="Loan"
                            stripe
                            style="width: 100%">
                        <el-table-column
                                prop="id"
                                label="账单流水号">
                        </el-table-column>

                        <el-table-column
                                prop="amount"
                                label="贷款金额">
                        </el-table-column>
                        <el-table-column
                                prop="stage"
                                label="分期数">
                        </el-table-column>
                        <el-table-column
                                prop="interest"
                                label="利率">
                        </el-table-column>
                        <el-table-column
                                prop="isPaidOff"
                                label="归还状态">
                            <template slot-scope="scope">
                                <span v-if="scope.row.isPaidOff===true">归还完毕</span>
                                <span v-else-if="scope.row.isPaidOff===false">未归还完毕</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" fixed="right" width="300px">
                            <template slot-scope="scope">
                                <el-button v-if="scope.row.isPaidOff===false" size="mini" type="primary"
                                           @click="partialReturn(scope.row)">部分归还
                                </el-button>
                                <el-button v-if="scope.row.isPaidOff===false" size="mini"
                                           @click="totalReturn(scope.row)">全额归还
                                </el-button>
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
                messageForm: {
                    id: '',
                },
                Account: [],
                Loan: [],
                selectedAccount: {},
                selectedLoan: {},
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
            this.selectedLoan = row
            this.$prompt('请输入归还金额', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputPattern: /^\d+(\.\d+)?$/,
                inputErrorMessage: '输入金额格式不正确'
            }).then(({value}) => {
                this.$axios.post('/repay/repayment', {
                    loanId: this.selectedLoan.id,
                    money: value,
                    type: 0
                }).then(resp => {
                    if (resp.data === 'Success') {
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
            })
        }
        }
    }
</script>

<style scoped>

</style>

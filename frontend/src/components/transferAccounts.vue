<template>
    <el-container>
        <el-header>
            <v-navbar :activeIndex="'1'"></v-navbar>
        </el-header>
        <el-container>
            <el-aside>
                <v-side :activeIndex="'2-2'"></v-side>
            </el-aside>
            <el-main>
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
                                <el-button size="mini" type="primary" @click="selectAccounts(scope.row)">转账</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-row>

                <el-dialog title="转账内容" :visible.sync="dialogFormVisible">
                    <el-form :model="transferForm" :rules="rules">
                        <el-form-item label="目标账户" prop="toIDCode">
                            <el-input v-model="transferForm.toIDCode" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="金额" prop="amount">
                            <el-input v-model="transferForm.amount" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="trans">确 定</el-button>
                    </div>
                </el-dialog>

            </el-main>
        </el-container>
    </el-container>
</template>

<script>
    export default {
        name: "TransferAccounts",
        data() {
            return {
                dialogFormVisible: false,
                transferForm: {
                    amount: '',
                    toIDDode: '',
                },
                messageForm: {
                    id: '',
                },
                rules: {
                    amount: [{required: true, message: '请输入金额', trigger: 'blur'},
                        {pattern: /^\d+(\.\d+)?$/, message: '请正确金额', trigger: 'blur'}],
                    toIDCode: [{required: true, message: '请输入账户', trigger: 'blur'},
                        {pattern: /^[0-9]*$/, message: '请正确输入账户', trigger: 'blur'}]
                },

                Account: [],
                selectedAccount: []

            };
        },
        methods: {
            findAccount() {
                this.$axios.post('/client/accountInfo',
                    this.messageForm.id //身份证号
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
            selectAccounts(row) {
                this.selectedAccount = row
                this.dialogFormVisible = true
            },
            trans() {
                this.$axios.post('/client/transfer', {
                    fromIdCode: this.selectedAccount.id,
                    toIdCode: this.transferForm.toIDDode,
                    amount: this.transferForm.amount
                }).then(resp => {
                    if (resp.data === 'success') {
                        this.$notify({
                            title: '转账成功！',
                            type: 'success'
                        });
                        this.dialogFormVisible = false
                    } else if (resp.data === 'wrong object') {
                        this.$notify({
                            title: '请检查目标账户！',
                            type: 'warning'
                        });
                    } else if (resp.data === 'wrong money') {
                        this.$notify({
                            title: '余额不足！',
                            type: 'warning'
                        })
                    }
                }).catch(error => {
                    console.log(error);
                    alert('网络连接失败')
                })

            },
        },
    }

</script>

<style scoped>

</style>

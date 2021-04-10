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
                <el-button @click="DayEndProcess">开始日终处理</el-button>


            </el-main>
        </el-container>
    </el-container>
</template>

<script>
    export default {
        name: "DayEndProcessing",
        data() {
            return {};
        },
        methods: {
            DayEndProcess() {
                this.$axios.post('/repay/autoRepayment',
                    true
                ).then(resp => {
                    this.$notify({
                        title: '处理成功！',
                        type: 'success'
                    });
                }).catch(error => {
                    console.log(error);
                    alert('网络连接失败')
                })
            }
        }
    }
</script>

<style scoped>

</style>

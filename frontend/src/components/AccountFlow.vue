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
              :filters="[{text: '隔离区', value: ''}, {text: '轻症', value: 0}, {text: '重症', value: 1}, {text: '危重症', value: 2}]"
              :filter-method="filterTag"
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

      getAllFlow() {
          this.$axios.post('/getAllFlow')
                  .then(resp => {
            if (resp.data === 'Success') {
              this.$notify({
                title: '归还成功！',
                type: 'success'
              });
              this.Flow = resp.data
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
      filterTag (value, row) {
        return row.conditionRate === value
      },

    }
  }
</script>

<style scoped>

</style>

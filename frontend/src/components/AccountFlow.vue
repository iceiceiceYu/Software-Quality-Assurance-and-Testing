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
          <div class="block">
            <span class="demonstration">默认</span>
            <el-date-picker
                    v-model="dates"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
            >
            </el-date-picker>
          </div>
          <el-button
                  @click="searchWithDate"
          >查询</el-button>
        </el-row>
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
<!--            <el-table-column-->
<!--              prop="name"-->
<!--              label="账户姓名">-->
<!--            </el-table-column>-->
            <el-table-column
              prop="account"
              label="账户">
            </el-table-column>
            <el-table-column
              prop="balance"
              label="balance"
              :filters="[{text: '收入', value: 1}, {text: '支出', value: -1}]"
              :filter-method="filterBalance"
            >

            </el-table-column>

            <el-table-column
                    prop="currentTotal"
                    label="currentTotal"
                    sortable>
            </el-table-column><el-table-column
                  prop="source"
                  label="source"
                  sortable>
          </el-table-column>

            <el-table-column
              prop="time"
              label="日期"
            sortable>
            </el-table-column>
            <el-table-column
                    prop="source"
                    label="source"
                    :filters="[{text: 'Financial Management Income', value: 'Financial Management Income'},
                    {text: 'Financial Management Outlay', value: 'Financial Management Outlay'},
                    {text: 'Loan Pay Outlay', value: 'Loan Pay Outlay'},
                    {text: 'Transfer Outlay', value: 'Transfer Outlay'},
                    {text: 'Transfer Income', value: 'Transfer Income'}]"
                    :filter-method="filterTag"
            >

            </el-table-column>

          </el-table>
        </el-row>


      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import store from '../store'
  export default {
    name: "AccountFlow",
    mounted() {
      this.getAllFlow()
    },
    data() {

      return {
        dates: '',
        Flow:[]

      };
    },
    methods: {
      searchWithDate() {
        this.Flow = []
        console.log(this.dates[0])
        console.log(this.dates[1])
        this.$axios.post('/transaction/find', {
          start: this.dates[0],
          end: this.dates[1]
        })
                .then(resp => {
                  if (resp.data != null) {

                    var response = resp.data
                    response.forEach((flow, index) => {

                      this.Flow.push({
                        id: flow.id,
                        account: flow.account.idcode,
                        balance: flow.balance,
                        currentTotal: flow.currentTotal,
                        source : flow.source,
                        time: flow.time
                      })
                    })
                  }
                }).catch(error => {
          console.log(error);
          alert('网络连接失败')
        })
      },

      getAllFlow() {
          this.$axios.post('/transaction/find', {
            start: '2000-01-01 00:00:00',
            end: '2030-01-01 00:00:00'
          })
                  .then(resp => {
            if (resp.data != null) {
              console.log(resp.data)

              var response = resp.data
              response.forEach((flow, index) => {

                this.Flow.push({
                  id: flow.id,
                  account: flow.account.idcode,
                  balance: flow.balance,
                  currentTotal: flow.currentTotal,
                  source : flow.source,
                  time: flow.time
                })
              })
            } else {
              this.$notify({
                title: '归还失败！',
                type: 'warning'
              });
            }
          }).catch(error => {
            console.log(error);
          })


      },
      filterTag (value, row) {
        return row.source === value
      },
      filterBalance (value, row) {
        return row.balance * value > 0
      },

    }
  }
</script>

<style scoped>

</style>

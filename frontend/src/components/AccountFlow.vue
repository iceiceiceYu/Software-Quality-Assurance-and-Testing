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
                    value-format="yyyy-mm-dd"
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
              prop="time"
              label="日期"
            sortable>
            </el-table-column>
            <el-table-column
                    prop="source"
                    label="source"
                    :filters="[{text: 'Financial Management Income', value: 'Financial Management Income'}, {text: 'Financial Management Outlay', value: 'Financial Management Outlay'}]"
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
  export default {
    name: "AccountFlow",
    mounted() {
      this.getAllFlow()
    },
    data() {

      return {
        dates: '',
        Flow:[{
          id: 102313,
          account: {

          },
          balance: 40,
          time: '',
          source: 'Financial Management Income'
        },
          {
            id: 104513,
            account: {

            },
            balance: 31,
            time: '',
            source: 'Financial Management Outlay'
          },
          {
            id: 106263,
            account: {

            },
            balance: 131,
            time: '',
            source: 'Financial Management Outlay'
          }]

      };
    },
    methods: {
      searchWithDate() {
        this.$axios.post('/searchWithDate', {
          start: this.dates[0],
          end: this.dates[1]
        })
                .then(resp => {
                  if (resp.data === 'Success') {

                    this.Flow = resp.data
                  }
                }).catch(error => {
          console.log(error);
          alert('网络连接失败')
        })
      },

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

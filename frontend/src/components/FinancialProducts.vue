<template>
  <el-container>
    <el-header>
      <v-navbar :activeIndex="'1'"></v-navbar>
    </el-header>
    <el-container>
      <el-aside>
        <v-side :activeIndex="'3-1'"></v-side>
      </el-aside>
      <el-main>
        <h1>理财产品购买</h1>
        <el-row>
          <el-form :inline="true" :modal="account" ref="account" label-width="80px">
            <el-form-item label="身份证号:">
              <el-input type="text" v-model="account.id"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="getCredential">查询理财产品</el-button>
            </el-form-item>
          </el-form>
        </el-row>

        <el-row>
          我的理财产品
          <el-table
                  :data="myProductTable"
                  style="width: 100%">
            <el-table-column
                    prop="name"
                    label="名称"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="type"
                    label="种类"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="price"
                    label="价格">
            </el-table-column>
          </el-table>

        </el-row>

        <el-divider></el-divider>
        <el-row>
          <el-col :offset="2" :span="10">
            <p>您的信用等级为：{{this.creditLevel}}级，以下是您可以购买的理财产品：</p>
          </el-col>
          <el-col :span="6">
          <el-select v-model="value" placeholder="请选择产品类型">
            <el-option
              v-for="item in options"
              :key="item.value"
              :disabled="item.disable"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>


          <el-select v-model="value" placeholder="请选择具体产品">
            <el-option
                    v-for="item in productoptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>

          </el-col>
          <el-col>
            {{this.value}}
          </el-col>

        </el-row>
        <el-row>
          <el-col v-if="this.value==='stock'">
            <el-input
                    v-model="this.stockAmount"
                    placeholder="请输入您要购买的股数"
            >
            </el-input>

          </el-col>
          <el-col>
            <el-date-picker
              v-model="purchaseDate"
              type="datetime"
              placeholder="选择日期"
              :picker-options="pickerOptions"
              value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>

          </el-col>
          <el-col>
            {{this.purchaseDate}}
          </el-col>
          <el-button
            @click="purchase"

          >购买</el-button>
        </el-row>


      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import store from '../store'
  export default {
    mounted() {
      this.$axios.post('/financial/increase',
              store.state.username
      ).then(resp => {
      }).catch(error => {
        console.log(error);
        alert('网络连接失败')
      })
    },
      name: "FinancialProducts",
      data() {

        const generateProducts = _ => {
          const data = []
          this.$axios.post('/financial/allInfo', this.username)
                  .then(resp => {
                    if (resp != null) {
                      var response = resp.data
                      response.forEach((product, index) => {
                        data.push({
                          name: product.name,
                          type: product.type,
                          price: product.price,
                          disabled: product.type !== this.type
                        })
                      })
                    }
                  })
                  .catch(error => {
                    console.log(error)
                  })
          return data
        }
          return {
            productName: '',
            accountId: '',
            pickerOptions: {
              disabledDate (time) {
                return time.getTime() < Date.now();
              }
            },
            account: {},
            options: [{
              value: 'stock',
              label: '股票',
              disable: this.creditLevel===2 || this.creditLevel===3
            }, {
              value: 'fund',
              label: '基金',
              disable: this.creditLevel===3
            }, {
              value: 'deposit',
              label: '定期',
              disable: false
            }],
            productoptions: generateProducts(),
            value: '',
            creditLevel: 1,
            purchaseDate: '',
            stockAmount: '',
            myProductTable: []

          };
      },
    methods: {
        getCredential() {

          this.$axios.post('/accountLevel',
              this.account.id
          ).then(resp => {
            this.$notify({
              title: resp.data,
              type: 'warning'
            });
          }).catch(error => {
            console.log(error);
            alert('网络连接失败')
          })


          this.$axios.post('/financial/purchaseInfo',
                  this.account.id
          ).then(resp => {
            if (resp != null) {
              var response = resp.data
              response.forEach((product, index) => {
                this.myProductTable.push({
                  name: product.name,
                  type: product.type,
                  price: product.price
                })
              })
            }
          })
                  .catch(error => {
                    console.log(error)
                  })

        },

        getMyFinancialProducts() {


        },

        purchase() {

          this.$axios.post('/purchase',
            {
              IDCode: this.account.id,
              name: this.productName,
              type: this.type,
              stockAmount: this.stockAmount,
              date: this.date
            },
          ).then(resp => {
            if (resp.data === 'success') {
              this.$notify({
                title: '购买成功！',
                type: 'success'
              });
            } else {
              this.$notify({
                title: resp.data,
                type: 'warning'
              });
            }
            // this.$notify({
            //   title: resp.data,
            //   type: 'warning'
            // });
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

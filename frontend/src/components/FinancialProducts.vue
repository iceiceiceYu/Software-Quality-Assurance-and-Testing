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
        </el-row>

        <el-row>
          <el-col :span="6">
          <el-select v-model="productType" placeholder="请选择产品类型">
            <el-option
              v-for="item in options"
              :key="item.value"
              :disabled="item.disable"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>


          </el-col >
          <el-col :span="6">
            <el-button @click="showProducts">显示产品</el-button>


          </el-col>
        </el-row>
        <br>
        <el-row>


          <el-col :span="6">
            <el-select v-model="productName" placeholder="请选择具体产品">
              <el-option
                      v-for="item in productoptions"
                      :key="item.value"
                      :label="item.label"
                      :disabled="item.disable"
                      :value="item.value">
              </el-option>
            </el-select>
          </el-col>

<!--          <el-col>-->
<!--            {{this.productName}}-->
<!--          </el-col>-->
          <el-col :span="6">
            <el-date-picker
                    v-model="purchaseDate"
                    type="datetime"
                    placeholder="选择日期"
                    :picker-options="pickerOptions"
                    value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>

          </el-col>
          <el-col :span="6"
                  v-if="this.productType==='stock'">
            <el-input
                    v-model="this.amount"
                    placeholder="请输入您要购买的股数"
            >
            </el-input>

          </el-col>

          <el-col>
            {{this.purchaseDate}}
          </el-col>

        </el-row>
        <br>

        <el-row>
          <el-col :span="6">
          <el-button
                  @click="purchase"

          >购买</el-button>
          </el-col>
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
      })
    },
      name: "FinancialProducts",
      data() {

        const generateProducts = _ => {
          const data = []
          let pName = this.productName
          this.$axios.post('/financial/allInfo', store.state.username)
                  .then(resp => {
                    if (resp != null) {
                      var response = resp.data
                      response.forEach((product, index) => {
                        console.log(product.type)
                        data.push({
                          value: product.name,
                          label: product.name,
                          // type: product.type,
                          // price: product.price,
                          // disable: product.type !== pName
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
            productType: '',
            accountId: '',
            value: '',
            value1: '',
            creditLevel: 2,
            pickerOptions: {
              disabledDate (time) {
                return time.getTime() < Date.now();
              }
            },
            account: {},
            options: [{
              value: 'stock',
              label: 'stock',
              disable: (this.creditLevel===2) || (this.creditLevel===3)
            }, {
              value: 'fund',
              label: 'fund',
              disable: this.creditLevel===3
            }, {
              value: 'deposit',
              label: 'deposit',
              disable: false
            }],
            productoptions: [],
            // productoptions: generateProducts(),


            purchaseDate: '',
            amount: '',
            myProductTable: []

          };
      },
    methods: {
      showProducts() {
        let pName = this.productType
        this.$axios.post('/financial/allInfo', store.state.username)
                .then(resp => {
                  if (resp != null) {
                    var response = resp.data
                    this.productoptions = []
                    response.forEach((product, index) => {
                      console.log(product.type)
                      this.productoptions.push({
                        value: product.name,
                        label: product.name,
                        // type: product.type,
                        // price: product.price,
                        disable: product.type !== pName
                      })
                    })
                  }
                })
                .catch(error => {
                  console.log(error)
                })
      },
        getCredential() {

          this.$axios.post('/financial/accountLevel',
              this.account.id
          ).then(resp => {
            this.creditLevel = resp.data
            this.options[0].disable = (this.creditLevel===2) || (this.creditLevel===3)
            this.options[1].disable = this.creditLevel===3
            this.$notify({
              title: "信用级别："+resp.data,
              type: 'success'
            });
          }).catch(error => {
            console.log(error);
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
                  stockAmount: product.stockAmount,
                  capital: product.capital,
                  profit: product.profit
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
          console.log(this.account.id)
          console.log(this.productName)
          console.log(this.productType)
          console.log(this.amount)
          console.log(this.purchaseDate)

          this.$axios.post('/financial/purchaseProduct',
            {
              idcode: this.account.id,
              name: this.productName,
              type: this.productType,
              stockAmount: this.amount===''?0:this.amount,
              date: this.purchaseDate
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

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
              <el-button type="primary">查询理财产品</el-button>
            </el-form-item>
          </el-form>
        </el-row>

        <el-divider></el-divider>
        <el-row>
          <el-col :offset="2" :span="10">
            <p>您的信用等级为：1级，以下是您可以购买的理财产品：</p>
          </el-col>
          <el-col :span="6">
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          </el-col>
        </el-row>


      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  export default {
    name: "FinancialProducts",
    data() {
      return {
        account: {},
        options: [{
          value: 'stock',
          label: '股票'
        }, {
          value: 'fund',
          label: '基金'
        }, {
          value: 'deposit',
          label: '定期'
        }],
        value: ''

      };
    },
    methods: {}
  }
</script>

<style scoped>

</style>

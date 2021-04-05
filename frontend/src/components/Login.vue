<template>
  <body id="poster">
  <el-form class="login-container" :model="loginForm" ref="loginForm"
           :rules="rules" label-width="80px">
    <h2>{{msg}}</h2>
    <el-form-item label="用户名：">
      <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="用户名"></el-input>
    </el-form-item>
    <el-form-item label="密码：">
      <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button class="login-button" type="primary" style="width: 100%;border: none" @click="submitClick('loginForm')">
        登录
      </el-button>
    </el-form-item>
  </el-form>

  </body>

</template>
<script>
  export default {
    name: 'Login',
    data() {
      return {
        rules: {
          username: [
            {required: true, message: '请输入用户名', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'}
          ]
        },
        loginForm: {
          username: '',
          password: ''
        },
        checked: true,
        msg: '银行业务模拟实训系统'
      }
    },
    methods: {
      submitClick(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$axios.post('/user/login', {
              username: this.loginForm.username,
              password: this.loginForm.password
            })
              .then(resp => {
                console.log(resp.data)
                if (resp.status === 200 && resp.data !== '') {
                  this.$store.commit('login', resp.data);
                  this.$router.replace({path: '/dashboard'})
                  this.$notify({
                    title: '登录成功',
                    type: 'success'
                  });
                } else {
                  alert('登陆失败！请检查用户名或密码。')
                }
              })
              .catch(error => {
                console.log(error);
                alert('网络连接失败')
              })
          } else {
            console.log("错误提交！")
            return false
          }
        })
      }
    }
  }
</script>

<style>
  #poster {
    background: url("../assets/eva.jpg");
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  body {
    margin: 0px;
    padding: 0;
  }

  .login-container {
    opacity: 90%;
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

</style>

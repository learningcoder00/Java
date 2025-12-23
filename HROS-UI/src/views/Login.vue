<template style="width: 100%;height: 100%;" >
  <div v-loading.fullscreen.lock="loading"
       class="login"
       element-loading-spinner="fa fa-spinner fa-pulse fa-3x fa-fw">
    <el-form :rules="rules"
             :model="loginFrom"
             class="logContainer"
             ref="loginFrom"
             @keydown.enter.native="loginSubmit">
      <h2 class="logtitle"><i class="fa fa-magnet fa-2x"
             style="color: #505458" />智 慧&nbsp;人 事 管 理</h2>
      <el-form-item prop="username">
        <el-input type="text"
                  v-model="loginFrom.username"
                  placeholder="请输入用户名"
                  auto-complete="off">
          <i slot="prefix"
             class="el-icon-user"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  v-model="loginFrom.password"
                  placeholder="请输入密码"
                  auto-complete="off">
          <i slot="prefix"
             class="el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input v-model="loginFrom.code"
                  auto-complete="off"
                  placeholder="验证码"
                  style="width: 63%"
                  @keyup.enter.native="loginSubmit">
          <i slot="prefix"
             class="el-icon-view"></i>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl"
               @click="getCode">
        </div>
      </el-form-item>
      <el-checkbox v-model="loginFrom.rememberMe"
                   style="margin:0 0 25px 0;">
        记住我
      </el-checkbox>
      <el-button type="primary"
                 style="width: 100%"
                 @click="loginSubmit">登录</el-button>
    </el-form>
  </div>
</template>

<script>
// import {postKeyValueRequest} from '../utils/api';
// 在main.js里以插件形似全局导入

export default {
  data () {
    return {
      codeUrl: '',
      cookiePass: '',
      loading: false,
      redirect: undefined,
      loginFrom: {
        username: 'admin',
        password: '123',
        code: '',
        rememberMe:''
      },
      checked: true,
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ],
        code: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
      }
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created () {
    this.getCode()

  },
  methods: {

    getCode () {
      this.getRequest("/auth/code").then(resp => {
          if(resp){
          console.log(resp)
        this.codeUrl = resp.img;
        this.loginFrom.uuid = resp.uuid;
        console.log(this.codeUrl );
        }
      });
    },
    loginSubmit () {
      //提交前空值校验
      this.$refs.loginFrom.validate((valid) => {
        if (valid) {
          // alert('submit!');
          this.loading = true;
          this.postKeyValueRequest('/doLogin', this.loginFrom
          ).then(data => {
            this.loading = false;
            if (data) {

              //方法将 JavaScript 的json对象转换为字符串。
              //将得到数存储在SessionStorage里
              window.sessionStorage.setItem("user", JSON.stringify(data.obj))
              //获取路由对象
              this.$router.replace('/home')
            }
          })
        } else {
          this.$notify.info({
            title: '系 统 讯 息',
            message: '输入框信息不完整哦!',
            showClose: false,
            offset: 100,
            duration: 5000,
            customClass: 'fontclass'
          });
        }
      });

    }
  }
}
</script>

<style >
.fontclass {
  font-size: 35px;
  font-family: 站酷庆科黄油体;
}

.login {
  background-size: cover;
  width: 100%;
  height: 100%;
  position: fixed;
  background-image: linear-gradient(to bottom right, #5470C6, #91CC75);
  display: flex;
  align-items: center;
  justify-content: center;
}
.logContainer {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 0 auto;
  width: 380px;
  padding: 40px 35px 20px 35px;
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid var(--border-light);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}
.logtitle {
  margin: 0px auto 30px auto;
  text-align: center;
  color: var(--primary-color);
  font-family: 站酷庆科黄油体;
  font-size: 24px;
  font-weight: bold;
}
.loginRen {
  text-align: center;
  margin: 0px 0px 35px 0px;
}
.login-code {
  width: 33%;
  display: inline-block;
  height: 38px;
  float: right;
}
.login-code img {
  cursor: pointer;
  vertical-align: middle;
  border-radius: 4px;
}

/* Element UI 组件样式覆盖 */
.el-input__inner {
  border-radius: 6px;
  border-color: var(--border-color);
  transition: all 0.3s ease;
}

.el-input__inner:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(84, 112, 198, 0.2);
}

.el-button--primary {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  border-radius: 6px;
  padding: 10px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.el-button--primary:hover {
  background-color: #6A85D6;
  border-color: #6A85D6;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(84, 112, 198, 0.3);
}

.el-button--primary:active {
  transform: translateY(0);
}

.el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.el-checkbox__input.is-checked+.el-checkbox__label {
  color: var(--primary-color);
}
</style>

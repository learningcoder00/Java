<template style="width: 100%;height: 100%;" >
  <div v-loading.fullscreen.lock="loading"
       class="login"
       element-loading-spinner="fa fa-spinner fa-pulse fa-3x fa-fw">
    <el-form :rules="rules"
             :model="loginFrom"
             class="logContainer"
             ref="loginFrom"
             @keydown.enter.native="loginSubmit">
      <h2 class="logtitle">
        <img src="@/common/img/logo.png" class="logo-img" alt="智管人事系统" />
        智管人事系统
      </h2>
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
  width: 100%;
  height: 100%;
  position: fixed;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.login::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('../common/img/bk.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  filter: blur(8px);
  transform: scale(1.1);
}

.login::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(123, 104, 238, 0.2);
}

@keyframes gradientShift {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

@keyframes float {
  0% { transform: translate(0, 0) rotate(0deg); }
  100% { transform: translate(-50px, -50px) rotate(360deg); }
}
.logContainer {
  border-radius: 20px;
  background-clip: padding-box;
  margin: 0 auto;
  width: 450px;
  padding: 35px 40px 30px 40px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 25px 70px rgba(0, 0, 0, 0.3), 
              0 0 0 1px rgba(255, 255, 255, 0.2) inset;
  backdrop-filter: blur(15px);
  position: relative;
  z-index: 1;
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.logtitle {
  margin: 0px auto 30px auto;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  font-family: 站酷庆科黄油体;
  font-size: 32px;
  font-weight: bold;
  letter-spacing: 3px;
  position: relative;
  background: linear-gradient(135deg, #7B68EE 0%, #9370DB 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logtitle .logo-img {
  width: 48px;
  height: 48px;
  object-fit: contain;
  vertical-align: middle;
  flex-shrink: 0;
}

.logtitle::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
    background: linear-gradient(90deg, #7B68EE, #9370DB);
  border-radius: 2px;
}
.loginRen {
  text-align: center;
  margin: 0px 0px 35px 0px;
}
.login-code {
  width: 33%;
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
  border-radius: 8px;
  border-color: var(--border-color);
  transition: all 0.3s ease;
  height: 44px;
  font-size: 15px;
  padding: 0 15px 0 40px;
}

.el-input__inner:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(123, 104, 238, 0.2);
}

.el-button--primary {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  border-radius: 8px;
  padding: 12px;
  font-size: 17px;
  font-weight: 500;
  transition: all 0.3s ease;
  height: 46px;
}

.el-button--primary:hover {
  background-color: #9370DB;
  border-color: #9370DB;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(123, 104, 238, 0.3);
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

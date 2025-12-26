<template>
  <div>

    <el-container>
      <el-header class="homeHeader">
        <div style="display: flex; align-items: center; width: 100%; position: relative;">
          <div class="header-logo-container" 
               style="display: flex; align-items: center; gap: 10px; flex-shrink: 0;"
               @click="goToHome">
            <img src="@/common/img/logo.png" class="header-logo" alt="智管人事系统" />
            <font id="title">智管人事系统</font>
          </div>
          
          <!-- 水平导航菜单 - 绝对居中 -->
          <div class="header-menu-center">
            <el-menu class="el-menu-horizontal"
                     router
                     unique-opened
                     active-text-color="#7B68EE"
                     mode="horizontal"
                     @select="menusClick"
                     :default-active="activeIndex">
              <el-submenu :index="index+''"
                          v-for="(item, index) in routes"
                          :key="index">
                <template slot="title">
                  <i style="color: #7B68EE; margin-right: 5px;"
                     :class="item.iconcls"></i>
                  <span>{{ item.name }}</span>
                </template>
                <el-menu-item :index="child.path"
                              v-for="( child, indexj ) in item.children"
                              :key="indexj">
                  <i style="color: #409eff; margin:0 5px;"
                     :class="child.iconcls"></i>
                  <span>{{ child.name }}</span>
                </el-menu-item>
              </el-submenu>
            </el-menu>
          </div>

          <div class="header-actions" style="display: flex; align-items: center; flex-shrink: 0;">
            <span class="el-dropdown-link">
              <i class="fa fa-envelope fa-1x "
                 style="cursor: pointer;margin-right:10px"
                 @click="pageChar"/>
            </span>

            <el-dropdown class="userInfo"
                         @command="commandHandler">
              <span class="el-dropdown-link">
                {{ user.name }}
                <i> <img :src="user.userface"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="logout"
                                  divided>注销登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main style="overflow: auto; height: calc(100vh - 60px); padding-top: 0;">
          <div class="homeRouterViewA"
               v-if="this.$router.currentRoute.path=='/home'">

            <Main/>
          </div>
          <router-view class="homeRouterViewA"/>
        </el-main>
    </el-container>
    <el-dialog :visible.sync="dialogTableVisible"
               width="0%"
               custom-class="pagechar">
      <pageChar/>
    </el-dialog>
  </div>
</template>

<script>
import Main from "./main";
import pageChar from "./chat/pageChat"

export default {

  name: "Home",
  data() {
    return {
      //获取用户信息，从sessionStorage中提取信息转换为
      user: JSON.parse(window.sessionStorage.getItem("user")),
      dialogTableVisible: false,
      activeIndex: '', // 控制导航栏高亮状态
    }
  },
  mounted() {
    // 初始化导航栏高亮状态
    if (this.$route.path === '/home') {
      this.activeIndex = '';
    } else {
      this.activeIndex = this.$route.path;
    }
  },
  methods: {
    goToHome(e) {
      e.preventDefault();
      e.stopPropagation();
      console.log('点击了logo，当前路径:', this.$route.path);
      // 清除导航栏高亮状态
      this.activeIndex = '';
      // 跳转到首页
      if (this.$route.path !== '/home') {
        this.$router.push('/home').catch(err => {
          // 如果路由已经在目标位置，忽略错误
          if (err.name !== 'NavigationDuplicated') {
            console.error('路由跳转错误:', err);
          }
        });
      } else {
        // 如果已经在首页，滚动到顶部并刷新视图
        const mainContent = document.querySelector('.el-main');
        if (mainContent) {
          mainContent.scrollTo({ top: 0, behavior: 'smooth' });
        }
        // 触发视图更新
        this.$nextTick(() => {
          this.$forceUpdate();
        });
      }
    },
    pageChar() {
      // this.$router.push("/chat");
      this.dialogTableVisible = true;
    },
    load() {
      this.loading = true
      setTimeout(() => {
        this.count += 3
        this.loading = false
      }, 2000)
    },
    /*子菜单的单机触发事件*/
    menusClick() {
      // 顶部导航栏不需要折叠功能
    },
    /*注销*/
    commandHandler(cmd) {
      if (cmd == 'logout') {
        this.$confirm('此操作注销登录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.getRequest('/logout');
          window.sessionStorage.removeItem("user");
          // 清除缓存
          this.$store.commit('initRoutes', []);
          this.$router.replace("/");

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          });
        });
      }
    },
  },
  computed: {
    routes() {
      return this.$store.state.routes
    },
    noMore() {
      return this.count >= 25
    },
    disabled() {
      return this.loading || this.noMore
    }

  },
  watch: {
    // 顶部导航栏不需要监听折叠状态
    $route(path) {
      // 当路由为首页时，清除导航栏高亮状态
      if (path.path === '/home') {
        this.activeIndex = '';
      } else {
        // 当路由为其他页面时，设置对应的高亮状态
        this.activeIndex = path.path;
      }
    }
  },
  components: {
    Main, pageChar
  }
}
</script>

<style>
.pagechar {
  display: flex;
  /* //居中对齐弹性盒的各项  元素：*/
  align-items: center;
  /*在弹性盒对象的  元素中的各项周围留有空白：*/
  justify-content: center;
}

.homeRouterViewA,
.homeRouterViewB {
  margin-top: 20px;
  width: 100%;
  height: calc(100vh - 60px);
  max-height: calc(100vh - 60px);
  padding: 0 20px;
  background: linear-gradient(to bottom, #f5f7fa 0%, #ffffff 100%);
  overflow: auto;
}

.homeHeader {
  background: transparent;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px 20px;
  box-sizing: border-box;
  font-family: 站酷庆科黄油体;
  height: 60px;
  position: relative;
  z-index: 100;
  transition: all 0.3s ease;
}

.header-logo-container {
  flex-shrink: 0;
  margin-left: 60px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 5px 10px;
  border-radius: 8px;
  position: relative;
  z-index: 1000;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  pointer-events: auto;
}

.header-logo-container:hover {
  background-color: rgba(123, 104, 238, 0.2);
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(123, 104, 238, 0.3);
}

.header-logo-container:hover #title {
  color: #7B68EE;
  text-shadow: 0 0 8px rgba(123, 104, 238, 0.5);
}

.header-logo-container:hover .header-logo {
  transform: scale(1.1);
  transition: transform 0.3s ease;
  filter: drop-shadow(0 0 4px rgba(123, 104, 238, 0.6));
}

.header-logo-container:active {
  background-color: rgba(123, 104, 238, 0.3);
  transform: scale(1.02);
  box-shadow: 0 1px 4px rgba(123, 104, 238, 0.4);
}

.header-logo-container:active #title {
  color: #6A5ACD;
}

.header-menu-center {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  justify-content: center;
}

.header-actions {
  position: absolute;
  right: 60px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  flex-shrink: 0;
}


#title {
  font-size: 20px;
  color: #7B68EE;
  font-weight: 600;
  user-select: none;
  pointer-events: none;
}

.header-logo-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-logo {
  width: 40px;
  height: 40px;
  object-fit: contain;
  flex-shrink: 0;
  transition: transform 0.3s ease;
  pointer-events: none;
  user-select: none;
}

.homeHeader .userInfo {
  /*光标变成手指*/
  cursor: pointer;
}

.el-dropdown-link img {
  width: 40px;
  height: 40px;
  /*添加圆角边框*/
  border-radius: 40px;
  margin-left: 15px;
}

.el-dropdown-link {
  display: flex;
  /* //居中对齐弹性盒的各项  元素：*/
  align-items: center;
  color: #7B68EE;
  font-weight: 600;
  font-size: 15px;
}

.el-menu-horizontal {
  border-bottom: none;
  background-color: transparent !important;
}

.el-menu-horizontal .el-menu-item,
.el-menu-horizontal .el-submenu__title {
  color: #7B68EE;
  height: 60px;
  line-height: 60px;
  border-bottom: none;
  transition: all 0.3s ease;
  margin: 0 5px;
  border-radius: 6px;
  background-color: transparent !important;
}

.el-menu-horizontal .el-menu-item:hover,
.el-menu-horizontal .el-submenu__title:hover {
  background-color: transparent !important;
  color: #7B68EE;
}

.el-menu-horizontal .el-menu-item.is-active,
.el-menu-horizontal .el-submenu.is-active .el-submenu__title {
  background-color: transparent !important;
  color: #7B68EE;
  border-bottom: 3px solid #7B68EE;
}

.el-menu-horizontal .el-submenu__title {
  background-color: transparent !important;
}

/* 下拉菜单宽度与父菜单项一致 */
.el-menu--horizontal .el-menu {
  min-width: auto !important;
}

.el-menu--horizontal .el-menu--popup {
  min-width: 0 !important;
  width: auto !important;
}

.el-menu--horizontal .el-menu--popup .el-menu-item {
  min-width: 0 !important;
  white-space: nowrap;
  padding: 0 20px;
}

/* 确保下拉菜单宽度与父菜单项标题宽度一致 */
.el-menu--horizontal .el-submenu .el-menu {
  min-width: 0 !important;
}

/* 下拉菜单容器宽度 */
.el-menu--horizontal .el-submenu .el-menu--popup {
  min-width: 0 !important;
  width: auto !important;
}
</style>
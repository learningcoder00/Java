<template>
  <div>

    <el-container>
      <el-header class="homeHeader">
        <div style="display: flex; align-items: center; width: 100%">
          <div style="margin-right: 30px"><i class="fa fa-magnet fa-2x" style="color: #d9c9c9"/>&nbsp; <font id="title">智慧人事管理系统</font></div>
          
          <!-- 水平导航菜单 -->
          <div style="flex: 1">
            <el-menu class="el-menu-horizontal"
                     router
                     unique-opened
                     active-text-color="#67C23A"
                     mode="horizontal"
                     @select="menusClick">
              <el-submenu :index="index+''"
                          v-for="(item, index) in routes"
                          :key="index">
                <template slot="title">
                  <i style="color: #ffffff; margin-right: 5px;"
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

          <div style="display: flex; align-items: center; margin-left: 30px">
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
      <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }"
                                v-show=" this.$router.currentRoute.path!=='/home'">
              首页
            </el-breadcrumb-item>
            <!--获取当前路由对象的指定属性-->
            <el-breadcrumb-item>{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
          </el-breadcrumb>
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
    }
  },
  mounted() {
  },
  methods: {
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
  margin-top: 10px;
  width: 100%;
  min-height: calc(100vh - 100px);
}

.homeHeader {
  background-color: #76d3e3;
  /* //弹性布局,设为Flex布局以后，子元素的float、clear和vertical-align属性将失效。*/
  display: flex;
  /* //居中对齐弹性盒的各项  元素：*/
  align-items: center;
  /*在弹性盒对象的  元素中的各项周围留有空白：*/
  justify-content: space-between;
  padding: 0px 15px;
  /*你想要设置的边框和内边距的值是包含在width内的*/
  box-sizing: border-box;
  /*向方框添加一个或多个阴影。*/
  /*box-shadow:0px 10px 10px -4px #409eff;*/
  font-family: 站酷庆科黄油体;
  height: 60px;
}

#title {
  font-size: 20px;
  color: #ffffff;
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
  color: #ffffff;
  font-weight: bolder;
  font-size: 15px;
  border: #ffffff;
}

.el-menu-horizontal {
  border-bottom: none;
  background-color: transparent;
}

.el-menu-horizontal .el-menu-item,
.el-menu-horizontal .el-submenu__title {
  color: #ffffff;
  height: 60px;
  line-height: 60px;
  border-bottom: none;
}
</style>
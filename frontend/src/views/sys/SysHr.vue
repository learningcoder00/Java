<template>
  <div>
    <!--弹性盒子元素在主轴（横轴）方向上的对齐方式。-->
    <div style="display: flex;justify-content: center;margin-top: 10px;align-items: center;">
      <el-input class="addPosInput"
                placeholder="请输入用户名进行搜索..."
                prefix-icon="el-icon-search"
                v-model="name"
                @keyup.enter.native="searchname">
      </el-input>
      <el-button icon="el-icon-search"
                 type="primary"
                 class="search-btn"
                 @click="searchname">搜索
      </el-button>
      <el-button type="primary"
                 icon="el-icon-plus"
                 class="search-btn"
                 style="margin-left: 10px"
                 @click="showAddHrView">
        添加用户
      </el-button>
    </div>

    <div class="center-right-infinite-lists"
         v-loading.fullscreen.lock="loading"
         element-loading-spinner="fa fa-spinner fa-pulse fa-3x fa-fw"
         style="margin-top: 10px">
      <el-scrollbar style="height: 100%">
        <div class="hr-container">
          <div class="hr-card"
               v-for="(hr, index) in hrs"
               :key="index">
            <div class="hr-card__header">
              <div class="hr-card__title">{{ hr.name }}</div>
              <el-button class="hr-card__delete-btn"
                         type="text"
                         icon="el-icon-delete"
                         @click="deletehr(hr)"></el-button>
            </div>
            <div class="hr-card__body">
              <div class="hr-card__avatar-wrapper">
                <el-image :src="hr.userface"
                          :alt="hr.name"
                          :title="hr.name"
                          class="hr-card__avatar"/>
                <div class="hr-card__status-tag" :class="hr.enabled ? 'is-active' : 'is-inactive'">
                  {{ hr.enabled ? '在线' : '离线' }}
                </div>
              </div>
              
              <div class="hr-card__info-list">
                <div class="info-item">
                  <i class="el-icon-user"></i>
                  <span class="info-label">用户名</span>
                  <span class="info-value">{{ hr.name }}</span>
                </div>
                <div class="info-item">
                  <i class="el-icon-mobile-phone"></i>
                  <span class="info-label">手机</span>
                  <span class="info-value">{{ hr.phone }}</span>
                </div>
                <div class="info-item">
                  <i class="el-icon-phone-outline"></i>
                  <span class="info-label">电话</span>
                  <span class="info-value">{{ hr.telephone }}</span>
                </div>
                <div class="info-item">
                  <i class="el-icon-location-outline"></i>
                  <span class="info-label">地址</span>
                  <span class="info-value">{{ hr.address }}</span>
                </div>
                <div class="info-item status-toggle">
                  <i class="el-icon-turn-off"></i>
                  <span class="info-label">账号状态</span>
                  <el-switch v-model="hr.enabled"
                             active-color="#13ce66"
                             inactive-color="#ff4949"
                             size="mini"
                             @change="updatenable(hr)"></el-switch>
                </div>
              </div>

              <div class="hr-card__roles">
                <div class="roles-header">
                  <span class="roles-title">用户角色</span>
                  <el-popover @show="showrol(hr)"
                              @after-leave="hiderol(hr)"
                              title="分配角色"
                              width="200"
                              trigger="click">
                    <el-select v-model="selectroles"
                               multiple
                               size="mini"
                               :popper-append-to-body="false"
                               placeholder="选择角色">
                      <el-option v-for="(item, index) in roles"
                                 :key="index"
                                 :label="item.namezh"
                                 :value="item.id">
                      </el-option>
                    </el-select>
                    <el-button slot="reference" 
                               type="text" 
                               icon="el-icon-edit-outline" 
                               class="role-edit-btn"></el-button>
                  </el-popover>
                </div>
                <div class="roles-tags">
                  <el-tag v-for="(role,indexj) in hr.roles"
                          size="mini"
                          :key="indexj"
                          type="primary"
                          class="role-tag">
                    {{ role.namezh }}
                  </el-tag>
                  <span v-if="!hr.roles || hr.roles.length === 0" class="no-role">暂无角色</span>
                </div>
              </div>
              
              <div class="hr-card__remark" v-if="hr.remark">
                <i class="el-icon-notebook-2"></i>
                <span class="remark-text">{{ hr.remark }}</span>
              </div>
            </div>
          </div>
        </div>
        <el-backtop target=".center-right-infinite-lists .el-scrollbar__wrap  "></el-backtop>

      </el-scrollbar>
    </div>
    <el-dialog :title="title"
               :visible.sync="dialogVisible"
               width="20%">
      <div style="margin-top: 20px;font-size: 16px;lineHeight:2">
        <div><el-input prefix-icon="el-icon-edit"
                       v-model="addHr.username"
                       placeholder="用户名"></el-input></div>
        <div><el-input prefix-icon="el-icon-edit"
                       v-model="addHr.name"
                       placeholder="昵称"></el-input></div>
        <div><el-input prefix-icon="el-icon-edit"
                       v-model="addHr.phone"
                       placeholder="手机号码"></el-input></div>
        <div><el-input prefix-icon="el-icon-edit"
                       v-model="addHr.telephone"
                       placeholder="电话号码"></el-input></div>
        <div><el-input prefix-icon="el-icon-edit"
                       v-model="addHr.address"
                       placeholder="地 址"></el-input></div>
        <div><el-input prefix-icon="el-icon-edit"
                       v-model="addHr.userface"
                       placeholder="头像地址"></el-input></div>
        <div>用户状态：
          <el-switch v-model="addHr.enabled"
                     active-color="#13ce66"
                     inactive-color="#ff4949"
          ></el-switch>
        </div>
        <div><el-input prefix-icon="el-icon-edit"
                       v-model="addHr.remark"
                       placeholder="备注"></el-input></div>
      </div>
      <span slot="footer"
            class="dialog-footer">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary"
                       @click="doAddHr">确 定</el-button>
          </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: '',
      hrs: [],
      roles: [],
      selectroles: [],
      loading: false,
      dialogVisible: false,
      title:'',
      addHr:  {
        name: '',
        username: '',
        phone: '',
        telephone: '',
        address: '',
        enabled: false,
        remark: '',
        userface: ''
      }
    }
  },
  mounted() {
    this.inithrs();
    this.initroles()
  },
  methods: {
    emptyHr() {
      this.addHr = {
        name: '',
        phone: '',
        username: '',
        telephone: '',
        address: '',
        enabled: false,
        remark: '',
        userface: ''
      }
    },
    doAddHr (){
      this.$notify.success({
        title: '添加讯息',
        message: '添 加 操 作 人 中...',
        showClose: false,
        offset: 100,
        duration: 1500,
        customClass: 'fontclass'
      });
      this.postRequest("/system/hr/add", this.addHr).then(resp => {
        if (resp) {
          this.dialogVisible = false;
          this.inithrs();
        }
      })
    },
    cancel(){
      this.dialogVisible = false;
      this.$notify.info({
        title: '修改讯息',
        message: '以 取 消 操作 ',
        showClose: false,
        offset: 100,
        duration: 1500,
        customClass: 'fontclass'
      });
    },
    showAddHrView() {
      this.emptyHr();
      this.title = '添加操作人';
      this.dialogVisible = true;
    },
    deletehr(hr) {
      this.$confirm('此操作将永久删除【' + hr.name + '】用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$notify.success({
          title: '删除讯息',
          message: '删 除 用 户 中...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclasssysuser'
        });
        this.deleteRequest("/system/hr/" + hr.id).then(resp => {
          if (resp) {
            this.initrps();
          }
          this.inithrs();
        })
      }).catch(() => {
        this.$notify.info({
          title: '删除讯息',
          message: '以 取 消 删 除 ',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclasssysuser'
        });
      });
    },
    searchname() {
      if (!this.name) {
        this.$notify({
          title: '搜索讯息',
          message: '搜 索 字 段 为 空!...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclasssysuser',
          type: 'warning'
        });
      }
      this.getRequest("/system/hr/?name=" + this.name).then(resp => {
        if (resp) {
          /*更新数据+清空列表*/
          this.hrs = resp;
          this.name = '';
        }
      });
    },

    updatenable(hr) {
      delete hr.roles;
      delete hr.authorities;
      this.$notify.success({
        title: '修改讯息',
        message: '用 户 信 息 修 改 中...',
        showClose: false,
        offset: 200,
        duration: 1500,
        customClass: 'fontclasssysuser'
      });
      this.putRequest("/system/hr/", hr).then(resp => {
        if (resp) {
          this.inithrs();
        }
      });
    },
    hiderol(hr) {
      let url = "/system/hr/role?" + "hrid=" + hr.id;
      this.selectroles.forEach(id => {
        url += "&rids=" + id;
      })
      url += "&rids";
      this.selectroles = null;
      this.$notify.success({
        title: '修改讯息',
        message: '用 户 信 息 修 改 中...',
        showClose: false,
        offset: 200,
        duration: 1500,
        customClass: 'fontclasssysuser'
      });
      this.putRequest(url).then(resp => {
        if (resp) {
          this.inithrs();
        }
      });
    },
    showrol(hr) {
      let roles = hr.roles;
      this.selectroles = [];
      roles.forEach(r => {
        this.selectroles.push(r.id);
      })
    },
    initroles() {
      this.getRequest("/system/hr/roles").then(resp => {
        if (resp) {
          this.roles = resp;
        }
      });
    },
    inithrs() {
      setTimeout(() => {
        this.$notify.success({
          title: '系统讯息',
          message: '用 户 信 息 加 载 中...',
          showClose: false,
          offset: 200,
          duration: 1500,
          customClass: 'fontclasssysuser'
        });
      }, 900)
      this.loading = true;
      this.getRequest("/system/hr/?name=" + this.name).then(resp => {
        if (resp) {
          this.hrs = resp;
          this.loading = false;
        }

      });
    }
  },
  watch: {
    hrs: function () {
      if (this.hrs.length == 0) {
        setTimeout(() => {
          this.loading = false;
          this.$notify.info({
            title: '系统讯息',
            message: '没 有 用 户 信 息...',
            showClose: false,
            offset: 100,
            duration: 1500,
            customClass: 'fontclasssysuser'
          });
        }, 1000);
      }
    }
  }
}
</script>

<style scoped>
.fontclasssysuser {
  font-family: 站酷庆科黄油体;
}

.center-right-infinite-lists {
  height: calc(100vh - 180px);
  width: 100%;
}

.hr-container {
  margin-top: 20px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 25px;
  padding: 0 20px 20px 20px;
}

/* 现代卡片设计 */
.hr-card {
  width: 320px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
}

.hr-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.hr-card__header {
  padding: 16px 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #ffffff 100%);
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hr-card__title {
  font-family: 站酷庆科黄油体;
  font-size: 22px;
  color: #303133;
}

.hr-card__delete-btn {
  color: #f56c6c;
  padding: 0;
  font-size: 18px;
}

.hr-card__body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.hr-card__avatar-wrapper {
  position: relative;
  width: 80px;
  margin: 0 auto;
}

.hr-card__avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.hr-card__status-tag {
  position: absolute;
  bottom: 0;
  right: -5px;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 10px;
  color: #fff;
  border: 2px solid #fff;
}

.hr-card__status-tag.is-active {
  background: #67c23a;
}

.hr-card__status-tag.is-inactive {
  background: #909399;
}

.hr-card__info-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
  gap: 8px;
}

.info-item i {
  color: #409eff;
  width: 16px;
}

.info-label {
  color: #909399;
  width: 60px;
  flex-shrink: 0;
  white-space: nowrap;
}

.info-value {
  color: #303133;
  word-break: break-all;
}

.status-toggle {
  margin-top: 4px;
}

.hr-card__roles {
  background: #f8fafc;
  border-radius: 8px;
  padding: 12px;
}

.roles-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.roles-title {
  font-size: 13px;
  font-weight: 600;
  color: #909399;
}

.role-edit-btn {
  padding: 0;
  font-size: 16px;
}

.roles-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.role-tag {
  border-radius: 4px;
}

.no-role {
  font-size: 12px;
  color: #c0c4cc;
  font-style: italic;
}

.hr-card__remark {
  display: flex;
  gap: 8px;
  padding-top: 8px;
  border-top: 1px dashed #ebeef5;
  font-size: 13px;
  color: #909399;
}

.hr-card__remark i {
  margin-top: 2px;
}

.remark-text {
  line-height: 1.4;
}

.addPosInput {
  width: 400px;
}

.el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>

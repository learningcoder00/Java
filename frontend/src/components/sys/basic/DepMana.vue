<template>

  <div style="width: 1100px;"
       class="mainclassstyle"
       v-loading.fullscreen.lock="loading"
       element-loading-spinner="fa fa-spinner fa-pulse fa-3x fa-fw">
    <div class="input_style">
      <el-input placeholder="请输入部门名称进行搜索..."
                prefix-icon="el-icon-search"
                v-model="filterText"
                style="margin-right: 15px">
        <el-button slot="append"
                   icon="el-icon-search"></el-button>
      </el-input>
      <el-tooltip content="部门树预览"
                  placement="right"
                  effect="light">
        <el-button type="success"
                   icon="el-icon-picture-outline-round"
                   @click="table = true"
                   circle
                   style="margin-left: 100px"></el-button>
      </el-tooltip>
    </div>
    <div class="center-right-infinite-list">
      <el-scrollbar style="height:100%">
        <div style="width: 1000px;overflow: visible">
          <el-tree style="width: 100%"
                   :data="deps"
                   :props="defaultProps"
                   :expand-on-click-node="false"
                   :filter-node-method="filterNode"
                   :default-expand-all="true"
                   :key="treeKey"
                   ref="tree">
            <!--node纸当前元素，data指当前元素的数据，子树数据-->
            <span class="custom-tree-node"
                  slot-scope="{ node, data }">
              <span class="dep-name">{{data.name }}</span>
              <div class="dep-actions">
                <el-button type="primary"
                           size="mini"
                           @click="() => showAddDepView(data)"
                           class="dep-action-btn">
                  添加部门
                </el-button>
                <el-button type="danger"
                           size="mini"
                           @click="() => deleteDep(data)"
                           class="dep-action-btn">
                  删除部门
                </el-button>
              </div>
            </span>
          </el-tree>
        </div>
      </el-scrollbar>
    </div>
    <el-dialog title="添加部门"
               :visible.sync="dialogVisible"
               width="30%">
      <div>
        <el-form :rules="rules">
          <el-input type="text"
                    class="updatePosInput"
                    placeholder="请输入部门..."
                    style="width: 400px"
                    v-model="dep.name"
                    @keydown.enter.native="doAddDep">
            <template slot="prepend">{{pname}} >></template>
          </el-input>
        </el-form>
      </div>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="outUpdate">取 消</el-button>
        <el-button type="primary"
                   @click="doAddDep">确 定</el-button>
      </span>
    </el-dialog>
    <el-drawer size="60%"
               direction="btt"
               custom-class="drawer"
              
               :visible.sync="table">
      <ve-tree :data="chartData"
               :settings="chartSettings"></ve-tree>
    </el-drawer>

  </div>

</template>

<script>

export default {
  name: "DepMana",

  data () {
    this.chartSettings = {
      seriesMap: {
        tree1: {
          height: '100%',
          width: '1400px',
          roam: true,
          top: "10",
          bottom: "10",
          initialTreeDepth: 8,
          leaves: {//最底部文字样式
            label: {
              normal: {
                position: 'bottom',

                verticalAlign: 'middle',
                align: 'left',
              },
            }
          },
          lineStyle: {//正常情况显示
            color: '#67C23A',
            width: 2,
          },
        }
      }
    }
    return {
      table: false,
      loading: false,
      chartData: {
        columns: ['name', 'value'],
        rows: [
          {
            name: 'tree1',
            value: this.deps
          }
        ]
      },
      dialogVisible: false,

      filterText: '',
      dep: {
        name: '',
        parentid: -1
      },
      pname: '',
      deps: [],
      treeKey: 0,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  /*全局数据监听*/
  watch: {
    filterText (val) {
      this.$refs.tree.filter(val);
    }
  },
  mounted () {
    this.initDeps();
  },
  methods: {
    outUpdate () {
      this.$notify.info({
        title: '修改讯息',
        message: '已 取 消 修 改...',
        showClose: false,
        offset: 100,
        duration: 1500,
        customClass: 'fontclass'
      });
      this.dialogVisible = false;
    },
    initDep () {
      this.dep = {
        name: '',
        parentid: -1
      }
      this.pname = '';
    },
    /*递归调用添加*/
    addDep2Deps (deps, dep) {
      for (let i = 0; i < deps.length; i++) {
        let d = deps[i];
        if (d.id == dep.parentid) {
          d.children = d.children.concat(dep);
          if (d.children.length > 0) {
            d.parent = true;
          }
          return;
        } else {
          this.addDep2Deps(d.children, dep);
        }
      }
    },
    doAddDep () {
      if (this.dep.name) {
        this.$notify.success({
          title: '添加讯息',
          message: '添 加 部 门 中...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
        this.postRequest("/system/basic/department/", this.dep).then(resp => {
          if (resp) {
            this.addDep2Deps(this.deps, resp.obj);
            this.dialogVisible = false;
            //初始化变量
            this.initDep();
            window.sessionStorage.removeItem("deps");
          }
        });
      } else {
        this.$notify({
          title: '添加讯息',
          message: '添 加 部 门 字 段 不 可 以 为 空...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass',
          type: 'warning'
        });
      }
    },
    removeDepFromDeps (p, deps, id) {
      for (let i = 0; i < deps.length; i++) {
        let d = deps[i];
        if (d.id == id) {
          deps.splice(i, 1);
          if (deps.length == 0) {
            p.parent = false;
          }
          return;
        } else {
          this.removeDepFromDeps(d, d.children, id);
        }
      }
    },
    deleteDep (data) {
      if (data.parent) {
        this.$notify({
          title: '删除讯息',
          message: '父 部 门 删 除 失 败',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass',
          type: "error"
        });
      } else {
        this.$confirm('此操作将永久删除【' + data.name + '】部门, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$notify.success({
            title: '删除讯息',
            message: '删 除 部 门 中...',
            showClose: false,
            offset: 100,
            duration: 1500,
            customClass: 'fontclass'
          });
          this.deleteRequest("/system/basic/department/" + data.id).then(resp => {
            if (resp) {
              this.removeDepFromDeps(null, this.deps, data.id);
            }
          })
        }).catch(() => {
          this.$notify.info({
            title: '删除讯息',
            message: '以 取 消 删 除...',
            showClose: false,
            offset: 100,
            duration: 1500,
            customClass: 'fontclass'
          });
        });
      }
    },
    showAddDepView (data) {
      this.pname = data.name;
      this.dep.parentid = data.id;
      this.dialogVisible = true;
    },
    /*初始数据加载*/
    initDeps () {
      this.loading = true;
      this.getRequest("/system/basic/department/").then(resp => {
        if (resp) {
          this.deps = resp;
          this.chartData.rows[0].value = this.deps;
          this.loading = false;
          this.treeKey++; // 强制重新渲染树及其插槽
          window.sessionStorage.setItem("deps", JSON.stringify(resp));
        }
      })
    },
    /*:filter-node-method="filterNode"属性调用的方法*/
    filterNode (value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    }
  }
}
</script>

<style>
.fontclass {
  font-size: 35px;
  font-family: 站酷庆科黄油体;
}
.el-drawer.drawer {
  border-radius: 15px 0px 0px 15px;
  box-shadow: dodgerblue;
  
}

.el-scrollbar__wrap {
  overflow: scroll;
  overflow-x: auto;
}

.center-right-infinite-list {
  width: 1000px;
  height: 690px;
}

.input_style {
  display: flex;
  align-items: center;
  margin: 0px 0px;
}

.mainclassstyle {
  width: 1500px;
}

.depBtn {
  padding: 2px;
}

/* 部门树节点：加大上下间距/行高（仅影响部门管理页） */
.mainclassstyle .el-tree-node__content {
  height: 44px;
  margin: 6px 0;
}

/* 自定义节点：左侧名称 + 右侧固定操作区，防止按钮被压扁 */
.mainclassstyle .custom-tree-node {
  display: flex;
  align-items: center;
  width: 100%;
  min-width: 0;
}

.mainclassstyle .dep-name {
  flex: 1 1 auto;
  min-width: 0;
  margin-left: 8px;
  padding-right: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 右侧操作区：加宽以完整显示“删除部门”，并增大按钮间距 */
.mainclassstyle .dep-actions {
  display: flex;
  gap: 14px;
  flex: 0 0 240px;
  justify-content: flex-end;
  align-items: center;
  z-index: 10;
  margin-left: 10px;
  margin-right: 10px;
}

.mainclassstyle .dep-action-btn {
  flex: 0 0 96px;
  height: var(--form-control-height);
  line-height: var(--form-control-height);
  padding: 0 10px;
  font-size: 12px;
  white-space: nowrap;
  box-sizing: border-box;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
</style>
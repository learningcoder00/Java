<template>
  <div>
    <div style="display: flex;justify-content: space-between">
      <el-button icon="el-icon-plus"
                 type="primary"
                 class="search-btn"
                 @click="showAddSalaryView">添加工资账套</el-button>
      <el-button icon="el-icon-refresh"
                 type="success"
                 class="search-btn"
                 @click="initSalaries">刷新</el-button>
    </div>
   
    <div style="margin-top: 10px">
      <el-table :data="salaries"
                border
                v-loading.fullscreen.lock="loading"
                element-loading-spinner="fa fa-spinner fa-pulse fa-3x fa-fw"
                stripe>
        <el-table-column type="selection"
                         width="55"></el-table-column>
        <el-table-column width="120"
                         prop="name"
                         label="账套名称"></el-table-column>
        <el-table-column width="100"
                         prop="basicsalary"
                         label="基本工资"></el-table-column>
        <el-table-column width="100"
                         prop="trafficsalary"
                         label="交通补助"></el-table-column>
        <el-table-column width="100"
                         prop="lunchsalary"
                         label="午餐补助"></el-table-column>
        <el-table-column width="100"
                         prop="bonus"
                         label="奖金"></el-table-column>
        <el-table-column width="100"
                         prop="createdate"
                         label="启用时间"></el-table-column>
        <el-table-column label="养老金"
                         align="center">
          <el-table-column width="70"
                           prop="pensionper"
                           label="比率"></el-table-column>
          <el-table-column width="70"
                           prop="pensionbase"
                           label="基数"></el-table-column>
        </el-table-column>
        <el-table-column label="医疗保险"
                         align="center">
          <el-table-column width="70"
                           prop="medicalper"
                           label="比率"></el-table-column>
          <el-table-column width="70"
                           prop="medicalbase"
                           label="基数"></el-table-column>
        </el-table-column>
        <el-table-column label="公积金"
                         align="center">
          <el-table-column width="70"
                           prop="accumulationfundper"
                           label="比率"></el-table-column>
          <el-table-column width="70"
                           prop="accumulationfundbase"
                           label="基数"></el-table-column>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini"
                       class="table-action-btn"
                       @click="showEditSalaryView(scope.row)">编辑</el-button>
            <el-button size="mini"
                       class="table-action-btn"
                       type="danger"
                       @click="deleteSalary(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog :title="dialogTitle"
               :visible.sync="dialogVisible"
               width="50%">
      <div style="display: flex;justify-content: space-around;align-items: center">
        <el-steps direction="vertical"
                  :active="activeItemIndex">
          <el-step :title="itemName"
                   v-for="(itemName,index) in salaryItemName"
                   :key="index"></el-step>
        </el-steps>
        <div v-for="(key, index) in salaryItemKey" :key="index">
          <el-input v-model="salary[key]"
                    :placeholder="'请输入'+salaryItemName[index]+'...'"
                    v-show="activeItemIndex==index"
                    style="width: 200px"></el-input>
        </div>
      </div>
      <span slot="footer"
            class="dialog-footer">
        <el-button class="sys-action-btn"
                   @click="preStep">{{ activeItemIndex == 11 ? '取消' : '上一步' }}</el-button>
        <el-button class="sys-action-btn"
                   type="primary"
                   @click="nextStep">{{ activeItemIndex == 11 ? '完成' : '下一步' }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SalSob",
  data () {
    return {
      rules: {
        basicsalary: [{ required: true, message: '不能为空', trigger: 'blur' }, { type: 'number', message: '必须为数字值' }],
        trafficsalary: [{ required: true, message: '不能为空', trigger: 'blur' }, { type: 'number', message: '必须为数字值' }],
        lunchsalary: [{ required: true, message: '不能为空', trigger: 'blur' }, { type: 'number', message: '必须为数字值' }],
        bonus: [{ required: true, message: '不能为空', trigger: 'blur' }, { type: 'number', message: '必须为数字值' }],
        pensionper: [{ required: true, message: '不能为空', trigger: 'blur' }],
        pensionbase: [{ required: true, message: '不能为空', trigger: 'blur' }, { type: 'number', message: '必须为数字值' }],
        medicalper: [{ required: true, message: '不能为空', trigger: 'blur' }],
        medicalbase: [{ required: true, message: '不能为空', trigger: 'blur' }, { type: 'number', message: '必须为数字值' }],
        accumulationfundper: [{ required: true, message: '不能为空', trigger: 'blur' }],
        accumulationfundbase: [{ required: true, message: '不能为空', trigger: 'blur' }, { type: 'number', message: '必须为数字值' }],
        name: [{ required: true, message: '不能为空', trigger: 'blur' }],
      },
      dialogVisible: false,
      loading: false,
      dialogTitle: '添加工资账套',
      salaries: [],
      activeItemIndex: 0,
      salaryItemKey: [
        'basicsalary',
        'trafficsalary',
        'lunchsalary',
        'bonus',
        'pensionper',
        'pensionbase',
        'medicalper',
        'medicalbase',
        'accumulationfundper',
        'accumulationfundbase',
        'name'
      ],
      salaryItemName: [
        '基本工资',
        '交通补助',
        '午餐补助',
        '奖金',
        '养老金比率',
        '养老金基数',
        '医疗保险比率',
        '医疗保险基数',
        '公积金比率',
        '公积金基数',
        '账套名称'
      ],
      salary: {
        basicsalary: 0,
        trafficsalary: 0,
        lunchsalary: 0,
        bonus: 0,
        pensionper: 0,
        pensionbase: 0,
        medicalper: 0,
        medicalbase: 0,
        accumulationfundper: 0,
        accumulationfundbase: 0,
        name: ''
      }
    }
  },
  mounted () {
    this.initSalaries();
  },
  methods: {
    showEditSalaryView (data) {
      this.dialogTitle = '修改工资账套';
      this.activeItemIndex = 0;
      this.salary = Object.assign({}, data);
      this.dialogVisible = true;
    },
    deleteSalary (data) {
      this.$confirm('此操作将删除【' + data.name + '】账套，是否继续？', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定'
      }).then(() => {
        this.deleteRequest("/salary/sob/" + data.id).then(resp => {
          if (resp) {
            this.initSalaries();
          }
        })
      }).catch(() => {
        this.$notify.info({
          title: '删除讯息',
          message: '删 除 取 消',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
      })
    },
    preStep () {
      if (this.activeItemIndex == 0) {
        return;
      } else if (this.activeItemIndex == 11) {
        //关闭对话框
        this.dialogVisible = false;
        return;
      }
      this.activeItemIndex--;
    },
    nextStep () {
      if (this.activeItemIndex == 11) {
        if (this.salary.id) {
          this.$notify.success({
            title: '修改讯息',
            message: ' 修 改 套 账 中...',
            showClose: false,
            offset: 100,
            duration: 1500,
            customClass: 'fontclass'
          });
          this.putRequest("/salary/sob/", this.salary).then(resp => {
            if (resp) {
              this.initSalaries();
              this.dialogVisible = false;
            }
          })
        } else {
          this.$notify.success({
            title: '添加讯息',
            message: '添 加 工 资 套 账 中...',
            showClose: false,
            offset: 100,
            duration: 1500,
            customClass: 'fontclass'
          });
          this.postRequest("/salary/sob/", this.salary).then(resp => {
            if (resp) {
              this.initSalaries();
              this.dialogVisible = false;
            }
          });
        }
        return;
      }
      this.activeItemIndex++;
    },
    showAddSalaryView () {
      //数据初始化
      this.salary = {
        basicsalary: 0,
        trafficsalary: 0,
        lunchsalary: 0,
        bonus: 0,
        pensionper: 0,
        pensionbase: 0,
        medicalper: 0,
        medicalbase: 0,
        accumulationfundper: 0,
        accumulationfundbase: 0,
        name: '',
      }
      this.dialogTitle = '添加工资账套';
      this.activeItemIndex = 0;
      this.dialogVisible = true;
    },
    initSalaries () {
      setTimeout(() => {
        this.$notify.success({
          title: '系统讯息',
          message: '工 资 套 账 信 息 加 载 中...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
      }, 1100);
      this.loading = true;
      this.getRequest("/salary/sob/").then(resp => {
        if (resp) {
          this.salaries = resp;
          this.loading = false;
        }
      })
    }
  }
}
</script>

<style >
.fontclass {
  font-family: 站酷庆科黄油体;
}
</style>
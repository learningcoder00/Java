<template>
  <div>
    <div class="top-style">
      <div class="search-button-group" style="display: flex;justify-content: flex-start; margin-left: 40px;">
        <el-input placeholder="请输入员工名进行搜索，可以直接回车搜索..."
                  prefix-icon="el-icon-search"
                  clearable
                  @clear="initEmps"
                  style="width: 350px;margin-right: 10px"
                  v-model="keyword"
                  @keydown.enter.native="initEmps"></el-input>
        <el-button icon="el-icon-search"
                   type="primary"
                   @click="initEmps">
          搜索
        </el-button>
      </div>

      <div class="action-button-group" style="display: flex;justify-content: flex-end; margin-right: 40px;">
        <el-upload :show-file-list="false"
                   :before-upload="beforeUpload"
                   :on-success="onSuccess"
                   :on-error="onError"
                   :disabled="importDataDisabled"
                   style="display: inline-flex"
                   action="/employee/basic/import">
          <el-button :disabled="importDataDisabled"
                     type="success"
                     :icon="importDataBtnIcon">
            {{ importDataBtnText }}
          </el-button>
        </el-upload>
        <el-button type="success"
                   @click="exportData"
                   icon="el-icon-download">
          导出数据
        </el-button>
        <el-button type="primary"
                   icon="el-icon-plus"
                   @click="showAddEmpView">
          添加用户
        </el-button>
        <!--add 弹窗-->
        <el-dialog :title="title"
                   :visible.sync="dialogVisible"
                   width="85%"
                   top="4vh"
                   :close-on-click-modal="false"
                   custom-class="employee-dialog">
          <div class="employee-form-container">
            <el-form :model="emp"
                     :rules="rules"
                     ref="empForm"
                     label-width="110px"
                     label-position="right">
              <!-- 基本信息 -->
              <div class="form-section">
                <div class="section-title">
                  <i class="el-icon-user"></i>
                  <span>基本信息</span>
                </div>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="姓名" prop="name">
                      <el-input v-model="emp.name"
                                prefix-icon="el-icon-user"
                                placeholder="请输入员工姓名"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="性别" prop="gender">
                      <el-radio-group v-model="emp.gender">
                        <el-radio label="男">男</el-radio>
                        <el-radio label="女">女</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="出生日期" prop="birthday">
                      <el-date-picker v-model="emp.birthday"
                                      type="date"
                                      value-format="yyyy-MM-dd"
                                      placeholder="选择出生日期"
                                      style="width: 100%"></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="身份证号" prop="idcard">
                      <el-input v-model="emp.idcard"
                                prefix-icon="el-icon-postcard"
                                placeholder="请输入身份证号码"
                                maxlength="18"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="民族" prop="nationid">
                      <el-select v-model="emp.nationid"
                                   placeholder="请选择民族"
                                   style="width: 100%"
                                   filterable>
                        <el-option v-for="item in nations"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="籍贯" prop="nativeplace">
                      <el-input v-model="emp.nativeplace"
                                prefix-icon="el-icon-location"
                                placeholder="请输入籍贯"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="政治面貌" prop="politicid">
                      <el-select v-model="emp.politicid"
                                 placeholder="请选择政治面貌"
                                 style="width: 100%"
                                 filterable>
                        <el-option v-for="item in politicsstatus"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="电子邮箱" prop="email">
                      <el-input v-model="emp.email"
                                prefix-icon="el-icon-message"
                                placeholder="请输入电子邮箱"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="电话号码" prop="phone">
                      <el-input v-model="emp.phone"
                                prefix-icon="el-icon-phone"
                                placeholder="请输入电话号码"
                                maxlength="11"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="24">
                    <el-form-item label="联系地址" prop="address">
                      <el-input v-model="emp.address"
                                prefix-icon="el-icon-location-outline"
                                placeholder="请输入联系地址"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>

              <!-- 工作信息 -->
              <div class="form-section">
                <div class="section-title">
                  <i class="el-icon-briefcase"></i>
                  <span>工作信息</span>
                </div>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="工号" prop="workid">
                      <el-input v-model="emp.workid"
                                prefix-icon="el-icon-edit"
                                placeholder="工号"
                                disabled></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="所属部门" prop="departmentid">
                      <el-popover placement="right"
                                  title="请选择部门"
                                  width="250"
                                  trigger="manual"
                                  v-model="popVisible">
                        <el-tree default-expand-all
                                 :data="allDeps"
                                 :props="defaultProps"
                                 @node-click="handleNodeClick"></el-tree>
                        <el-input slot="reference"
                                  v-model="inputDepName"
                                  placeholder="请选择部门"
                                  readonly
                                  @click.native="showDepView1()"
                                  @focus="showDepView1()"
                                  style="cursor: pointer;">
                          <i slot="prefix" class="el-icon-office-building"></i>
                        </el-input>
                      </el-popover>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="职位" prop="posid">
                      <el-select v-model="emp.posid"
                                 placeholder="请选择职位"
                                 style="width: 100%"
                                 filterable>
                        <el-option v-for="item in positions"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="职称" prop="joblevelid">
                      <el-select v-model="emp.joblevelid"
                                 placeholder="请选择职称"
                                 style="width: 100%"
                                 filterable>
                        <el-option v-for="item in joblevels"
                                   :key="item.id"
                                   :label="item.name"
                                   :value="item.id">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="聘用形式" prop="engageform">
                      <el-radio-group v-model="emp.engageform">
                        <el-radio label="劳动合同">劳动合同</el-radio>
                        <el-radio label="劳务合同">劳务合同</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="入职日期" prop="begindate">
                      <el-date-picker v-model="emp.begindate"
                                      type="date"
                                      value-format="yyyy-MM-dd"
                                      placeholder="选择入职日期"
                                      style="width: 100%"></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="转正日期" prop="conversiontime">
                      <el-date-picker v-model="emp.conversiontime"
                                      type="date"
                                      value-format="yyyy-MM-dd"
                                      placeholder="选择转正日期"
                                      style="width: 100%"></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>

              <!-- 教育信息 -->
              <div class="form-section">
                <div class="section-title">
                  <i class="el-icon-reading"></i>
                  <span>教育信息</span>
                </div>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="学历" prop="tiptopdegree">
                      <el-select v-model="emp.tiptopdegree"
                                 placeholder="请选择学历"
                                 style="width: 100%">
                        <el-option v-for="item in tiptopDegrees"
                                   :key="item"
                                   :label="item"
                                   :value="item">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="毕业院校" prop="school">
                      <el-input v-model="emp.school"
                                prefix-icon="el-icon-school"
                                placeholder="请输入毕业院校名称"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="专业名称" prop="specialty">
                      <el-input v-model="emp.specialty"
                                prefix-icon="el-icon-document"
                                placeholder="请输入专业名称"
                                clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>

              <!-- 合同信息 -->
              <div class="form-section">
                <div class="section-title">
                  <i class="el-icon-document"></i>
                  <span>合同信息</span>
                </div>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="合同起始日期" prop="begincontract">
                      <el-date-picker v-model="emp.begincontract"
                                      type="date"
                                      value-format="yyyy-MM-dd"
                                      placeholder="选择合同起始日期"
                                      style="width: 100%"></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="合同终止日期" prop="endcontract">
                      <el-date-picker v-model="emp.endcontract"
                                      type="date"
                                      value-format="yyyy-MM-dd"
                                      placeholder="选择合同终止日期"
                                      style="width: 100%"></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </el-form>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button @click="quxiao" size="medium">取 消</el-button>
            <el-button type="primary" @click="doAddEmp" size="medium">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>

    <div class="content-style">
      <div>
        <el-table :data="emps"
                  stripe
                  border
                  v-loading.fullscreen.lock="loading"
                  element-loading-spinner="fa fa-spinner fa-pulse fa-3x fa-fw"
                  :customClass="loadingstyle"
                  style="width: 100%"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection"
                             width="55">
            </el-table-column>
            <el-table-column fixed
                             prop="name"
                             align="center"
                             label="姓名"
                             width="100">
            </el-table-column>
            <el-table-column prop="workid"
                             fixed
                             label="工号"
                             align="center"
                             width="100">
            </el-table-column>
            <el-table-column prop="gender"
                             label="性别"
                             align="center"
                             width="50">
            </el-table-column>
            <el-table-column prop="birthday"
                             width="95"
                             align="center"
                             label="出生日期">
            </el-table-column>
            <el-table-column prop="idcard"
                             width="200"
                             align="center"
                             label="身份证号码">
            </el-table-column>
            <el-table-column prop="wedlock"
                             width="100"
                             align="center"
                             label="婚姻状况">
            </el-table-column>
            <el-table-column prop="nation.name"
                             width="75"
                             align="center"
                             label="民族">
            </el-table-column>
            <el-table-column prop="nativeplace"
                             width="100"
                             align="center"
                             label="籍贯">
            </el-table-column>
            <el-table-column prop="politicsstatus.name"
                             align="center"
                             label="政治面貌">
            </el-table-column>
            <el-table-column prop="email"
                             width="180"
                             align="center"
                             label="电子邮件">
            </el-table-column>
            <el-table-column prop="phone"
                             width="120"
                             align="center"
                             label="电话号码">
            </el-table-column>
            <el-table-column prop="address"
                             width="250"
                             align="center"
                             label="联系地址">
            </el-table-column>
            <el-table-column prop="department.name"
                             width="100"
                             align="center"
                             label="所属部门">
            </el-table-column>
            <el-table-column prop="position.name"
                             width="100"
                             align="center"
                             label="职位">
            </el-table-column>
            <el-table-column prop="jobLevel.name"
                             width="100"
                             align="center"
                             label="职称">
            </el-table-column>
            <el-table-column prop="engageform"
                             width="100"
                             align="center"
                             label="聘用形式">
            </el-table-column>
            <el-table-column prop="tiptopdegree"
                             width="80"
                             align="center"
                             label="最高学历">
            </el-table-column>
            <el-table-column prop="specialty"
                             width="150"
                             align="center"
                             label="专业">
            </el-table-column>
            <el-table-column prop="school"
                             width="150"
                             align="center"
                             label="毕业院校">
            </el-table-column>
            <el-table-column prop="begindate"
                             width="95"
                             align="center"
                             label="入职日期">
            </el-table-column>
            <el-table-column prop="conversiontime"
                             width="95"
                             align="center"
                             label="转正日期">
            </el-table-column>
            <el-table-column prop="begincontract"
                             width="125"
                             align="center"
                             label="合同起始日期">
            </el-table-column>
            <el-table-column prop="endcontract"
                             width="125"
                             align="center"
                             label="合同截止日期">
            </el-table-column>
            <el-table-column width="100"
                             align="center"
                             label="合同期限">
              <template slot-scope="scope">
                <el-tag size="mini">{{ scope.row.contractterm }}</el-tag>
                年
              </template>
            </el-table-column>
            <el-table-column fixed="right"
                             width="150"
                             align="center"
                             label="操作">
              <template slot-scope="scope">
                <el-button
                    @click="showEditEmpView(scope.row)"
                    style="padding: 3px">编辑
                </el-button>
                <el-button
                    @click="deleteEmp(scope.row)"
                    style="padding: 3px"
                    type="danger">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
      </div>
    </div>
    <div class="bottom-style">
      <el-button type="danger"
                 style="margin-top: 10px"
                 :disabled="multipleSelection.length==0"
                 @click="deleteMany">批量删除
      </el-button>
      <el-pagination style="margin-top: 10px"
                     background
                     @current-change="currentChange"
                     @size-change="sizeChange"
                     layout="sizes, prev, pager, next, jumper, ->, total, slot"
                     :total="total"
                     :page-sizes="[13,33,333,,3333]">
      </el-pagination>

    </div>
    <!--添加弹出框-->

  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    return {
      multipleSelection: [],
      title: '',
      importDataBtnText: '导入数据',
      importDataBtnIcon: 'el-icon-upload2',
      importDataDisabled: false,
      emps: [],
      loading: false,
      popVisible: false,
      dialogVisible: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 13,
      nations: [],
      joblevels: [],
      politicsstatus: [],
      positions: [],
      tiptopDegrees: ['本科', '大专', '硕士', '博士', '高中', '初中', '小学', '其他'],
      options: [{
        value: '',
        label: ''
      }],
      inputDepName: '所属部门',
      emp: {
        id: '',
        name: "小明",
        gender: "男",
        birthday: "1995-10-08",
        idcard: "610122199001011256",
        wedlock: "已婚",
        nationid: 1,
        nativeplace: "内蒙古",
        politicid: 13,
        email: "1224965096@qq.com",
        phone: "18565558897",
        address: "内蒙古呼和浩特",
        departmentid: 29,
        joblevelid: "",
        posid: 29,
        engageform: "劳务合同",
        tiptopdegree: "本科",
        specialty: "信息与计算科学",
        school: "集宁师范学院",
        begindate: "2017-12-31",
        workstate: "在职",
        workid: "",
        contractterm: 2,
        conversiontime: "2018-03-31",
        notworkdate: null,
        begincontract: "2017-12-31",
        endcontract: "2019-12-31",
        workage: null
      },
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      rules: {
        name: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        gender: [{required: true, message: '请输入性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请输入出生日期', trigger: 'blur'}],
        idcard: [{required: true, message: '请输入身份证号码', trigger: 'blur'}, {
          pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
          message: '身份证号码格式不正确',
          trigger: 'blur'
        }],
        wedlock: [{required: true, message: '请输入婚姻状况', trigger: 'blur'}],
        nationid: [{required: true, message: '请输入民族', trigger: 'blur'}],
        // 注意：这里的 key 必须和 el-form-item 的 prop / emp 字段名一致
        nativeplace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
        politicid: [{required: true, message: '请输入政治面貌', trigger: 'blur'}],
        email: [
          {
            required: true,
            message: '请输入邮箱地址',
            trigger: 'blur',
            transform: (v) => (v || '').trim()
          },
          {
            // 用正则更可控，避免 type:'email' 因为前后空格等原因误判
            pattern: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
            message: '邮箱格式不正确',
            trigger: 'blur',
            transform: (v) => (v || '').trim()
          }
        ],
        phone: [{required: true, message: '请输入电话号码', trigger: 'blur'}],
        address: [{required: true, message: '请输入员工地址', trigger: 'blur'}],
        departmentid: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
        joblevelid: [{required: true, message: '请输入职称', trigger: 'blur'}],
        posid: [{required: true, message: '请输入职位', trigger: 'blur'}],
        engageform: [{required: true, message: '请输入聘用形式', trigger: 'blur'}],
        tiptopdegree: [{required: true, message: '请输入学历', trigger: 'blur'}],
        specialty: [{required: true, message: '请输入专业', trigger: 'blur'}],
        school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
        begindate: [{required: true, message: '请输入入职日期', trigger: 'blur'}],
        workstate: [{required: true, message: '请输入工作状态', trigger: 'blur'}],
        workid: [{required: true, message: '请输入工号', trigger: 'blur'}],
        contractterm: [{required: true, message: '请输入合同期限', trigger: 'blur'}],
        conversiontime: [{required: true, message: '请输入转正日期', trigger: 'blur'}],
        notworkdate: [{required: true, message: '请输入离职日期', trigger: 'blur'}],
        begincontract: [{required: true, message: '请输入合同起始日期', trigger: 'blur'}],
        endcontract: [{required: true, message: '请输入合同结束日期', trigger: 'blur'}],
        workage: [{required: true, message: '请输入工龄', trigger: 'blur'}],
      }
    }
  },
  mounted() {
    this.initEmps();
    this.initData();
    this.initPositions();
  },
  methods: {
    quxiao() {
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
    deleteMany() {
      this.$confirm('此操作将永久删除【' + this.multipleSelection.length + '】条记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$notify.success({
          title: '删除讯息',
          message: '删 除 员 工 中...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
        let ids = '?';
        this.multipleSelection.forEach(item => {
          ids += 'ids=' + item.id + '&';
        })
        this.deleteRequest("/employee/basic/many/" + ids).then(resp => {
          if (resp) {
            this.initEmps();
          }
        })
      }).catch(() => {
        this.$notify.info({
          title: '删除讯息',
          message: '以 取 消 删 除 ',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /*上传失败*/
    onError() {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
    },
    /*上传成功*/
    onSuccess() {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
      this.$notify.success({
        title: '上传讯息',
        message: '上传成功...',
        showClose: false,
        offset: 100,
        duration: 1500,
        customClass: 'fontclass'
      });
      this.initEmps();
    },
    /*上传中*/
    beforeUpload() {
      this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'fa fa-circle-o-notch fa-spin  fa-fw';
      this.importDataDisabled = true;
    },
    exportData() {
      window.open('/employee/basic/export', '_parent');
    },
    emptyEmp() {
      this.emp = {
        id: '',
        name: "",
        gender: "男",
        birthday: "1998-10-08",
        idcard: "",
        wedlock: "未婚",
        nationid: 1,
        nativeplace: "",
        politicid: 3,
        email: "",
        phone: "",
        address: "",
        departmentid: 8,
        joblevelid: "",
        posid: "",
        engageform: "",
        tiptopdegree: "",
        specialty: "",
        school: "",
        begindate: "2023-02-31",
        workstate: "在职",
        workid: '',
        contractterm: 2,
        conversiontime: "2023-02-31",
        notworkdate: null,
        begincontract: "2023-02-31",
        endcontract: "2023-02-31",
        workage: null
      }
      this.inputDepName = '';
    },
    showEditEmpView(data) {
      this.initPositions();
      this.title = '编辑员工信息';
      this.emp = data;
      this.inputDepName = data.department.name;
      this.dialogVisible = true;
    },
    deleteEmp(data) {
      this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$notify.success({
          title: '删除讯息',
          message: '删 除 员 工 中...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
        this.deleteRequest("/employee/basic/" + data.id).then(resp => {
          if (resp) {
            this.initEmps();
          }
        })
      }).catch(() => {
        this.$notify.info({
          title: '删除讯息',
          message: '以 取 消 删 除 ',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
      });
    },
    doAddEmp() {
      if (this.emp.id) {
        this.$refs['empForm'].validate(valid => {
          if (valid) {
            this.$notify.success({
              title: '修改讯息',
              message: ' 修 改 员 工 中...',
              showClose: false,
              offset: 100,
              duration: 1500,
              customClass: 'fontclass'
            });
            this.putRequest("/employee/basic/", this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        });
      } else {
        this.$refs['empForm'].validate(valid => {
          if (valid) {
            this.$notify.success({
              title: '添加讯息',
              message: '添 加 员 工 中...',
              showClose: false,
              offset: 100,
              duration: 1500,
              customClass: 'fontclass'
            });
            this.postRequest("/employee/basic/", this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        });
      }
    },
    handleNodeClick(data) {
      this.inputDepName = data.name;
      this.emp.departmentid = data.id;
      this.popVisible = !this.popVisible
    },
    showDepView1() {
      this.popVisible = !this.popVisible
    },
    /**/
    initPositions() {
      this.getRequest('/employee/basic/positions').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })
    },
    getMaxWordID() {
      this.getRequest("/employee/basic/maxWorkID").then(resp => {
        if (resp) {
          this.emp.workid = resp.obj;
          if (this.emp.workid <= 0){
            this.emp.workid = randomNum(1000, 10000);
          }
          return resp.obj;
        }
      })
    },
    randomNum(minNum, maxNum) {
      switch (arguments.length) {
        case 1:
          return parseInt(Math.random() * minNum + 1, 10);
        case 2:
          return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);
        default:
          return 0;
      }
    },
    /*添加员工数据预加载*/
    initData() {
      /*如果缓存了在缓存里取*/
      /*民族*/
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest('/employee/basic/nations').then(resp => {
          if (resp) {
            this.nations = resp;
            window.sessionStorage.setItem("nations", JSON.stringify(resp));
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
      }
      /*职称*/
      if (!window.sessionStorage.getItem("joblevels")) {
        this.getRequest('/employee/basic/joblevels').then(resp => {
          if (resp) {
            this.joblevels = resp;
            window.sessionStorage.setItem("joblevels", JSON.stringify(resp));
          }
        })
      } else {
        this.joblevels = JSON.parse(window.sessionStorage.getItem("joblevels"));
      }
      /*政治面貌*/
      if (!window.sessionStorage.getItem("politicsstatus")) {
        this.getRequest('/employee/basic/politicsstatus').then(resp => {
          if (resp) {
            this.politicsstatus = resp;
            window.sessionStorage.setItem("politicsstatus", JSON.stringify(resp));
          }
        })
      } else {
        this.politicsstatus = JSON.parse(window.sessionStorage.getItem("politicsstatus"));
      }
      /*所在部门*/
      if (!window.sessionStorage.getItem("deps")) {
        this.getRequest('/employee/basic/deps').then(resp => {
          if (resp) {
            this.allDeps = resp;
            window.sessionStorage.setItem("deps", JSON.stringify(resp));
          }
        })
      } else {
        this.allDeps = JSON.parse(window.sessionStorage.getItem("deps"));
      }
    },
    /*分页事件处理*/
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps();
    },
    showAddEmpView() {
      this.emptyEmp();
      this.title = '添加员工';
      this.getMaxWordID();
      this.dialogVisible = true;
    },
    /*初始化搜索处理*/
    initEmps() {
      this.loading = true;
      let url = '/employee/basic/?page=' + this.page + '&size=' + this.size;
      if (this.keyword) {
        this.$notify.success({
          title: '搜索讯息',
          message: '搜 索 员 工 中...',
          showClose: false,
          offset: 100,
          duration: 1500,
          customClass: 'fontclass'
        });
        url += "&name=" + this.keyword;
      }
      /*数据 返回*/
      this.$notify.success({
        title: '系统讯息',
        message: '基 本 信 息 加 载 中...',
        showClose: false,
        offset: 100,
        duration: 1500,
        customClass: 'fontclass'
      });
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          /*总记录数*/
          this.total = resp.total;
          this.keyword = '';
        }
      });
    }
  }
}
</script>

<style scoped>
.top-style {
  /*定义弹性布局*/
  display: flex;
  /*分布方式*/
  justify-content: space-between;
  margin-top: 20px;
}

.content-style {
  margin-top: 10px;
}

.content-style .el-scrollbar {
  overflow: hidden;
}

.content-style .el-scrollbar__wrap {
  overflow: hidden !important;
}

.bottom-style {
  display: flex;
  justify-content: space-between;
}

.fontclasssys {
  font-family: 站酷庆科黄油体;
}

.loadingstyle {
  font-family: 站酷庆科黄油体;
  font-size: 25px;
}

/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}

/* 员工表单样式优化 */
.employee-form-container {
  padding: 10px 0;
  max-height: 60vh;
  overflow-y: auto;
  margin-bottom: 10px;
}

.employee-form-container::-webkit-scrollbar {
  width: 6px;
}

.employee-form-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.employee-form-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.employee-form-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.form-section {
  margin-bottom: 30px;
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
  transition: all 0.3s ease;
}

.form-section:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
}

.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #409eff;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.section-title i {
  margin-right: 8px;
  font-size: 18px;
  color: #409eff;
}

.employee-form-container .el-form-item {
  margin-bottom: 22px;
}

.employee-form-container .el-form-item__label {
  font-weight: 500;
  color: #606266;
  white-space: nowrap;
  overflow: visible;
  text-overflow: ellipsis;
  line-height: 40px;
}

/* 统一所有输入控件的高度 */
.employee-form-container .el-input__inner,
.employee-form-container .el-textarea__inner {
  height: 40px;
  line-height: 40px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.employee-form-container .el-input__inner:focus,
.employee-form-container .el-textarea__inner:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

/* 输入框前缀图标垂直居中 - 与文字对齐 */
.employee-form-container .el-input__prefix {
  height: 40px;
  line-height: 40px;
  top: 0;
  left: 0;
  display: table-cell;
  vertical-align: middle;
}

.employee-form-container .el-input__prefix i {
  line-height: 40px;
  display: inline-block;
  vertical-align: middle;
  font-size: 16px;
}

/* 输入框文字垂直居中 - 与图标对齐 */
.employee-form-container .el-input__inner input {
  height: 40px;
  line-height: 40px;
  vertical-align: middle;
}

/* 确保只读输入框可以点击（用于部门选择） */
.employee-form-container .el-input.is-disabled .el-input__inner,
.employee-form-container .el-input[readonly] .el-input__inner {
  cursor: pointer;
  pointer-events: auto;
}

.employee-form-container .el-input[readonly] {
  cursor: pointer;
}

.employee-form-container .el-input[readonly] .el-input__inner {
  cursor: pointer;
  background-color: #fff;
}

/* 统一下拉框高度 */
.employee-form-container .el-select {
  width: 100%;
}

.employee-form-container .el-select .el-input__inner {
  height: 40px;
  line-height: 40px;
}

.employee-form-container .el-select .el-input__suffix {
  line-height: 40px;
  height: 40px;
  top: 0;
  display: table-cell;
  vertical-align: middle;
}

.employee-form-container .el-select .el-input__suffix i {
  line-height: 40px;
  display: inline-block;
  vertical-align: middle;
}

/* 统一日期选择器高度 */
.employee-form-container .el-date-editor {
  width: 100%;
}

.employee-form-container .el-date-editor.el-input {
  width: 100%;
}

.employee-form-container .el-date-editor .el-input__inner {
  height: 40px;
  line-height: 40px;
}

.employee-form-container .el-date-editor .el-input__prefix,
.employee-form-container .el-date-editor .el-input__suffix {
  line-height: 40px;
  height: 40px;
  top: 0;
  display: table-cell;
  vertical-align: middle;
}

.employee-form-container .el-date-editor .el-input__prefix i,
.employee-form-container .el-date-editor .el-input__suffix i {
  line-height: 40px;
  display: inline-block;
  vertical-align: middle;
}

/* 统一表单项内容区域高度 */
.employee-form-container .el-form-item__content {
  min-height: 40px;
  display: flex;
  align-items: center;
  line-height: 40px;
}

/* 单选按钮组垂直居中对齐 */
.employee-form-container .el-radio-group {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  height: 40px;
  line-height: 40px;
}

.employee-form-container .el-radio {
  margin-right: 20px;
  margin-bottom: 0;
  line-height: 40px;
  height: 40px;
  display: flex;
  align-items: center;
}

.employee-form-container .el-radio__label {
  line-height: 40px;
  padding-left: 8px;
}

.employee-form-container .el-radio__input {
  line-height: 40px;
  vertical-align: middle;
}

.employee-form-container .el-radio__inner {
  vertical-align: middle;
}

/* 对话框底部按钮区域 */
.employee-form-container + .el-dialog__footer,
.el-dialog__body + .el-dialog__footer {
  padding: 15px 20px !important;
  border-top: 1px solid #e8e8e8;
  margin-top: 10px;
}

.dialog-footer {
  text-align: right;
  padding: 12px 0;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
  width: 100%;
  box-sizing: border-box;
}

.dialog-footer .el-button {
  padding: 10px 25px;
  font-size: 14px;
  min-width: 80px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 对话框整体样式优化 */
.employee-dialog {
  display: flex;
  flex-direction: column;
}

.employee-dialog .el-dialog__body {
  padding: 20px;
  max-height: calc(80vh - 140px);
  overflow: hidden;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.employee-dialog .el-dialog__footer {
  padding: 10px 20px !important;
  border-top: 1px solid #e8e8e8;
  position: relative;
  z-index: 10;
  flex-shrink: 0;
  margin-top: auto;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .employee-form-container .el-col {
    margin-bottom: 10px;
  }
}
</style>

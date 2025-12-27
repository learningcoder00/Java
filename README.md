## 🚀 项目概览

本项目采用前后端分离架构，提供了从基础信息录入、复杂的奖惩培训跟踪，到自动化的薪资核算以及深度的数据分析报表等全方位功能。

### 核心功能模块

- **员工管理**：包含基础信息、高级资料（入职日期、职位职称等多维搜索）、奖惩管理、培训管理及绩效评估。
- **薪资管理**：支持工资账套配置、员工账套设置、工资表查询及数据导出（Excel）。
- **部门管理**：动态树形结构展示部门层级，支持快速添加、删除及平滑渲染。
- **数据统计**：
  - **人事信息统计**：通过精美的饼状图展示政治面貌、民族、职称、部门等分布情况。
  - **人事记录分析**：提供柱状图/折线图，结合 KPI 指标概览，深度挖掘人事变动趋势。
- **系统管理**：操作员权限分配、日志审计（操作记录垂直对齐展示）、基础配置（职位、职称、奖惩规则）。
- **实时沟通**：内置基于 WebSocket 的聊天功能，方便企业内部即时通讯。

## 🛠 技术栈

### 后端 (Spring Boot)
- **核心框架**：Spring Boot 2.2.2.RELEASE
- **安全认证**：Spring Security (基于角色访问控制)
- **持久层**：MyBatis + MySQL 8.0
- **数据库连接池**：Alibaba Druid
- **实时通讯**：WebSocket + STOMP
- **其他**：POI (Excel 导入导出), Easy-captcha (图形验证码)

### 前端 (Vue.js)
- **框架**：Vue 2.6 + Vuex + Vue Router
- **UI 组件库**：Element UI
- **数据可视化**：ECharts + V-Charts (包含自定义渐变美化组件)
- **样式方案**：Sass + 全局 CSS 变量 (统一 UI 规范)
- **工具库**：Axios (网络请求), Font-Awesome (图标)

## ✨ 近期 UI/UX 优化亮点

- **全局样式统一**：通过 `global.css` 定义统一的控件高度（34px）和排版对齐规则，确保搜索栏在不同分辨率下完美垂直居中。
- **现代卡片布局**：重构了操作员管理页面，采用响应式阴影卡片流式布局，增强视觉通透感。
- **高级统计图表**：引入 `BeautifulPie` 和 `StatCard` 自定义组件，支持 ECharts 渐变色、阴影特效及 KPI 聚合展示，数据呈现更具专业感。
- **性能与体验修复**：
  - 优化了部门树（DepMana）的渲染机制，解决按钮显示延迟问题。
  - 修正了后端分页 SQL 逻辑，确保多表联查下每页数据条数精确无误。
  - 全站按键（如批量删除、刷新、搜索）实现大小与风格的完美统一。

## 📂 项目结构

```text
├── backend                # 后端 Spring Boot 工程
│   ├── src/main/java      # Java 源代码
│   ├── src/main/resources # 配置文件及 MyBatis Mapper XML
│   └── database           # 数据库 SQL 脚本
├── frontend               # 前端 Vue 工程
│   ├── src/components     # 公共组件 (自定义图表、统计卡片等)
│   ├── src/views          # 业务逻辑页面 (人事、薪资、系统等)
│   └── src/common/css     # 全局样式定义
└── README.md              # 项目说明文档
```

## 🛠️ 快速开始

### 1. 数据库准备
- 在 MySQL 中创建名为 `vhr` 的数据库。
- 执行 `database/vhr.sql` 脚本导入初始数据。

### 2. 后端启动
- 修改 `backend/src/main/resources/application.properties` 中的数据库连接信息。
- 在 `backend` 目录下运行：
  ```bash
  mvn spring-boot:run
  ```

### 3. 前端启动
- 进入 `frontend` 目录。
- 安装依赖：
  ```bash
  npm install
  ```
- 启动项目：
  ```bash
  npm run serve
  ```

---
*本项目持续迭代中，致力于打造最极致的 OA 办公管理体验。*


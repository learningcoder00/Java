<template>
  <div class="welcome-container">
    <div class="welcome-content">
      <!-- 主标题 -->
      <div class="welcome-title">
        <i class="fa fa-users fa-3x welcome-icon"></i>
        <h1 class="title-text">欢迎使用智管人事系统</h1>
        <p class="subtitle-text">Welcome to Smart HR Management System</p>
      </div>

      <!-- 功能卡片 -->
      <div class="feature-cards">
        <div class="feature-card">
          <div class="card-icon">
            <i class="el-icon-user-solid"></i>
          </div>
          <h3>人事管理</h3>
          <p>高效管理员工信息</p>
        </div>
        <div class="feature-card">
          <div class="card-icon">
            <i class="el-icon-s-data"></i>
          </div>
          <h3>数据统计</h3>
          <p>实时数据分析</p>
        </div>
        <div class="feature-card">
          <div class="card-icon">
            <i class="el-icon-document"></i>
          </div>
          <h3>系统配置</h3>
          <p>灵活的系统设置</p>
        </div>
        <div class="feature-card">
          <div class="card-icon">
            <i class="el-icon-setting"></i>
          </div>
          <h3>权限管理</h3>
          <p>安全的权限控制</p>
        </div>
      </div>

      <!-- 底部提示 -->
      <div class="welcome-footer">
        <p>请使用顶部导航菜单开始使用系统功能</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "main",
  data () {
    return {
    }
  },
  mounted () {
  },
  methods: {
  }
}
</script>

<style scoped>
.welcome-container {
  width: 100%;
  height: 100vh;
  max-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e6e1f5 100%);
  padding: 20px;
  padding-top: 60px;
  box-sizing: border-box;
  overflow: hidden;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.welcome-content {
  max-width: 1200px;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.8s ease-out;
  overflow: hidden;
  padding-top: 70px;
  padding-bottom: 40px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.welcome-title {
  text-align: center;
  margin-bottom: 50px;
  margin-top: 20px;
  flex-shrink: 0;
}

.welcome-icon {
  color: #7B68EE;
  margin-bottom: 20px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.title-text {
  font-size: 42px;
  font-weight: bold;
  font-family: 站酷庆科黄油体;
  background: linear-gradient(135deg, #7B68EE 0%, #9370DB 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 20px 0;
  letter-spacing: 2px;
}

.subtitle-text {
  font-size: 18px;
  color: #909399;
  margin-top: 10px;
  font-weight: 300;
  letter-spacing: 1px;
}

.feature-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 25px;
  margin-bottom: 30px;
  flex-shrink: 0;
  width: 100%;
  max-width: 1400px;
}

.feature-card {
  background: #ffffff;
  border-radius: 16px;
  padding: 45px 35px;
  text-align: center;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  min-width: 0;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
}

.card-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #7B68EE 0%, #9370DB 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: #ffffff;
  transition: all 0.3s ease;
}

.feature-card:hover .card-icon {
  transform: scale(1.1) rotate(5deg);
}

.feature-card h3 {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 15px 0 10px;
  font-family: 站酷庆科黄油体;
}

.feature-card p {
  font-size: 14px;
  color: #909399;
  line-height: 1.6;
  margin: 0;
}

.welcome-footer {
  text-align: center;
  margin-top: auto;
  padding-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  flex-shrink: 0;
}

.welcome-footer p {
  font-size: 16px;
  color: #606266;
  margin: 0;
  font-family: 站酷庆科黄油体;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .feature-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .title-text {
    font-size: 32px;
  }
  
  .welcome-title {
    margin-bottom: 30px;
  }
  
  .feature-cards {
    grid-template-columns: 1fr;
    gap: 15px;
    margin-bottom: 20px;
  }
  
  .feature-card {
    padding: 25px 20px;
  }
  
  .welcome-footer {
    padding-top: 15px;
  }
}
</style>

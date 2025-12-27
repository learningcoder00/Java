<template>
  <div class="stat-card">
    <div class="stat-card__header">
      <div class="stat-card__title-row">
        <div class="stat-card__title">
          <i v-if="icon" :class="icon" class="stat-card__icon"></i>
          <span>{{ title }}</span>
        </div>
        <div v-if="subtitle" class="stat-card__subtitle">{{ subtitle }}</div>
      </div>

      <div v-if="kpis && kpis.length" class="stat-card__kpis">
        <div v-for="(k, idx) in kpis" :key="idx" class="kpi">
          <div class="kpi__label">{{ k.label }}</div>
          <div class="kpi__value" :class="k.tone ? ('kpi__value--' + k.tone) : ''">
            {{ k.value }}
            <span v-if="k.unit" class="kpi__unit">{{ k.unit }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="stat-card__body">
      <slot />
    </div>
  </div>
</template>

<script>
export default {
  name: 'StatCard',
  props: {
    title: { type: String, default: '' },
    subtitle: { type: String, default: '' },
    icon: { type: String, default: '' },
    kpis: { type: Array, default: () => [] }
  }
}
</script>

<style scoped>
.stat-card {
  border-radius: 16px;
  border: 1px solid #eaeaea;
  background: #fff;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.stat-card__header {
  padding: 14px 18px 10px;
  background: linear-gradient(180deg, rgba(123, 104, 238, 0.06) 0%, rgba(255, 255, 255, 1) 70%);
  border-bottom: 1px solid #f2f4f8;
}

.stat-card__title-row {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
}

.stat-card__title {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 28px;
  font-family: 站酷庆科黄油体;
  color: #303133;
  letter-spacing: 1px;
}

.stat-card__icon {
  color: #7B68EE;
  font-size: 22px;
}

.stat-card__subtitle {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
}

.stat-card__kpis {
  margin-top: 10px;
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 10px;
}

.kpi {
  border: 1px solid #eef0f6;
  background: #ffffff;
  border-radius: 12px;
  padding: 10px 12px;
  min-width: 0;
}

.kpi__label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 6px;
}

.kpi__value {
  font-size: 18px;
  color: #303133;
  font-weight: 700;
  line-height: 1.1;
}

.kpi__unit {
  font-size: 12px;
  color: #909399;
  margin-left: 4px;
  font-weight: 600;
}

.kpi__value--success { color: #00C9A7; }
.kpi__value--danger { color: #FF6B81; }
.kpi__value--warning { color: #FFB020; }
.kpi__value--primary { color: #7B68EE; }

.stat-card__body {
  padding: 6px 12px 14px;
}

@media (max-width: 1100px) {
  .stat-card__kpis { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
</style>



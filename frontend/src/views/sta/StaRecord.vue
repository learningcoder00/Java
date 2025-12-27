<template>
  <div>
    <div style="margin-top:30px;display:flex ;justify-content: center;"
         v-loading.fullscreen.lock="loading"
         element-loading-spinner="fa fa-spinner fa-pulse fa-3x fa-fw">
      <div style="width:100%;color:#E6A23C">
        <el-carousel indicator-position="outside"
                     height="740px"
                     type="card"
                     :interval="4000"
                     style="border-radius: 15px;overflow-y: hidden;padding-bottom:30px;">
          <el-carousel-item v-for="(item,index) in datas"
                            :key="index"
                            class="sta-card">
            <StatCard
              :title="item.title"
              :subtitle="item.subtitle"
              :icon="item.icon"
              :kpis="buildKpis(item)"
            >
              <ve-histogram
                :data="item.chartData"
                :settings="item.chartSettings"
                :extend="item.chartExtend"
                height="610px"
                width="100%"
                :judge-width="true"
              />
            </StatCard>

          </el-carousel-item>

        </el-carousel>
      </div>
    </div>
  </div>
</template>

<script>
import StatCard from '@/components/sta/StatCard.vue'

export default {
  name: "StaRecord",
  components: { StatCard },
  data () {
    // 人事记录分析：统一图表主题（白底、图例、网格、坐标轴、Tooltip、配色）
    const commonHistogramExtend = (option) => {
      const opt = option || {}
      opt.backgroundColor = '#ffffff'

      opt.legend = Object.assign({
        type: 'scroll',
        top: 12,
        left: 'center',
        icon: 'roundRect',
        itemWidth: 12,
        itemHeight: 10,
        itemGap: 12,
        textStyle: { fontSize: 12, color: '#606266' }
      }, opt.legend || {})

      opt.tooltip = Object.assign({
        trigger: 'axis',
        axisPointer: { type: 'shadow' }
      }, opt.tooltip || {})

      opt.grid = Object.assign({
        top: 72,
        left: 60,
        right: 40,
        bottom: 70,
        containLabel: true
      }, opt.grid || {})

      // 坐标轴统一风格
      opt.xAxis = Array.isArray(opt.xAxis) ? opt.xAxis : (opt.xAxis ? [opt.xAxis] : [])
      opt.yAxis = Array.isArray(opt.yAxis) ? opt.yAxis : (opt.yAxis ? [opt.yAxis] : [])
      opt.xAxis = opt.xAxis.map((x) => Object.assign({
        axisLabel: { color: '#606266' },
        axisLine: { lineStyle: { color: '#E4E7ED' } },
        axisTick: { show: false }
      }, x || {}))
      opt.yAxis = opt.yAxis.map((y) => Object.assign({
        axisLabel: { color: '#606266' },
        splitLine: { lineStyle: { color: '#EBEEF5' } },
        axisLine: { show: false },
        axisTick: { show: false }
      }, y || {}))

      // 系列统一美化（柱圆角、线条平滑）
      if (Array.isArray(opt.series)) {
        opt.series = opt.series.map((s) => {
          const series = s || {}
          if (series.type === 'bar') {
            series.barMaxWidth = series.barMaxWidth || 28
            // 按语义配色：入职(绿)、离职(红)
            const name = String(series.name || '')
            const isDimission = name.includes('离职')
            const isEntrant = name.includes('入职')
            const color = isDimission
              ? new (require('echarts').graphic).LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#FF6B81' }, { offset: 1, color: '#FFD0C2' }])
              : isEntrant
                ? new (require('echarts').graphic).LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#00C9A7' }, { offset: 1, color: '#BFF1F1' }])
                : series.itemStyle && series.itemStyle.color
            series.itemStyle = Object.assign({
              borderRadius: [8, 8, 0, 0],
              color
            }, series.itemStyle || {})
          }
          if (series.type === 'line') {
            series.smooth = true
            series.symbolSize = series.symbolSize || 6
            const name = String(series.name || '')
            const isRate = name.includes('率')
            const isAvg = name.includes('平均')
            series.lineStyle = Object.assign({
              width: 3,
              color: isRate ? '#7B68EE' : (isAvg ? '#FFB020' : series.lineStyle && series.lineStyle.color)
            }, series.lineStyle || {})
            series.itemStyle = Object.assign({
              color: isRate ? '#7B68EE' : (isAvg ? '#FFB020' : series.itemStyle && series.itemStyle.color)
            }, series.itemStyle || {})
          }
          return series
        })
      }

      return opt
    }

    return {
      datas: [
        {
          title: "部门人事分析",
          subtitle: "按部门对入职、离职与离职率进行对比",
          icon: "el-icon-office-building",
          chartSettings: {

            showLine: ['aDouble',],
            yAxisName: ['人数', '比率'],
            xAxis: {
              axisLabel: {
                rotate: 45,
              }
            },
            label: { show: true, position: "left" },
            labelMap: {
              "dimission": "离职人数",
              "entrant": "入职人数",
              "aDouble": "离职率"
            },

          },
          chartExtend: commonHistogramExtend,

          chartData: {
            columns: [],
            rows: [

            ]
          }
        },
        {
          title: "职位人事分析",
          subtitle: "按职位对入职/离职/离职率与平均离职工龄进行分析",
          icon: "el-icon-s-custom",
          chartSettings: {
            showLine: ['entrant', 'dimission'],
            yAxisName: ['人数', '比率'],
            xAxis: {
              axisLabel: {
                rotate: 45,
              }
            },
            label: { show: true, position: "top" },
            labelMap: {
              "dimission": "离职人数",
              "entrant": "入职人数",
              "aDouble": "离职率",
              "sum": "平均离职工龄",
            },
          },
          chartExtend: commonHistogramExtend,
          chartData: {
            columns: [],
            rows: []

          }
        },
        {
          title: "职称人事分析",
          subtitle: "按职称分析入职、离职与离职率（含均值指标）",
          icon: "el-icon-medal-1",
          chartSettings: {
            showLine: ['entrant', 'aDouble'],
            yAxisName: ['人数', '比率'],
            xAxis: {
              axisLabel: {
                rotate: 45,
              }
            },
            label: { show: true, position: "top" },
            labelMap: {
              "dimission": "离职人数",
              "entrant": "入职人数",
              "aDouble": "离职率",
              "sum": "平均离职年龄",
            },
          },
          chartExtend: commonHistogramExtend,
          chartData: {
            columns: [],
            rows: []

          }
        },
      ],
      loading: false,
    }
  },
  mounted () {
    this.initpoin();
    this.initpin();
    this.initJop();
  },
  methods: {
    buildKpis (item) {
      const rows = (item && item.chartData && Array.isArray(item.chartData.rows)) ? item.chartData.rows : []
      const sum = (key) => rows.reduce((acc, r) => acc + Number(r && r[key] != null ? r[key] : 0), 0)
      const avg = (key) => {
        if (!rows.length) return 0
        return sum(key) / rows.length
      }
      const entrant = sum('entrant')
      const dimission = sum('dimission')
      const rateAvg = avg('aDouble') // 后端看起来就是比率(0.x)
      const hasSum = rows.some(r => r && r.sum != null)
      const avgSum = hasSum ? avg('sum') : null

      const fmtInt = (v) => String(Math.round(Number(v || 0)))
      const fmtPct = (v) => {
        const n = Number(v || 0) * 100
        return (Math.round(n * 100) / 100).toFixed(2)
      }
      const fmt1 = (v) => (Math.round(Number(v || 0) * 10) / 10).toFixed(1)

      const kpis = [
        { label: '入职总数', value: fmtInt(entrant), tone: 'success' },
        { label: '离职总数', value: fmtInt(dimission), tone: 'danger' },
        { label: '离职率均值', value: fmtPct(rateAvg), unit: '%', tone: 'primary' }
      ]
      if (avgSum != null) {
        kpis.push({ label: '平均指标', value: fmt1(avgSum), tone: 'warning' })
      } else {
        kpis.push({ label: '样本维度', value: String(rows.length), tone: 'warning' })
      }
      return kpis
    },
    initpoin () {
      this.loading = true;
      this.$notify.success({
        title: '系统讯息',
        message: '人 事 记 录 统 计 数 据 加 载 中...',
        showClose: false,
        offset: 100,
        duration: 5000,
        customClass: 'fontclass'
      });
      this.getRequest("/statistics/recored/department").then(resp => {
        if (resp) {
          this.datas[0].chartData.columns = ["name", "entrant", "dimission", "aDouble"];
          this.datas[0].chartData.rows = resp;

        }
      });
    },
    initpin () {
      this.getRequest("/statistics/recored/workAge").then(resp => {
        if (resp) {
          this.datas[1].chartData.columns = ["name", "entrant", "dimission", "aDouble", "sum"];
          this.datas[1].chartData.rows = resp;

        }
      });
    },
    initJop () {
      this.getRequest("/statistics/recored/JobLevel").then(resp => {
        if (resp) {
          this.datas[2].chartData.columns = ["name", "entrant", "dimission", "aDouble", "sum"];
          this.datas[2].chartData.rows = resp;
          this.loading = false;
        }
      });
    }
  }
}
</script>
<style scoped>
.sta-card {
  border-radius: 15px;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #EBEEF5;
  background: #fff;
}
</style>
<template>
  <div class="beautiful-pie">
    <div ref="chart" class="beautiful-pie__chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'BeautifulPie',
  props: {
    title: {
      type: String,
      default: ''
    },
    rows: {
      type: Array,
      default: () => []
    },
    // 是否玫瑰图
    rose: {
      type: Boolean,
      default: false
    },
    // 展示前 N 个标签，其余隐藏，避免拥挤
    labelTopN: {
      type: Number,
      default: 6
    }
  },
  data () {
    return {
      chart: null,
      ro: null
    }
  },
  mounted () {
    this.initChart()
    this.render()
    this.bindResize()
  },
  beforeDestroy () {
    this.unbindResize()
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }
  },
  watch: {
    rows: {
      deep: true,
      handler () {
        this.render()
      }
    },
    rose () {
      this.render()
    },
    title () {
      this.render()
    }
  },
  methods: {
    initChart () {
      if (this.chart) return
      this.chart = echarts.init(this.$refs.chart)
    },
    bindResize () {
      const el = this.$refs.chart
      if (!el) return
      // ResizeObserver 优先，兼容不支持时退化到 window resize
      if (typeof ResizeObserver !== 'undefined') {
        this.ro = new ResizeObserver(() => {
          if (this.chart) this.chart.resize()
        })
        this.ro.observe(el)
      } else {
        this._onResize = () => {
          if (this.chart) this.chart.resize()
        }
        window.addEventListener('resize', this._onResize)
      }
    },
    unbindResize () {
      if (this.ro) {
        this.ro.disconnect()
        this.ro = null
      }
      if (this._onResize) {
        window.removeEventListener('resize', this._onResize)
        this._onResize = null
      }
    },
    normalizeData () {
      const list = Array.isArray(this.rows) ? this.rows : []
      const data = list
        .map(r => ({
          name: r && r.name != null ? String(r.name) : '',
          value: Number(r && r.sum != null ? r.sum : r && r.value != null ? r.value : 0)
        }))
        .filter(d => d.name && !Number.isNaN(d.value))
        .sort((a, b) => b.value - a.value)

      const total = data.reduce((acc, cur) => acc + cur.value, 0)
      return { data, total }
    },
    buildColors (count) {
      // 柔和渐变调色板（ECharts 支持在 itemStyle.color 里用 LinearGradient）
      const base = [
        ['#7B68EE', '#B6A8FF'],
        ['#00C9A7', '#8AF2E1'],
        ['#FF6B81', '#FFB3C0'],
        ['#FFB020', '#FFE0A3'],
        ['#4D96FF', '#A3C8FF'],
        ['#9D7CE8', '#D8C8FF'],
        ['#4BC0C0', '#BFF1F1'],
        ['#FF8A65', '#FFD0C2']
      ]
      const colors = []
      for (let i = 0; i < count; i++) {
        const g = base[i % base.length]
        colors.push(new echarts.graphic.LinearGradient(0, 0, 1, 1, [
          { offset: 0, color: g[0] },
          { offset: 1, color: g[1] }
        ]))
      }
      return colors
    },
    render () {
      if (!this.chart) return
      const { data, total } = this.normalizeData()
      const colors = this.buildColors(data.length)
      const topN = Math.max(0, this.labelTopN)

      // 只给 TopN 显示外部标签，其余隐藏避免挤压导致“看起来大小不一”
      const showLabelNames = new Set(data.slice(0, topN).map(d => d.name))

      const option = {
        backgroundColor: '#ffffff',
        tooltip: {
          trigger: 'item',
          formatter: '{b}<br/>{c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          top: 12,
          left: 'center',
          icon: 'roundRect',
          itemWidth: 12,
          itemHeight: 10,
          itemGap: 12,
          textStyle: { fontSize: 12, color: '#606266' }
        },
        graphic: [{
          type: 'group',
          left: 'center',
          top: '56%',
          children: [
            {
              type: 'text',
              style: {
                text: '总计',
                fill: '#909399',
                fontSize: 12,
                fontWeight: 500,
                textAlign: 'center'
              },
              top: -12,
              left: 'center'
            },
            {
              type: 'text',
              style: {
                text: String(total),
                fill: '#303133',
                fontSize: 22,
                fontWeight: 700,
                textAlign: 'center'
              },
              top: 6,
              left: 'center'
            }
          ]
        }],
        series: [{
          name: this.title,
          type: 'pie',
          roseType: this.rose ? 'radius' : false,
          radius: this.rose ? ['28%', '74%'] : ['40%', '72%'],
          center: ['50%', '56%'],
          avoidLabelOverlap: true,
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 2,
            shadowBlur: 12,
            shadowColor: 'rgba(0,0,0,0.10)',
            shadowOffsetY: 6
          },
          label: {
            show: true,
            formatter: (p) => {
              if (!showLabelNames.has(p.name)) return ''
              return `${p.name}\n${p.percent}%`
            },
            fontSize: 12,
            color: '#606266'
          },
          labelLine: {
            show: true,
            length: 14,
            length2: 10,
            lineStyle: { color: '#C0C4CC' }
          },
          emphasis: {
            scale: true,
            scaleSize: 10,
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 700,
              color: '#303133'
            }
          },
          data: data.map((d, idx) => ({
            ...d,
            itemStyle: { color: colors[idx] }
          }))
        }]
      }

      this.chart.setOption(option, true)
    }
  }
}
</script>

<style scoped>
.beautiful-pie {
  width: 100%;
  height: 100%;
}

.beautiful-pie__chart {
  width: 100%;
  height: 640px;
}
</style>



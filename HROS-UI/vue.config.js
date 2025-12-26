let proxyObj = {};
proxyObj['/ws'] = {
    ws: true,
    target: "ws://localhost:8080"  // 修复：改为后端实际端口 8080
};

proxyObj['/'] = {
    target: 'http://localhost:8080',  // 修复：改为后端实际端口 8080
    ws: false,
    changeOrigin: true,
    pathRewrite: {
        '^/': ''
    }
}
module.exports = {
    devServer: {
        host: '127.0.0.1',
        port: 8081,  // 修复：添加端口 8081，避免与后端 8080 冲突
        proxy: proxyObj,
    }
}
const title = '终端应用小工具系统';

module.exports =  {
    // 前后端分离
    dev: {
        title: title,
        html: {
            template: 'index.html',
            templateOutPath: '',
            data: {
                clientName: '/'// 前端工程名称
            }
        },
        proxyUrl: 'http://localhost:9091/',// 代理后端服务器根路径
        outputPath: './target/',
        static: '',
        copy: true,// copy资源
        compress: false
    },
    // 前后端不分离，使用thymeleaf引擎模板
    pro: {
        title: title,
        html: {
            template: 'index_thymeleaf.html',
            templateOutPath: 'templates/',
            data: {
                _$: '$'// thymeleaf的$符
            }
        },
        outputPath: '../',// 打包文件输出的位置
        static: 'static/',// 静态资源存放的位置
        copy: true,// 是否copy资源
        compress: true// 开启js压缩
    }
};
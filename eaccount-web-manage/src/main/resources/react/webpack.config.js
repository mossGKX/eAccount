const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const Parameter = require('./webpack.parameter.js');
const src_path = './src/';

const timeStamp = new Date().getTime();

module.exports = function (env) {
    const config = Parameter[env];
    if(!config) {
        throw new Error('环境变量为空');
    }
    const webpackConfig = {
        devServer: {
            proxy: {
                '/api': {
                    target: config.proxyUrl,
                    pathRewrite: {'^/api': ''}
                }
            }
        },
        // 插件项
        plugins: [
            new HtmlWebpackPlugin({
                title: config.title,
                filename: config.html.templateOutPath + 'index.html',// 输出的 HTML 文件名，默认是 index.html, 也可以直接配置带有子目录
                template: src_path + config.html.template,// 模板文件路径，支持加载器，比如 html!./index.html
                inject: 'body',// true | 'head' | 'body' | false  ,注入所有的资源到特定的 template 或者 templateContent 中，
                // 如果设置为 true 或者 body，所有的 javascript 资源将被放置到 body 元素的底部，'head' 将放置到 head 元素中。
                chunks: [ ],
                minify: {
                    removeComments: true,
                    collapseWhitespace: !!config.compress
                },
                data: config.html.data || {},
                hash: timeStamp
            })
        ],
        //页面入口文件配置
        entry: {
            index: [ src_path + 'index.jsx' ]
        },
        //入口文件输出配置
        output: {
            path: path.resolve(__dirname, config.outputPath),
            filename: config.static + 'resources/[name].js'
        },
        module: {
            //loaders加载器
            loaders: [
                {
                    test: /\.(js|jsx)$/,//一个匹配loaders所处理的文件的拓展名的正则表达式，这里用来匹配js和jsx文件（必须）
                    loader: 'babel-loader',//loader的名称（必须）
                    query: {
                        presets: ['es2015', 'react', 'stage-0'] // babel-preset-stage-0 增强es6语法
                    }
                }
            ]
        }
    };

    if(config.compress) {
        webpackConfig.plugins.push(
            new webpack.optimize.UglifyJsPlugin({ //压缩代码
                compress: {
                    warnings: false
                },
                output: {
                    comments: false
                },
                except: ['$super', '$', 'exports', 'require'] // 排除关键字
            })
        )
    }

    if(config.copy) {
        webpackConfig.plugins.push(
            new CopyWebpackPlugin([{
                from: src_path + 'static/',
                to: path.resolve(__dirname, config.outputPath + config.static)
            }])
        )
    }

    return webpackConfig;
};
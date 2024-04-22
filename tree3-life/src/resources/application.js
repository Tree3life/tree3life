//系统中的常量配置文件
export const app_name = "tree3life";

//服务名称/网关前缀 gatewayPrefix
export const gatewayPrefix = {
    "app": "/app",// 认证相关
    "archives": "/archives",//文件处理服务
    "chat": "/chat",//聊天服务
    "search": "/search",//搜索服务
    "users": "/users",//用户服务
}


const settings = {
//region 自定义配置
        "token_key": app_name + "Token",//token在浏览器存储中的key
        "user_key": app_name + "User",


//endregion  自定义配置

//region 基本路径配置
        //          "http://主机:端口/网关前缀/真实路径",
        // "demo_url":"http://localhost:9200/search/searchT/aa",
        "base_url": "http://localhost:9200",//api基本路径
        /* 基本路径演示
        "base_url_app": "http://localhost:9200/app/",//认证相关路径
        "base_url_archives": "http://localhost:9200/archives/",//文件处理服务
        "base_url_chat": "http://localhost:9200/chat/",//聊天服务
        "base_url_search": "http://localhost:9200/search/",//搜索服务
        "base_url_users": "http://localhost:9200/users/",//用户服务*/

//endregion 基本路径配置


        timeout: 12000,//请求超时时间,
        websocket_url: 'ws://127.0.0.1:8088/webSocket/rupert'
    }

export default settings

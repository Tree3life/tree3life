/*
 该模块是用于定义action对象中type类型的常量值，目的只有一个：便于管理的同时防止 开发者 单词写错
*/
export default Object.freeze({

    saveInLocalCache: Symbol("将后端共享的固定常量信息写入缓存"),
    /**
     * 用户信息相关
     */
    storageUserInfo: Symbol('暂存用户信息'),


    /**
     * 系统信息相关
     */
    saveMenuList: Symbol('缓存菜单信息'),
    savePermissionInfo: Symbol('缓存权限信息'),

    //region websocket相关
    //接收一个`消息对象`参数，在 channel建立成功的回调onopen中向服务器发送该消息
    initWebsocketServer: Symbol('初始化连接websocket服务器:connect、设置各种回调函数——(onopen，onmessage，onclose，onerror)'),
    sendMessageText: Symbol('发送websocket消息'),
    sendMessageBlob: Symbol('发送二进制数据ArrayBuffer消息'),
    sendMessageArrayBuffer: Symbol('发送websocket消息'),
    parseMessage: Symbol('解析websocket消息'),
    destroyWebsocketServer: Symbol("销毁与websocket服务器建立的连接对象"),
    //endregion websocket相关

})

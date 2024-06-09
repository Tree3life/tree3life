package com.tree3.pojo.message;

/**
 * <p>
 * 文件消息
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 11:18 </p>
 */
public class MessageFile extends MessageHead {

    private static final long serialVersionUID = 7056105591514900078L;
    /**
     * 消息内容
     */
    private Object fileObj;

    @Override
    public int queryMessageType() {
        return MessageFile;
    }
}

package com.tree3.pojo;

/**
 * <p>
 * 文件解析结果
 * 本类仅起到标记作用
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 8:14 </p>
 */
public abstract class FileParseResult {
    String fileName;
    String fileType;
    /**
     * 文件后缀
     */
    String fileSuffix;

    public abstract String getFileName();
    public abstract String getFileType();
    public abstract String getFileSuffix();
}
package com.tree3.tool;

/**
 * <p>
 * 标注记录所有的文件类型
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 8:04 </p>
 */
public final class FileType {

    private FileType() {
        throw new AssertionError("No xxxxxx.com.tree3.parser.FileType instances for you!");
    }


    public static final String TEXT = "text";
    public static final String WORD = "word";
    public static final String EXCEL = "excel";
    public static final String PDF = "pdf";
    public static final String MARKDOWN = "MARKDOWN";
}

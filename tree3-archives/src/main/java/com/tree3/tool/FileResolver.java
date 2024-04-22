package com.tree3.tool;

import com.tree3.pojo.FileParseResult;

import java.nio.charset.Charset;

/**
 * <p>
 * 文件解析的核心类
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 10:38 </p>
 */
public interface FileResolver {


    /**
     * 此方法应采用 设计模式  ，实现对多种文件的解析
     * 核心方法
     *
     * @param fileType {@link com.tree3.tool.FileType}
     * @param path
     * @return
     */
    FileParseResult parseFile(String fileType, String path);


    /**
     * 原封不动地读取文件，获取文件最原始的内容
     *
     * @param pat
     * @return
     */
    String getOriginalText(String pat);

    /**
     * 确定文件类型
     *
     * @param path
     * @return
     */
    String determineFileType(String path);

    /**
     * 确定文件类型
     *
     * @param path
     * @return
     */
    Charset determineFileEncode(String path);
}

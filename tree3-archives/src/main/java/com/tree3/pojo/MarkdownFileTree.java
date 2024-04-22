package com.tree3.pojo;

/**
 * <p>
 * 将文件解析为
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 8:12 </p>
 */
public class MarkdownFileTree extends FileParseResult {
    public static void main(String[] args) {
        FileParseResult markdownResult = new MarkdownFileTree();
    }

    /**
     * 标题
     */
    public MarkdownFileTree children;

    public MarkdownFileTree() {
    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public String getFileType() {
        return null;
    }

    @Override
    public String getFileSuffix() {
        return null;
    }
}

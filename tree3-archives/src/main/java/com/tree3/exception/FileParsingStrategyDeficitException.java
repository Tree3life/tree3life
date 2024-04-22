package com.tree3.exception;

/**
 * <p>
 *  没有对应的文件解析策略时报错
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 11:55 </p>
 */
public class FileParsingStrategyDeficitException extends RuntimeException{
    private static final long serialVersionUID = -5320619299935222072L;

    public FileParsingStrategyDeficitException(String message) {
        super(message);
    }
}

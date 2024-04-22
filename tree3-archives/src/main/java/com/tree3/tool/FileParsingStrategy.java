package com.tree3.tool;

import com.tree3.pojo.FileParseResult;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 8:00 </p>
 */
@FunctionalInterface
public interface FileParsingStrategy {
    FileParseResult parseFile(String  path);
}

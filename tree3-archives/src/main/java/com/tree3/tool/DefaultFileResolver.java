package com.tree3.tool;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.tree3.exception.FileParsingStrategyDeficitException;
import com.tree3.exception.IllegalIcuFileTypeException;
import com.tree3.pojo.FileParseResult;
import com.tree3.pojo.MarkdownFileTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 10:41 </p>
 */
public class DefaultFileResolver implements FileResolver {
    private static final HashMap<String, FileParsingStrategy> fileParsingStrategyMap = new HashMap<>();

    /**
     * 初始化 各种 类型文本的解析策略
     */
    static {
        //解析Markdown文件的具体流程
        fileParsingStrategyMap.put(FileType.MARKDOWN, path -> {
            System.out.println("markdown--------------");
            return new MarkdownFileTree();
        });

        //解析txt文件的具体流程
        fileParsingStrategyMap.put(FileType.TEXT, path -> {
            return null;
        });
        fileParsingStrategyMap.put(FileType.PDF, path -> {
            return null;
        });
        fileParsingStrategyMap.put(FileType.WORD, path -> {
            return null;
        });
        fileParsingStrategyMap.put(FileType.EXCEL, path -> {
            return null;
        });
    }


    @Override
    public FileParseResult parseFile(String fileType, String path) {
        Assert.notEmpty(fileType);
        Assert.notEmpty(path);
        System.out.println("aaaa");

        //获取该类型文本的 解析策略
        FileParsingStrategy strategy = fileParsingStrategyMap.get(fileType);
        if (ObjectUtil.isNull(strategy)) {
            throw new FileParsingStrategyDeficitException("未知的文件类型，请先添加对应的文件解析程序");
        }

        //调用解析策略获取结果
        return strategy.parseFile(path);
    }

    @Override
    public String getOriginalText(String pat) {
        return null;
    }

    /**
     * 返回不带`.`的后缀名
     * @param filePath
     * @return
     */
    @Override
    public String determineFileType(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            throw new IllegalArgumentException("文件路径不能为空");
        }
        File file = new File(filePath);
        if (file.isDirectory()) {
            throw new IllegalIcuFileTypeException("不支持的文件类型");
        }

        int index = filePath.lastIndexOf(".");
        if (index == -1) {
            throw new IllegalIcuFileTypeException("不支持的文件类型（无法读取文件后缀名）");
        } else {
            String ext = filePath.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return StrUtil.containsAny(ext, '/', '\\') ? "" : ext;
        }
    }

    /**
     * 读取文件流的前3个字节
     * 获取文件的编码格式
     *
     * @param path
     * @return
     */
    @Override
    public Charset determineFileEncode(String path) {
        if (new File(path).isDirectory()) {
            throw new IllegalIcuFileTypeException("不支持的文件");
        }
        Charset charset = StandardCharsets.UTF_8;
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            InputStream is = new FileInputStream(path);
            int read = is.read(first3Bytes, 0, 3);

            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = StandardCharsets.UTF_16LE;
                checked = true;

            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = StandardCharsets.UTF_16BE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = StandardCharsets.UTF_8;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xA
                    && first3Bytes[1] == (byte) 0x5B
                    && first3Bytes[2] == (byte) 0x30) {
                charset = StandardCharsets.UTF_8;
                checked = true;
            } else {
                throw new RuntimeException("不支持的文本编码格式");
            }
//                 else if (first3Bytes[0] == (byte) 0xD
//                        && first3Bytes[1] == (byte) 0xA
//                        && first3Bytes[2] == (byte) 0x5B) {
//                    charset = "GBK";
//                    checked = true;
//                } else if (first3Bytes[0] == (byte) 0x5B
//                        && first3Bytes[1] == (byte) 0x54
//                        && first3Bytes[2] == (byte) 0x49) {
//                    charset = "windows-1251";
//                    checked = true;
//                }
            InputStream istmp = new FileInputStream(path);
            if (!checked) {
                int loc = 0;
                while ((read = istmp.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = istmp.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = istmp.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = istmp.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = StandardCharsets.UTF_8;
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            istmp.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

}

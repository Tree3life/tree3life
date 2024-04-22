package com.tree3.parser;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.json.JSONUtil;
import com.tree3.exception.FileParsingStrategyDeficitException;
import com.tree3.pojo.FileParseResult;
import com.tree3.tool.DefaultFileResolver;
import com.tree3.tool.FileType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.collection.iteration.ReversiblePeekingIterator;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 10:21 </p>
 */
public class DefaultFileResolverTest {
    private static final String FILE_PATH = "F:\\Desktop\\temp\\新建文件夹\\testHome\\解析Markdown文件测试.md";

    @Test
    public void parseFile() {
        DefaultFileResolver defaultFileResolver = new DefaultFileResolver();
        FileParseResult aaaa = defaultFileResolver.parseFile(FileType.MARKDOWN, "aaaa");
        System.out.println(aaaa);

    }

    @Test
    public void getOriginalText() {

        File file = new File(FILE_PATH);
        StringBuilder builder = new StringBuilder();
        System.out.println(FileUtil.extName(file));

        //将文件以字节的形式进行读取，以字符的形式放入内存
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            char[] vessels = new char[1024 * 1024 * 8];
            int len;
            while ((len = reader.read(vessels)) != -1) {
                builder.append(vessels, 0, len);
            }
            System.out.println(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileParsingStrategyDeficitException("文件解析异常");
        }
    }



    public static void walkTree(Node node) {

    }

    @Test
    public void determineFileType() {
        DefaultFileResolver resolver = new DefaultFileResolver();
        String baseName = "F:\\Desktop\\temp\\新建文件夹\\testHome\\";
        String file1 = new String(baseName+"ccc.txt");
        String file2 = new String(baseName+"ccc16.aa");
        String file3 = new String(baseName+"ccc16B.bb");
        String file4 = new String(baseName+"cccan.cc.....\\\\aaaaa");
        System.out.println("ccc.txt----"+resolver.determineFileType(file1.toString()));
        System.out.println("ccc16.aa----"+resolver.determineFileType(file2.toString()));
        System.out.println("ccc16B.bb----"+resolver.determineFileType(file3.toString()));
        System.out.println("cccan.cc----"+resolver.determineFileType(file4.toString()));

    }

    @Test
    public void determineFileEncode() {
        DefaultFileResolver resolver = new DefaultFileResolver();
        String baseName = "F:\\Desktop\\temp\\新建文件夹\\testHome\\";
        File file1 = new File(baseName+"ccc.txt");
        File file2 = new File(baseName+"ccc16.txt");
        File file3 = new File(baseName+"ccc16B.txt");
        File file4 = new File(baseName+"cccan.txt");
        System.out.println("ccc.txt----"+resolver.determineFileEncode(file1.toString()));
        System.out.println("ccc16.txt----"+resolver.determineFileEncode(file2.toString()));
        System.out.println("ccc16B.txtt----"+resolver.determineFileEncode(file3.toString()));
        System.out.println("cccan.txt----"+resolver.determineFileEncode(file4.toString()));

    }

    @Test
    public void flexdownTest() {
        FileReader reader = new FileReader(FILE_PATH, StandardCharsets.UTF_8);
        String originalContent = reader.readString();

        MutableDataSet options = new MutableDataSet();
        // uncomment to set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        //uncomment to convert soft-breaks to hard breaks
//        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(originalContent);
//        String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
//        System.out.println(document.getChars());
//        Node firstChild = document.getFirstChild();
//        System.out.println(firstChild.getChars());
//        Node next = firstChild.getNext();
//        System.out.println(next.getChars());
//        System.out.println(document.getChars());
        ReversiblePeekingIterator<Node> childIterator = document.getChildIterator();

        Node next;
        while (childIterator.hasNext()) {
            next = childIterator.next();
            System.out.println("=====================================================================");
            System.out.println(next.getChars());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }
    }




    public static String GetEncoding(File file)
    {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            InputStream is = new FileInputStream(file);
            int read = is.read(first3Bytes, 0, 3);

            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }else if (first3Bytes[0] == (byte) 0xA
                    && first3Bytes[1] == (byte) 0x5B
                    && first3Bytes[2] == (byte) 0x30) {
                charset = "UTF-8";
                checked = true;
            }else if (first3Bytes[0] == (byte) 0xD
                    && first3Bytes[1] == (byte) 0xA
                    && first3Bytes[2] == (byte) 0x5B) {
                charset = "GBK";
                checked = true;
            }else if (first3Bytes[0] == (byte) 0x5B
                    && first3Bytes[1] == (byte) 0x54
                    && first3Bytes[2] == (byte) 0x49) {
                charset = "windows-1251";
                checked = true;
            }
            //bis.reset();
            InputStream istmp = new FileInputStream(file);
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
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            is.close();
            istmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }


}
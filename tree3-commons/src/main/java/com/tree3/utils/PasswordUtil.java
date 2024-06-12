package com.tree3.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * <p>
 * MD5加盐实现方式
 * 一般使用的加盐 md5(Password+User Name),即将用户名和密码字符串相加再MD5,这样的MD5摘要基本上不可反查。
 * 但有时候用户名可能会发生变化，发生变化后密码即不可用了（验证密码实际上就是再次计算摘要的过程)。<br/>
 * 一个简单的加盐算法:每次保存密码到数据库时，都生成一个随机16位数字，将这16位数字和密码相加再求MD5摘要，
 * 然后在摘要中再将这16位数字按规则掺入形成一个48位的字符串。
 * 在验证密码时再从48位字符串中按规则提取16位数字，和用户输入的密码相加再MD5。按照
 * 这种方法形成的结果肯定是不可直接反查的，且同一个密码每次保存时形成的摘要也都是不
 * 同的。
 * 一般MD5用于对密码进行加密，比如对用户密码做MD5+加盐进行加密后保存在数据库做比对
 * 来实现密码验证。
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/6/12 18:32 </p>
 */
public class PasswordUtil {

    /**
     * 生成含有随机盐的密码
     *
     * @param password
     * @return
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验密码是否正确
     *
     * @param password
     * @param md5
     * @return
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     *
     * @param src
     * @return
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

//    public static void main(String[] args) {
//        //加密+加盐
//        String password = generate("admin");
//        System.out.println("结果：" + password + "长度：" + password.length());
//        //解码
//        System.out.println(verify("admin", password));
//        //加密+加盐
//        String password2 = generate("admin");
//        System.out.println("结果：" + password2 + "长度：" + password2.length());
//        //解码
//        System.out.println(verify("admin", password2));
//    }
}

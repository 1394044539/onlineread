package com.wpy.utils;

import com.google.common.collect.Lists;
import com.wpy.constant.CharConstant;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 13940
 */
public class StringUtils {

    private static final String atChar="@";

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static String getUuid(){
        return UUID.randomUUID().toString();
    }

    public static String getUuid32(){
        return UUID.randomUUID().toString().trim().replaceAll("-","");
    }

    public static boolean isNotBlank(Object obj){
        if(obj!=null){
            String str=obj.toString().trim();
            return str.length() != 0 ;
        }
        return false;
    }

    public static boolean isBlank(Object obj){
        return !isNotBlank(obj);
    }

    /**
     * md5加密
     * @param password
     * @param accountName
     * @return
     */
    public static String md5Encryption(String password, String accountName) {
        Md5Hash md5Hash=new Md5Hash(password,accountName,2);
        return md5Hash.toString();
    }

    /**
     * 获取文件类型
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName){
        if(StringUtils.isNotEmpty(fileName)){
            int index = fileName.lastIndexOf(CharConstant.SEPARATOR);
            if(index != -1){
                return fileName.substring(index+1);
            }
        }
        return "";
    }

    /**
     * 获取文件类型
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        if(StringUtils.isNotEmpty(fileName)){
            int index = fileName.lastIndexOf(CharConstant.SEPARATOR);
            if(index != -1){
                return fileName.substring(0,index);
            }
            return fileName;
        }
        return "";
    }

    /**
     * list转换为,字符串
     * @param list
     * @return
     */
    public static String listToCommaStr(List<String> list) {
        if(!CollectionUtils.isEmpty(list)) {
            String result="";
            for (String str : list) {
                if(StringUtils.isEmpty(result)){
                    result+=str;
                }else{
                    result+=CharConstant.COMMA+str;
                }
            }
            return result;
        }
        return "";
    }

    /**
     * ,字符串转换为list
     * @param commaStr
     * @return
     */
    public static List<String>  commaStrToList(String commaStr) {
        List<String> list= Lists.newArrayList();
        if(StringUtils.isNotEmpty(commaStr)) {
            String[] split = commaStr.split(",");
            list= Arrays.asList(split);
        }
        return list;
    }

    /**
     * 根据list获得总字数
     * @param allList
     * @return
     */
    public static Long getListLength(List<String> allList) {
        long num=0;
        for (String str : allList) {
            num+=str.length();
        }
        return num;
    }

    /**
     * 去除开头和结尾的各种空格
     * @param str
     * @return
     */
    public static String removeBlank(String str) {
        String result="";
        if(StringUtils.isNotEmpty(str)){
            result=str.trim();
            while (result.startsWith(StringUtils.unicodeToStr("\\u3000"))){
                result = result.replaceFirst("\\u3000", "");
            }
        }
        return result;
    }


    /**
     * unicode转字符串
     *
     * @param unicode
     * @return
     */
    public static String unicodeToStr(String unicode) {
        StringBuilder sb = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int index = Integer.parseInt(hex[i], 16);
            sb.append((char) index);
        }
        return sb.toString();
    }

    /**
     *去掉html
     */
    public static String removeHtml(String html) {
        String s = html.replaceAll("<[^>]*>", "");
        s.replaceAll("&nbsp","");
        return s;
    }


    static public String replaceFirst(String str, String oldStr, String newStr)
    {
        int i = str.indexOf(oldStr);
        if (i == -1){ return str;}
        str = str.substring(0, i) + newStr + str.substring(i + oldStr.length());
        return str;
    }

    static public String replaceAll(String str, String oldStr, String newStr)
    {
        int i = str.indexOf(oldStr);
        int n = 0;
        while(i != -1)
        {
            str = str.substring(0, i) + newStr + str.substring(i + oldStr.length());
            i = str.indexOf(oldStr, i + newStr.length());
            n++;
        }
        return str;
    }

    /**
     * 传入一个id或一个list，会判空之后封装到一起去返回一个list
     * @param id
     * @param ids
     * @return
     */
    public static List<String> getIdList(String id, List<String> ids) {
        List<String> list=Lists.newArrayList();
        if(!CollectionUtils.isEmpty(ids)){
            list=ids;
        }
        if(StringUtils.isNotEmpty(id)){
            list.add(id);
        }
        return list;
    }

    /**
     * 加密手机号
     * @param phone
     * @return
     */
    public static String phoneEncryption(String phone) {
        if(StringUtils.isNotEmpty(phone)){
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return phone;
    }

    /**
     * 邮箱加密
     * @param email
     * @return
     */
    public static String emailEncryption(String email){
        if(StringUtils.isNotEmpty(email)&&email.contains(atChar)&&email.length()>3){
            return email.replaceAll(email.substring(4,email.lastIndexOf(atChar)),"*****");
        }
        return email;
    }
}

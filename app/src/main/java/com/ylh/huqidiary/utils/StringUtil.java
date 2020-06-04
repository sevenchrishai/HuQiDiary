package com.ylh.huqidiary.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: yinlinhai
 * @Date: 2019/6/13
 */
public class StringUtil {

    /**
     * 剪切字符串
     * @param str
     * @param indexStr
     * @return
     */
    public static String trimStr(String str, String indexStr){
        if(str == null){
            return null;
        }
        StringBuilder newStr = new StringBuilder(str);
        if(newStr.indexOf(indexStr) == 0){
            newStr = new StringBuilder(newStr.substring(indexStr.length()));//在开头

        }else if(newStr.indexOf(indexStr) == newStr.length() - indexStr.length()){
            newStr = new StringBuilder(newStr.substring(0,newStr.lastIndexOf(indexStr)));//在结尾

        }else if(newStr.indexOf(indexStr) < (newStr.length() - indexStr.length())){
            newStr =  new StringBuilder(newStr.substring(0,newStr.indexOf(indexStr))//在中间
                    +newStr.substring(newStr.indexOf(indexStr)+indexStr.length(),newStr.length()));

        }
        return newStr.toString();
    }


    /**
     *  * 描述：获取字符串中被两个字符（串）包含的所有数据
     *  * @param str 处理字符串
     *  * @param start 起始字符（串）
     *  * @param end 结束字符（串）
     *  * @param isSpecial 起始和结束字符是否是特殊字符
     *  * @return Set<String>
     *
     */
    public static Set<String> getStrContainData(String str, String start, String end, boolean isSpecial) {
        Set<String> result = new HashSet<>();
        if (isSpecial){
            start = "\\" + start;
            end = "\\" + end;
        }
        String regex = start + "(.*?)" + end;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()) {
            String key = matcher.group(1);
            if(!key.contains(start) && !key.contains(end)) {
                result.add(key);

            }
        }
        return result;
    }
}

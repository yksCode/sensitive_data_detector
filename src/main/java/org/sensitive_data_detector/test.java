package org.sensitive_data_detector;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    private static final Pattern mobliePhonePattern = Pattern.compile("1[356789]\\d{9}");
    private static final Pattern phonePattern = Pattern.compile("0\\d{2,3}-[1-9]\\d{6,7}");
    private static final Pattern idPattern = Pattern.compile("([1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx])|([1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2})");
    private static final Pattern emailPattern = Pattern.compile("[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(?:\\.[a-zA-Z0-9_-]+)");
    private static final Pattern bankCardPattern = Pattern.compile("([1-9]{1})(\\d{15}|\\d{18})");
    private static final Pattern digitPattern = Pattern.compile("0|1|2|3|4|5|6|7|8|9");
    private static final String addressPattern = "(ns|nsf|nz|nt)";
    private static final String personNamePattern = "nr|nrf";

    private static final String s1 = "无风险";
    private static final String s2 = "低风险";
    private static final String s3 = "中风险";
    private static final String s4 = "高风险";

    public static void main(String[] args) {
//        String input = "35052119991228151"; // Replace with your input string
//        String result = autoCheckSecret(input);
//        System.out.println(result);
        String sentence = "叶开盛是华南师范大学计算机学院的学生";
        List<seg_word> words = sentenceSplit(sentence);
        for(seg_word word:words){
            System.out.println(word.word+"\t"+word.PartOfSpeech);
        }
    }

    public static String sensitiveWordRecognize(String database, String table, String[] columns, String[][] data, int index, int size) {
        int dataLen = data.length;
        size = (int) Math.ceil(dataLen / (double) size);
        int start = index * size;
        int end = Math.min((index + 1) * size, dataLen);
        StringBuilder resultStr = new StringBuilder();
        for (int i = start; i < end; i++) {
            String[] rowData = data[i];
            int k = 0;
            for (String value : rowData) {
                String[] result = {database, table, columns[k], value, autoCheckSecret(value)};
                resultStr.append(result).append("\r\n");
                k++;
            }
        }
        return resultStr.toString();
    }

    public static String checkSecret(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return value + " " + s4;
        } else {
            return value + " " + s1;
        }
    }

    public static String checkID(String value) {
        if (idPattern.matcher(value).matches() && (value.length() == 15 || value.length() == 18)) {
            return value + " 身份证 " + s4;
        } else {
            return value + " " + s1;
        }
    }

    public static String checkPhone(String value) {
        if (phonePattern.matcher(value).matches() && value.length() == 11) {
            return value + " 固话 " + s3;
        } else {
            return value + " " + s1;
        }
    }

    public static String checkMobilePhone(String value) {
        if (mobliePhonePattern.matcher(value).matches() && value.length() == 11) {
            return value + " 手机 " + s4;
        } else {
            return value + " " + s1;
        }
    }

    public static String checkEmail(String value) {
        if (emailPattern.matcher(value).matches()) {
            return value + " 邮箱 " + s3;
        } else {
            return value + " " + s1;
        }
    }

    public static String checkBankCard(String value) {
        if (bankCardPattern.matcher(value).matches() && (value.length() == 16 || value.length() == 19)) {
            return value + " 银行卡 " + s3;
        } else {
            return value + " " + s1;
        }
    }

    public static String checkChineseAddressAndName(String value) {
        String[] segText = value.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : segText) {
            if (word.matches(addressPattern)) {
                return value + " 地址 " + s4;
            }
            if (word.matches(personNamePattern)) {
                return value + " 姓名 " + s4;
            }
        }
        return result.toString();
    }

    public static String autoCheckSecret(String value) {
        if (value.length() <= 1) {
            return s1;
        }
        if (idPattern.matcher(value).matches() && (value.length() == 15 || value.length() == 18)) {
            return value + " 身份证 " + s4;
        } else if (bankCardPattern.matcher(value).matches() && (value.length() == 16 || value.length() == 19)) {
            return value + " 银行卡 " + s3;
        } else if (phonePattern.matcher(value).matches()) {
            return value + " 固话 " + s3;
        } else if (emailPattern.matcher(value).matches()) {
            return value + " 邮箱 " + s3;
        } else if (mobliePhonePattern.matcher(value).matches() && value.length() == 11) {
            return value + " 手机 " + s4;
        } else {
            String result = checkChineseAddressAndName(value);
            if (!result.isEmpty()) {
                return result;
            } else {
                return value + " " + s1;
            }
        }
    }

    public static List<seg_word> sentenceSplit(String sentence){
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        List<Term> termList = segment.seg(sentence);
        List<seg_word> segWords = new ArrayList<>();
        System.out.println(termList);
        for (Term term : termList) {
            String word = term.toString().substring(0, term.length());      //词
            String nature = term.toString().substring(term.length() + 1);   //词性
            seg_word segWord = new seg_word(word,nature);
            segWords.add(segWord);
        }
        return segWords;
    }
}


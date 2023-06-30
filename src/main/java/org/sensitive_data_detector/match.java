package org.sensitive_data_detector;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class match {
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
    private static List<List<String>> RISK;

    public match() {
        RISK = new ArrayList<>();
    }

    public static String sensitiveWordRecognize(String database, String table, String[] columns, String[][] data, int index, int size) {
        int dataLen = data.length;
        size = (int) Math.ceil(dataLen / (double) size);
        int start = index * size;
        int end = Math.min((index + 1) * size, dataLen);
        StringBuilder resultStr = new StringBuilder();
        for (int i = start; i < end; i++) {
            String[] rowData = data[i];
//            int k = 0;
//            for (String value : rowData) {
////                String[] result = {database, table, columns[k], value, autoCheckSecret(value)};
//                resultStr.append(result).append("\r\n");
//                k++;
//            }
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
        Matcher mt = idPattern.matcher(value);
        if (mt.find()) {
            return "身份证:" + mt.group() + "\t\t";
        } else {
            return s1;
        }
    }

    public static String checkPhone(String value) {
        if (phonePattern.matcher(value).matches()) {
            return " 固话 " + s3;
        } else {
            return s1;
        }
    }

    public static String checkMobilePhone(String value) {
        Matcher mt = mobliePhonePattern.matcher(value);
        if (mt.find()) {
            return "手机:" + mt.group() + "\t\t";
        } else {
            return s1;
        }
    }

    public static String checkEmail(String value) {
        Matcher mt = emailPattern.matcher(value);
        if (mt.find()) {
            return "邮箱:" + mt.group() + "\t\t";
        } else {
            return s1;
        }
    }

    public static String checkBankCard(String value) {
        Matcher mt = bankCardPattern.matcher(value);
        if (mt.find()) {
            return "银行卡:" + mt.group() + "\t\t";
        } else {
            return value + " " + s1;
        }
    }

    public static String checkChineseAddress(List<seg_word> segWords) {
        String address = "";
        for (seg_word word : segWords) {
            if(word.PartOfSpeech.matches(addressPattern)){
                address += word.word;
            }
        }
        return "地址:" + address+ "\t\t";
    }

    public static String checkChineseName(List<seg_word> segWords){
        String name = "";
        for (seg_word word : segWords) {
            if (word.PartOfSpeech.matches(personNamePattern)) {
                name += word.word + " ";
                return name;
            }
        }
        return "姓名" + name + "\t\t";
    }

    public static List<String> autoCheckSecret(String value) {
        List<seg_word> words = sentenceSplit(value);
        List<String> l = new ArrayList<>();

        //ID 银行卡和电话有固定长度，分词后匹配精确度更高
        for(seg_word word : words){
            if(word.PartOfSpeech.equals("m")) {
                if(word.word.length() == 18){
                    String ID = checkID(word.word);
                    if (!ID.equals(s1))
                        l.add(ID);
                }
                else if(word.word.length() == 11){
                    String mbPhone = checkMobilePhone(word.word);
                    if(! mbPhone.equals(s1))
                        l.add(mbPhone);;
                }
                else if(word.word.length() >=12){
                    String bankCard= checkBankCard(word.word);
                    if(! bankCard.equals(s1))
                        l.add(bankCard);
                }
            }
        }
        String email  = checkEmail(value);
        if(!email.equals(s1))
            l.add(email);
        return l;
    }

    public static List<String> checkAddressAndName(String value){
        //中文地址和中文姓名需要nlp分词检测
        List<seg_word> words = sentenceSplit(value);//分词
        String address = checkChineseAddress(words);//地址
        String name    = checkChineseName(words);//姓名
        List<String> l = new ArrayList<>();
        l.add(name);
        l.add(address);
        return l;
    }
    public static List<seg_word> sentenceSplit(String sentence){
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);//姓名识别默认开启，只需开启地址识别
        List<Term> termList = segment.seg(sentence);
        List<seg_word> segWords = new ArrayList<>();//分好的词
        for (Term term : termList) {
            String word = term.toString().substring(0, term.length());      //词
            String nature = term.toString().substring(term.length() + 1);   //词性
            seg_word segWord = new seg_word(word,nature);
            segWords.add(segWord);
        }
        return segWords;
    }
}


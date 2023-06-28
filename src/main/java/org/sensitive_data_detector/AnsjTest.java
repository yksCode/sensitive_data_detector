package org.sensitive_data_detector;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.*;

/**
 * Created by WangLei on 16-12-9.
 */
public class AnsjTest {

    public static void test() {
        //只关注这些词性的词
        Set<String> expectedNature = new HashSet<String>() {{
            add("n");add("v");add("vd");add("vn");add("vf");
            add("vx");add("vi");add("vl");add("vg");
            add("nt");add("nz");add("nw");add("nl");
            add("ng");add("userDefine");add("wh");
        }};
        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
        Result result = ToAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms
        System.out.println(result.getTerms());
        List<Term> terms = result.getTerms(); //拿到terms
        System.out.println(terms.size());

        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(expectedNature.contains(natureStr)) {
                //System.out.println(word + ":" + natureStr);
            }
        }
        Result result1 = NlpAnalysis.parse("helloWorld");
        System.out.println(result1.iterator());
        //System.out.println(ToAnalysis.parse("叶开盛是大帅哥学号2022023219号码是13055644812/350521199912281512"));
    }

    public static void main(String[] args)
    {
        String text = "中国华南师范大学叶开盛是大帅哥学号2022023219号";
        Segment sg = HanLP.newSegment().enableNameRecognize(true);
        String name = sg.seg(text).toString();
        System.out.println(name);
        Segment sg1 = HanLP.newSegment().enablePlaceRecognize(true);
        List<com.hankcs.hanlp.seg.common.Term> termList = sg1.seg(text);
        for (int i = 0; i < termList.size(); i++) {
            System.out.println(termList.get(i));
        }
//        System.out.println(termList);
    }
}

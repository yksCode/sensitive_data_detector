import org.sensitive_data_detector.match;
import org.sensitive_data_detector.seg_word;

import java.util.List;

public class TE {
    public static void main(String[] args) {
        String s1 = "17813 沈梅 女 130124199905279111 15055832484 shenmei@163.com 广东省河源市吴县一街142号-2-9左1室 1 A2 26 0.107692308 2 3 1 0.567164179 0.672727273 0.320083682 0.562208886 0.08 9 1 0 2 0.25 1 2 8 2 1 1 1 1 1 3 1 0.0004 2 1 2 3 0.652173913 null 0.591549296 null 7 272 3 2 1 1 2 2 1 null 3 2 3 3 null 1 3 1 1 2 1 2 1 null 1 3 3 1 3 2 3 null 1 1 1 2 2 1 1 3 1 0 0 0 0 0 0 1 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 5"
                + "\n17814 张秀巧 女 420702198611268838 18756000159 zhangxiuqiao@yeah.net 福建省龙岩市屏东支路124号-11-10号 1 D3 26 0.282051282 2 3 1 0.104477612 0.618181818 0.230125523 0.454733067 0.043 14 1 0 2 0.05 1 2 10 3 1 2 1 2 1 1 3 null 3 2 3 3 0.333333333 null 0.309859155 null 0 453 2 2 1 3 2 2 2 null 3 2 1 3 null 1 3 1 1 2 1 2 3 null 1 3 3 1 3 2 3 null 1 3 1 2 2 1 3 3 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 8";
        match mt = new match();
        List<seg_word> words = match.sentenceSplit(s1);
        for(seg_word word : words){
            System.out.print(word.word + word.PartOfSpeech + "    ");
        }
    }
}

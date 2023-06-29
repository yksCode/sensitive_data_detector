package org.sensitive_data_detector;
/*
*   分词生成结果的数据结构
 */
public class seg_word {
    public String word;//分好的词
    public String PartOfSpeech;//词性

    public seg_word(String word, String partofSpeech) {
        this.word = word;
        this.PartOfSpeech = partofSpeech;
    }
}

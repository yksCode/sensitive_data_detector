import org.sensitive_data_detector.DB;
import org.sensitive_data_detector.match;
import org.sensitive_data_detector.seg_word;

import java.util.List;

public class Test {
    static String ip = "127.0.0.1";
    static String db_name = "sensitive_data";
    static String user_name = "root";
    static String password = "";
    static DB db = new DB(ip,user_name,password,db_name);

    static void test(){
//        List<String> column = db.getColumn("sensitive_data","t_user_test");
//        for(String item : column){
//            System.out.println(item);
//        }
        //获取测试表中的所有数据
        List<String> table = db.getLine("sensitive_data","t_user_test");

        //识别数据库敏感字段
        match mt = new match();
        for(String col:table){
            String res = mt.autoCheckSecret(col);
            System.out.println(res);

        }

    }
    public static void main(String[] args) {
        test();
    }
}

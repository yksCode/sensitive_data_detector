import org.sensitive_data_detector.DB;
import org.sensitive_data_detector.match;
import org.sensitive_data_detector.seg_word;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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




        String res = "";
        //获取数据库名
        List<String> db_name = db.getDatabase();
        //获取表名
        List<String> table_name = db.getTable(db_name.get(0));
        //获取测试表中的所有数据
        List<String> table = db.getLine(db_name.get(0), table_name.get(0));
        //获取数据库字段名
        List<String> field = db.getColumn(db_name.get(0), table_name.get(0));
        //提取已检测的字段
        String detectedField = "";
        int count = 1;
        for(String f : field){
            detectedField += (count++) + "." + f + "  ";
        }
        //识别数据库敏感字段
        match mt = new match();
        String risk = "";
        for(String col:table){
            String parts[] = col.split(" ");
            List<String> addressAndName = mt.checkAddressAndName(col);
            addressAndName.set(0,"姓名："+parts[1]);
            List<String> otherInfo = mt.autoCheckSecret(col);
            List<String> riskInfo  = Stream.concat(addressAndName.stream(),otherInfo.stream()).collect(Collectors.toList());
            String line = "------------------------------------------------------------------------------------------------------------------------------------------------------";

            String Info = line + "\n" +
                            "database_name:" + db_name + "\n" +
                            "table_name   :" + table_name + "\n" +
                            "已检测字段：" + detectedField + "\n" + "已检测到的敏感信息：\n[";
            for(String s : riskInfo){
                Info += "\n" + " " + s;
            }
            Info += "\n]\n" + line + "\n\n";

//            写入文件
            String fileName = "Message.txt";
            try (FileWriter fileWriter = new FileWriter(fileName, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(Info);
                bufferedWriter.newLine(); // 添加换行符
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(Info);
        }
    }
    public static void main(String[] args) {
        performance();
    }

    public static void performance(){
        long startTime = System.nanoTime();
        System.out.println("10000 columns are tested");
        // 调用要测试的函数
        test();
        // 记录结束时间
        long endTime = System.nanoTime();
        // 计算函数执行时间（毫秒）
        double executionTime = (endTime - startTime) / 1_000_000.0;
        // 计算函数执行时间（秒）
        double executionTimeInSeconds = executionTime / 1000.0;
        // 精确到0.1秒
        double roundedTime = Math.round(executionTimeInSeconds * 10) / 10.0;
        System.out.println("spent " + roundedTime + "S");
    }

}

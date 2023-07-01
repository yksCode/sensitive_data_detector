# sensitive_data_detector

#### 运行结果如下：

![image-20230701083529058](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20230701083529058.png)



1.检测一段话中所包含的个人敏感信息：

```java
String sentence = "";
//实例化match类
match mt = new match();
//返回检测到的中文姓名&中文地址的列表
List<String> addressAndName = mt.checkAddressAndName(sentence);
//检测身份证、电话号码、银行卡号、邮箱等
List<String> otherInfo = mt.autoCheckSecret(sentence);
```

2.从数据库中获取信息并检测其中的敏感信息

```java
/*
*	ip:数据库ip地址，本地数据库为127.0.0.1
*	username,password:用户和密码
*	db_name:数据库名
*	
*	默认数据库为mysql，若为其他数据库则将DB.java中	*Class.forName("com.mysql.cj.jdbc.Driver")修改为其他数据库。
*/
DB db = new DB(String ip, String username, String password, String db_name);
//获取数据库名
List<String> db_name = db.getDatabase();
//获取表名
List<String> table_name = db.getTable(db_name.get(0));
//获取测试表中的所有数据
List<String> table = db.getLine(db_name.get(0), table_name.get(0));
//获取数据库字段名
 List<String> field = db.getColumn(db_name.get(0), table_name.get(0));

接下来的步骤和1一样

```

3.其他API接口

```java
/*
* match类的API
*/
//检测value字符串中的信息
public static String checkID(String value);
public static String checkPhone(String value);
public static String checkBankCard(String value);
public static String checkMobilePhone(String value);
//中文名字和中文地址识别需要调用hanlp分词
public static List<seg_word> sentenceSplit(String sentence);
public static String checkChineseName(List<seg_word> segWords);
public static String checkChineseAddress(List<seg_word> segWords);
     
```

```java
/*
* DB类的API
*/
public DB(String ip, String username, String password, String db_name);
public List<String> getDatabase();
public List<String> getTable(String database);
public List<String> getColumn(String database, String table);
public List<String> getContent(String database, String table, String column);
public List<String> getLine(String database, String table);
```


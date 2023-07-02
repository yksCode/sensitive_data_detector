# sensitive_data_detector

### 运行结果

测试代码在src/test/java/Test.java

![image-20230701083529058](https://github.com/yksCode/sensitive_data_detector/assets/48611034/c4f79551-1774-4c35-85e8-1ad22569c8d7)

### 程序流程
![流程图](https://github.com/yksCode/sensitive_data_detector/assets/48611034/a79eda2a-62a0-4582-ab2d-ea62f84a42e6)

### 检测原理

1.正则表达式：正则表达式是一种强大的文本匹配工具，它可以用来识别特定模式的字符串。首先定义一个正则表达式模式，该模式描述了要匹配的敏感信息的特征。模式可以包含普通字符（如字母和数字）以及特殊字符和元字符（如通配符、限定符、字符类等）。正则表达式引擎会根据模式的规则进行匹配。正则表达式适用于有规律的字符串，例如固定长度的身份证号、手机号、银行卡号和固定格式的邮箱地址。

2.nlp分词：基于github上的开源库hanLP，可以识别中文姓名和中文地址。此库通过对熟语料自动角色标注，统计单词的角色频次、角色的转移概率等，训练出一个模型，同时总结一些可用的模式串，然后根据上述模型，利用[HMM-Viterbi算法](http://www.hankcs.com/nlp/general-java-implementation-of-the-viterbi-algorithm.html)标注陌生文本的粗分结果，利用[Aho-Corasick算法](http://www.hankcs.com/program/algorithm/implementation-and-analysis-of-aho-corasick-algorithm-in-java.html)模式匹配，匹配出可能的地址，将其送入第二层隐马尔可夫模型中。算法详解：[《实战HMM-Viterbi角色标注中国人名识别》](http://www.hankcs.com/nlp/chinese-name-recognition-in-actual-hmm-viterbi-role-labeling.html)；[《实战HMM-Viterbi角色标注地名识别》](http://www.hankcs.com/nlp/ner/place-names-to-identify-actual-hmm-viterbi-role-labeling.html)

### API

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

3.性能测试

```java
/*
*	src/test/java/Test.java
*/
public static void performance();
```

4.数据库类和敏感信息匹配类

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

### 性能测试

1.测试平台

|        CPU         | AMD Ryzen 5 5600G    3.90 GHz |
| :----------------: | :---------------------------: |
|    **操作系统**    |     **Ubuntu 18.04 LTS**      |
|  **JDK version**   |           **20.1**            |
| **Python version** |            **3.8**            |

2.测试方法

处理**10,000**条数据**10**次的平均花销时间，处理过程包括读库、检测、写入文件。

3.性能对比

|    java    |   7.9S    |
| :--------: | :-------: |
| **python** | **34.7S** |

### 优化思路

多线程并发可能可以减少时间花销




package cn.pointer.lrl;

import cn.pointer.lrl.utils.Md5Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.pointer.lrl.generator.pojo.User;
import cn.pointer.lrl.vo.RespMsgVo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {
    public UserUtil() throws FileNotFoundException {
        /*
        测试说明：
        1. 先执行本脚本，生成数据库信息的同时生成会话存入redis
        2. 需要在登录控制器里面开启返回值
        PS：随后测试可以在数据库有数据的情况下，直接登录用户捕获登录返回会话即可
         */
    }

    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);

//        for (int i = 0; i < count; i++) {
//            User user = new User();
//            user.setId(13000000000L + i);
//            user.setUserName("user" + i);
//            user.setPassWord("b7797cce01b4b131b433b6acf4add449");
//            user.setSalt("1a2b3c4d");
//            users.add(user);
//        }

        // 插入数据库
        Connection conn = getConn();
//        String sql = "insert into t_user(user_name, salt, pass_word, id) values(?, ?, ?, ?)";
        String sql = "select * from t_user where id <> 13751378427";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs= preparedStatement.executeQuery(sql);

//        for (int i = 0; i < count; i++) {
//            User user = users.get(i);
//            preparedStatement.setString(1, user.getUserName());
//            preparedStatement.setString(2, user.getSalt());
//            preparedStatement.setString(3, user.getPassWord());
//            preparedStatement.setLong(4, user.getId());
//            // 添加到批量处理中
//            preparedStatement.addBatch();
//        }
        while(rs.next()){
            users.add(new User().setId(rs.getLong(1)).setPassWord(rs.getString(3)));
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
        conn.close();

        String urlStr = "http://localhost:8080/login/doLogin";
        File file = new File("cache/config.txt");

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        // 设置文件指针的偏移位置，以文件开头字节为单位
        raf.seek(0);
        for (User user : users) {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConn = (HttpURLConnection) url.openConnection();
            httpURLConn.setRequestMethod("POST");
            // 设置HttpURLConnection可读，设置true之后可以调用getOutputStream方法获得字节输出流
            httpURLConn.setDoOutput(true);
            OutputStream os = httpURLConn.getOutputStream();

            String param = "mobile=" + user.getId() + "&password=" + Md5Util.serverPass("123456","1a2b3c4d");
            os.write(param.getBytes());
            os.flush();

            InputStream is = httpURLConn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int len = 0;
            while ((len = is.read(buffer)) >= 0) {
                bos.write(buffer, 0, len);
            }
            is.close();
            bos.close();

            String response = bos.toString();
            ObjectMapper objectMapper = new ObjectMapper();

            RespMsgVo respMsgVo = objectMapper.readValue(response, RespMsgVo.class);
            String userTicket = (String) respMsgVo.getObject();

            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());

            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());

            System.out.println(row);
        }
    }

    private static Connection getConn() throws Exception {
        String url = "jdbc:mariadb://localhost:3306/sec_kill33";
        String username = "root";
        String password = "123456";
        String driver = "org.mariadb.jdbc.Driver";

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(10000);
    }
}

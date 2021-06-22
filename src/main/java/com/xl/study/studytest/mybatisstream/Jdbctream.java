package com.xl.study.studytest.mybatisstream;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 在test中有mybatis流进行整合查询
 * 还没找到插入的相关信息
 */

public class Jdbctream {
    private String url ="jdbc:mysql://localhost:3306/taotao?rewriteBatchedStatements=true";
    private String user ="root";
    private String password ="123";
    @Test
    public void jdbcInsert(){
        Connection conn =null;
        PreparedStatement pstm =null;
        ResultSet rt =null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url,user,password);
            String sql ="INSERT INTO tab_user2(id,user_name,age,job,sal) VALUES(null,?,60,'演员',?)";
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i =0; i <200000; i++) {
                pstm.setString(1,"周润发"+i);
                pstm.setDouble(2,300000+i);
                pstm.addBatch();
            }
            pstm.executeBatch();
            long end = System.currentTimeMillis();
            System.out.println("用时："+(end-start));
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (conn!=null){
                try {
                    conn.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstm!=null){
                try {
                    pstm.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

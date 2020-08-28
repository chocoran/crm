package com.zlz.crm.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtil {
    private static SqlSessionFactory factory;
    static{
        try {
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            InputStream in = Resources.getResourceAsStream("mybatis_config.xml");
            factory = builder.build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        SqlSession sqlSession = null;
        if (factory != null) {
            sqlSession = factory.openSession();
        }
        return sqlSession;
    }
}

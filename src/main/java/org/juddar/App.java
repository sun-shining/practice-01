package org.juddar;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.juddar.dao.UserDao;
import org.juddar.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Hello world!
 *
 */

public class App
{
    public static void main( String[] args ) throws Exception {
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession =
                sqlSessionFactory.openSession();
        UserDao userMapper = sqlSession.getMapper(UserDao.class);
        List<User> users = userMapper.selectUserList();
        for (User user : users){
            System.out.println(user.getName());
        }


    }
}

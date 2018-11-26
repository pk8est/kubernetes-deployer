package com.pkest.admin.aliyun;

import com.pkest.backend.admin.AdminApplication;
import com.pkest.backend.admin.entites.User;
import com.pkest.backend.admin.repository.UserRepository;
import com.pkest.libs.myibatis.CompareBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by wuzhonggui on 2018/4/13.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AdminApplication.class })
public class CompareBuilderTest {

    private static final Logger logger = LoggerFactory.getLogger(CompareBuilderTest.class);

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp(){
    }

    @Test
    public void insert() throws Exception {
        User user = new User();
        user.setStatus(1);
        user.setUdb("test");
        user.setRealname("test");
        user.setCtime(1);
        user.setLastLogin(1);
        long id = userRepository.insert(user);
        System.err.println(id);
        System.err.println(user);

        user.setStatus(2);
        userRepository.update(user);

        user.setStatus(3);
        System.err.println(userRepository.find(user.getManagerId()));

        System.err.println(userRepository.findOne(new CompareBuilder("status", 1)));

        System.err.println(userRepository.findAll(new CompareBuilder("status", 1, new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "manager_id")))));

    }

    @Test
    public void select() throws Exception {
        CompareBuilder builder = new CompareBuilder();
        List<User> list = userRepository.select(builder);
        System.err.println(list);
    }
}
package com.daishun.springmybatissimpleexample.dao.impl;

import com.daishun.springmybatissimpleexample.dao.BaseRepository;
import com.daishun.springmybatissimpleexample.dao.dao.UserDao;
import com.daishun.springmybatissimpleexample.model.domain.User;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author daishun
 * @since 2019/7/30
 */
@Repository
public class UserRepository extends BaseRepository<User> {

    @Resource
    private UserDao userDao;

    public List<User> getUserList() {
        return userDao.getAllUsers();
    }
}

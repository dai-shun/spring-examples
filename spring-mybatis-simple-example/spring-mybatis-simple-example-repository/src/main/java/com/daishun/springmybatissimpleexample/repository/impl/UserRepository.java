package com.daishun.springmybatissimpleexample.repository.impl;

import com.daishun.springmybatissimpleexample.repository.BaseRepository;
import com.daishun.springmybatissimpleexample.repository.dao.UserDao;
import com.daishun.springmybatissimpleexample.repository.domain.User;
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

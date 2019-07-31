package com.daishun.example.dao.impl;

import com.daishun.example.dao.BaseRepository;
import com.daishun.example.dao.dao.UserDao;
import com.daishun.example.model.domain.User;
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

    public List<User> getUserList(){
        return userDao.getAllUsers();
    }
}

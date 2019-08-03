package com.daishun.springmybatissimpleexample.dao.dao;

import com.daishun.springmybatissimpleexample.model.domain.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();
}
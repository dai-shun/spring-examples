package com.daishun.springmybatissimpleexample.repository.dao;

import com.daishun.springmybatissimpleexample.repository.domain.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();
}
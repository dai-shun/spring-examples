package com.daishun.example.dao.dao;

import com.daishun.example.model.domain.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();
}
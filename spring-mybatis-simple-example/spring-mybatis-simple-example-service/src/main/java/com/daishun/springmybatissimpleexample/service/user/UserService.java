package com.daishun.springmybatissimpleexample.service.user;

import com.daishun.springmybatissimpleexample.repository.domain.User;
import com.daishun.springmybatissimpleexample.repository.impl.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author daishun
 * @since 2019/7/31
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUserList() {
        return userRepository.getUserList();
    }

    public User getUserById(Long id) {
        return userRepository.getById(id);
    }
}

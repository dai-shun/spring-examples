package com.daishun.example.service.user;

import com.daishun.example.dao.impl.UserRepository;
import com.daishun.example.model.domain.User;
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

    public List<User> getUserList(){
        return userRepository.getUserList();
    }

    public User getUserById(Long id){
        return userRepository.getById(id);
    }
}

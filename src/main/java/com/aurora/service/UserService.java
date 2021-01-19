package com.aurora.service;

import com.aurora.entity.User;

/**
 *  用户业务层
 */
public interface UserService {
    User checkUser(String username, String password);
}

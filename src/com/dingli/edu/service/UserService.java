package com.dingli.edu.service;

import com.dingli.edu.domain.User;

import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-07 20:33
 */
public interface UserService {
    User findUserByStuCodeAndPass(String stuCode, String password);

    List<User> findManager(String searchType, String keyword);

    void saveManager(User user, String[] dormBuildIds);

    User findById(int id);

    void updateManager(User user);
}

package com.dingli.edu.dao;

import com.dingli.edu.domain.User;

import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-07 20:20
 */
public interface UserDao {
    User findUserByStuCodeAndPass(String stuCode, String password);

    List<User> findManager(String searchType, String keyword);

    String findManagerStuCodeMax();

    Integer saveManager(User user);

    User findById(int id);

    void updateManager(User user);
}

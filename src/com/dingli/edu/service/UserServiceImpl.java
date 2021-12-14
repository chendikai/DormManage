package com.dingli.edu.service;

import com.dingli.edu.dao.DormBuildDao;
import com.dingli.edu.dao.UserDao;
import com.dingli.edu.dao.impl.DormBuildDaoImpl;
import com.dingli.edu.dao.impl.UserDaoImpl;
import com.dingli.edu.domain.DormBuild;
import com.dingli.edu.domain.User;

import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-07 20:35
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();

    @Override
    public User findUserByStuCodeAndPass(String stuCode, String password) {
        return userDao.findUserByStuCodeAndPass(stuCode, password);
    }

    @Override
    public List<User> findManager(String searchType, String keyword) {
        List<User> users = userDao.findManager(searchType, keyword);

        for (User user : users) {
            List<DormBuild> builds = dormBuildDao.findByUserId(user.getId());
            user.setDormBuilds(builds);
        }
        return users;
    }

    @Override
    public void saveManager(User user, String[] dormBuildIds) {
        // 找到数据库中宿舍管理员中最大的学号+1作为当前要保存的宿舍管理员的学号
        String managerStuCodeMax = userDao.findManagerStuCodeMax();

        user.setStuCode(managerStuCodeMax);

        // 保存宿舍管理员
        Integer userId = userDao.saveManager(user);

        // 保存宿舍管理员和宿舍楼的中间表
        dormBuildDao.saveManagerAndBuild(userId,dormBuildIds);

    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public void updateManager(User user) {
        userDao.updateManager(user);
    }

}

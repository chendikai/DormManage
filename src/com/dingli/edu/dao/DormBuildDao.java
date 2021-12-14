package com.dingli.edu.dao;

import com.dingli.edu.domain.DormBuild;

import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-08 17:43
 */
public interface DormBuildDao {
    List<DormBuild> find();

    DormBuild findById(int id);

    List<DormBuild> findByUserId(Integer id);

    void saveManagerAndBuild(Integer userId, String[] dormBuildIds);

    void deleteByUserId(Integer id);
}

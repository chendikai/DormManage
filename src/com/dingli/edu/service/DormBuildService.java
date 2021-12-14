package com.dingli.edu.service;

import com.dingli.edu.domain.DormBuild;

import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-09 15:59
 */
public interface DormBuildService {
    List<DormBuild> find();

    List<DormBuild> findByUserId(Integer id);

    void deleteByUserId(Integer id);

    void saveManagerAndBuild(Integer id, String[] dormBuildIds);
}

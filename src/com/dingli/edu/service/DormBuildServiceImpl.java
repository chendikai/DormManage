package com.dingli.edu.service;

import com.dingli.edu.dao.DormBuildDao;
import com.dingli.edu.dao.impl.DormBuildDaoImpl;
import com.dingli.edu.domain.DormBuild;

import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-09 16:00
 */
public class DormBuildServiceImpl implements DormBuildService {
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();
    @Override
    public List<DormBuild> find() {
        return dormBuildDao.find();
    }

    @Override
    public List<DormBuild> findByUserId(Integer id) {
        return dormBuildDao.findByUserId(id);
    }

    @Override
    public void deleteByUserId(Integer id) {
        dormBuildDao.deleteByUserId(id);
    }

    @Override
    public void saveManagerAndBuild(Integer id, String[] dormBuildIds) {
        dormBuildDao.saveManagerAndBuild(id, dormBuildIds);
    }
}

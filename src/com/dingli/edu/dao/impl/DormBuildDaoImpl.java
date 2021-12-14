package com.dingli.edu.dao.impl;

import com.dingli.edu.dao.DormBuildDao;
import com.dingli.edu.domain.DormBuild;
import com.dingli.edu.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-08 17:44
 */
public class DormBuildDaoImpl implements DormBuildDao {
    @Override
    public List<DormBuild> find() {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "select * from tb_dormbuild ";
            //③ 获取集装箱或者说是车
            preparedStatement = connection.prepareStatement(sql);

            //④执行SQL,获取执行后的结果,查询的结果封装在ResultSet
            rs = preparedStatement.executeQuery();

            //因为查询出来的结果包括表头信息，所以要指针下移一行，看是否有查询出来的数据
            //如有数据，就进入循环体，封装该行数据
            List<DormBuild> builds = new ArrayList<DormBuild>();
            while (rs.next()) {
                DormBuild build = new DormBuild();
                build.setId(rs.getInt("id"));
                build.setName(rs.getString("name"));
                build.setDisabled(rs.getInt("disabled"));
                build.setRemark(rs.getString("remark"));

                builds.add(build);
            }
            return builds;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, preparedStatement, connection);
        }
        return null;
    }

    @Override
    public DormBuild findById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_dormbuild where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            rs = ps.executeQuery();

            while (rs.next()) {
                DormBuild dormBuild = new DormBuild();
                dormBuild.setId(rs.getInt("id"));
                dormBuild.setName(rs.getString("name"));
                dormBuild.setDisabled(rs.getInt("disabled"));
                dormBuild.setRemark(rs.getString("remark"));

                return dormBuild;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return null;
    }

    @Override
    public List<DormBuild> findByUserId(Integer id) {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "SELECT tb_dormbuild.* FROM tb_manage_dormbuild " +
                    " LEFT JOIN tb_dormbuild ON tb_dormbuild.id = tb_manage_dormbuild.dormBuild_id " +
                    " WHERE  user_id = ?";
            //③ 获取集装箱或者说是车
            preparedStatement = connection.prepareStatement(sql);
            //索引从1开始
            preparedStatement.setInt(1, id);

            //④执行SQL,获取执行后的结果,查询的结果封装在ResultSet
            rs = preparedStatement.executeQuery();

            //因为查询出来的结果包括表头信息，所以要指针下移一行，看是否有查询出来的数据
            //如有数据，就进入循环体，封装该行数据
            List<DormBuild> builds = new ArrayList<>();
            while (rs.next()) {
                DormBuild build = new DormBuild();
                build.setId(rs.getInt("id"));
                build.setName(rs.getString("name"));
                build.setDisabled(rs.getInt("disabled"));
                build.setRemark(rs.getString("remark"));

                builds.add(build);
            }
            return builds;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, preparedStatement, connection);
        }
        return null;
    }

    @Override
    public void saveManagerAndBuild(Integer userId, String[] dormBuildIds) {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "INSERT INTO tb_manage_dormbuild(USER_ID,DormBuild_id) VALUE(?,?)";
            //③ 获取集装箱或者说是车
            preparedStatement = connection.prepareStatement(sql);

            for (String dormBuildId : dormBuildIds) {
                //索引从1开始
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, Integer.parseInt(dormBuildId));

                //将sql语句添加到批处理
                preparedStatement.addBatch();
            }
            //执行批处理
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(preparedStatement, connection);
        }
    }

    @Override
    public void deleteByUserId(Integer id) {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "DELETE FROM tb_manage_dormbuild WHERE user_id =?";
            //③ 获取集装箱或者说是车
            preparedStatement = connection.prepareStatement(sql);
            //索引从1开始
            preparedStatement.setInt(1, id);

            //④执行SQL,获取执行后的结果,查询的结果封装在ResultSet
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(preparedStatement, connection);
        }
    }
}

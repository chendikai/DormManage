package com.dingli.edu.dao.impl;

import com.dingli.edu.dao.UserDao;
import com.dingli.edu.domain.User;
import com.dingli.edu.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-07 20:21
 */
public class UserDaoImpl implements UserDao {
    @Override
    public User findUserByStuCodeAndPass(String stuCode, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_user where stu_code = ? and password = ? and disabled = 0";
            ps = conn.prepareStatement(sql);

            ps.setString(1, stuCode);
            ps.setString(2, password);

            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setCreateUserId(rs.getInt("create_user_id"));
                user.setDisabled(rs.getInt("disabled"));
                user.setDormBuildId(rs.getInt("dormBuildId"));
                user.setDormCode(rs.getString("dorm_Code"));
                user.setName(rs.getString("name"));
                user.setPassWord(rs.getString("passWord"));
                user.setRoleId(rs.getInt("role_id"));
                user.setSex(rs.getString("sex"));
                user.setStuCode(rs.getString("stu_code"));
                user.setTel(rs.getString("tel"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return null;
    }

    @Override
    public List<User> findManager(String searchType, String keyword) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();
            String temp = "";
            if (null != searchType && null != keyword) {
                if ("name".equals(searchType)) {
                    temp = " and name like '%" + keyword + "'%";
                } else if ("sex".equals(searchType)) {
                    temp = " and sex = '" + keyword + "'";
                } else if ("tel".equals(searchType)) {
                    temp = " and tel = " + keyword;
                }

            }
            String sql = "select * from tb_user where role_id = 1" + temp;
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                //每一行的数据封装在一个实体bean中，根据字段名获取字段值，注意该字段是什么类型，就get什么类型
                user.setId(rs.getInt("id"));
                user.setCreateUserId(rs.getInt("create_user_id"));
                user.setDisabled(rs.getInt("disabled"));
                user.setName(rs.getString("name"));
                user.setPassWord(rs.getString("passWord"));
                user.setRoleId(rs.getInt("role_id"));
                user.setSex(rs.getString("sex"));
                user.setStuCode(rs.getString("stu_code"));
                user.setTel(rs.getString("tel"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public String findManagerStuCodeMax() {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "SELECT stu_code from tb_user WHERE id = (select MAX(id) from tb_user)";
            //③ 获取集装箱或者说是车
            preparedStatement = connection.prepareStatement(sql);
            //④执行SQL,获取执行后的结果,查询的结果封装在ResultSet
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String stu_code = rs.getString("stu_code");
                return "00" + (Integer.parseInt(stu_code) + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, preparedStatement, connection);
        }
        return null;
    }

    @Override
    public Integer saveManager(User user) {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "INSERT INTO tb_user(NAME,PASSWORD,stu_code,sex,tel,role_id,create_user_id,disabled) VALUE(?,?,?,?,?,?,?,?)";
            //③ 获取集装箱或者说是车  Statement.RETURN_GENERATED_KEYS指定返回生成的注解
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassWord());
            preparedStatement.setString(3, user.getStuCode());
            preparedStatement.setString(4, user.getSex());
            preparedStatement.setString(5, user.getTel());
            preparedStatement.setInt(6, user.getRoleId());
            preparedStatement.setInt(7, user.getCreateUserId());
            preparedStatement.setInt(8, user.getDisabled());
            //④执行SQL,获取执行后的结果,查询的结果封装在ResultSet
            preparedStatement.executeUpdate();
            rs = preparedStatement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, preparedStatement, connection);
        }
        return null;
    }

    @Override
    public User findById(int id) {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "select * from tb_user where id = ?";
            //③ 获取集装箱或者说是车
            preparedStatement = connection.prepareStatement(sql);
            //索引从1开始
            preparedStatement.setInt(1, id);

            //④执行SQL,获取执行后的结果,查询的结果封装在ResultSet
            rs = preparedStatement.executeQuery();

            //因为查询出来的结果包括表头信息，所以要指针下移一行，看是否有查询出来的数据
            //如有数据，就进入循环体，封装该行数据
            while (rs.next()) {
                User user = new User();
                //每一行的数据封装在一个实体bean中，根据字段名获取字段值，注意该字段是什么类型，就get什么类型
                user.setId(rs.getInt("id"));
                user.setCreateUserId(rs.getInt("create_user_id"));
                user.setDisabled(rs.getInt("disabled"));
                user.setName(rs.getString("name"));
                user.setPassWord(rs.getString("passWord"));
                user.setRoleId(rs.getInt("role_id"));
                user.setSex(rs.getString("sex"));
                user.setStuCode(rs.getString("stu_code"));
                user.setTel(rs.getString("tel"));
                user.setDormBuildId(rs.getInt("dormBuildId"));
                user.setDormCode(rs.getString("dorm_Code"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, preparedStatement, connection);
        }
        return null;
    }

    @Override
    public void updateManager(User user) {
        //① 获取连接（数据库地址  用户名 密码）
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DBUtils.getConnection();
            //② 准备SQL语句
            String sql = "UPDATE tb_user SET NAME = ? ,PASSWORD= ?,sex=?,tel=?,disabled= ? WHERE id = ?";

            //③ 获取集装箱或者说是车  Statement.RETURN_GENERATED_KEYS指定返回生成的注解
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassWord());
            preparedStatement.setString(3, user.getSex());
            preparedStatement.setString(4, user.getTel());
            preparedStatement.setInt(5, user.getDisabled());
            preparedStatement.setInt(6, user.getId());

            //④执行SQL,获取执行后的结果,查询的结果封装在ResultSet
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, preparedStatement, connection);
        }
    }

}

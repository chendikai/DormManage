package com.dingli.edu.controller;

import com.dingli.edu.utils.CookieUtils;
import com.dingli.edu.utils.CookieUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 陈迪凯
 * @date 2021-01-08 15:43
 */
@WebServlet("/loginOut.action")
public class LoginOutServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 清除保存在session会话中的用户信息
        req.getSession().removeAttribute("session_user");

        // 清除保存在cookie的用户信息
        Cookie cookie = CookieUtils.getCookieByName(req, "cookie_name_pass");

        if (null != cookie) {
            // 将cookie的有效时间置为0，马上失效
            cookie.setMaxAge(0);

            // 设置cookie的作用范围
            cookie.setPath(req.getContextPath());

            // 将cookie响应出去
            resp.addCookie(cookie);
        }

        // 重定向到登录页面
        resp.sendRedirect("index.jsp");
    }
}

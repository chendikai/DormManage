package com.dingli.edu.controller;

import com.dingli.edu.domain.User;
import com.dingli.edu.service.UserService;
import com.dingli.edu.service.UserServiceImpl;
import com.dingli.edu.utils.CookieUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author 陈迪凯
 * @date 2021-01-07 20:36
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    public LoginServlet() {
        super();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8"); // Tomcat 8.0 处理post请求乱码问题

        // 获取前端页面用户输入的用户信息
        String stuCode = req.getParameter("stuCode");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");

        UserService userService = new UserServiceImpl();
        User user = userService.findUserByStuCodeAndPass(stuCode, password);
        if (null == user) {
            req.setAttribute("error", "您输入的学号或密码错误！");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else {
            // 保存在session中的数据，默认是30分钟内有效。保存在session中的数据，在整个项目中都可以获取得到
            req.getSession().setAttribute("session_user", user);

            if (null != remember && remember.equals("remember-me")) {
                CookieUtils.addCookie("cookie_name_pass", 7*24*60*60, req, resp, URLEncoder.encode(stuCode, "utf-8"), URLEncoder.encode(password, "utf-8"));
            }
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        }
    }
}

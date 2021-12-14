package com.dingli.edu.controller;

import com.dingli.edu.domain.DormBuild;
import com.dingli.edu.domain.User;
import com.dingli.edu.service.DormBuildService;
import com.dingli.edu.service.DormBuildServiceImpl;
import com.dingli.edu.service.UserService;
import com.dingli.edu.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈迪凯
 * @date 2021-01-08 17:40
 */
@WebServlet("/dormManager.action")
public class DormManagerServlet extends HttpServlet {
    public DormManagerServlet() {
        super();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        String id = request.getParameter("id");

        UserService userService = new UserServiceImpl();

        DormBuildService buildService = new DormBuildServiceImpl();

        List<DormBuild> builds = buildService.find();
        request.setAttribute("builds", builds);

        if (null != action && action.equals("list")) {
            String searchType = request.getParameter("searchType");
            String keyword = request.getParameter("keyword");

            // 查询宿舍管理员
            List<User> users = userService.findManager(searchType, keyword);

            request.setAttribute("searchType", searchType);
            request.setAttribute("keyword", keyword);
            request.setAttribute("users", users);
            request.setAttribute("mainRight", "dormManagerList.jsp");
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);

        } else if (null != action && action.equals("preAdd")) {
            // 跳转到宿舍管理员添加页面
            request.setAttribute("mainRight", "dormManagerAddOrUpdate.jsp");
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
        } else if (null != action && action.equals("save")) {
            String name = request.getParameter("name");
            String passWord = request.getParameter("passWord");
            String sex = request.getParameter("sex");
            String tel = request.getParameter("tel");

            // 获取复选框中用户选中的宿舍楼
            String[] dormBuildIds = request.getParameterValues("dormBuildId");

            if (null == id || id.equals("")) {
                User user = new User(name, passWord, sex, tel, null, 1);
                user.setDisabled(0);

                // 当前登录的用户
                User user2 = (User) request.getSession().getAttribute("session_user");
                user.setCreateUserId(user2.getId());

                userService.saveManager(user, dormBuildIds);
            } else {
                //通过宿舍管理员ID获取宿舍管理员
                User user = userService.findById(Integer.parseInt(id));

                user.setName(name);
                user.setPassWord(passWord);
                user.setSex(sex);
                user.setTel(tel);

                userService.updateManager(user);

                // 修改还需修改宿舍管理员与宿舍楼的中间表
                // ① 删除当前宿舍管理员管理的所有宿舍楼
                buildService.deleteByUserId(user.getId());

                //② 新增当前宿舍管理员管理的所有宿舍楼
                buildService.saveManagerAndBuild(user.getId(), dormBuildIds);
            }

            // 跳转到宿舍管理员列表页，查看所有的宿舍管理员
            // 重定向，请求链断开，不能在下一个servlet或jsp中获取保存在request中的参数
            response.sendRedirect(getServletContext().getContextPath() + "/dormManager.action?action=list");
        } else if (null != action && action.equals("preUpdate")) {
            // 根据宿舍管理员ID，获取宿舍管理员
            User user = userService.findById(Integer.parseInt(id));

            //根据宿舍管理员ID获取宿舍管理员管理的楼栋
            List<DormBuild> userBuilds = buildService.findByUserId(user.getId());
            user.setDormBuilds(userBuilds);

            List<Integer> userBuildids = new ArrayList<>();

            // 遍历当前宿舍管理员管理的所有宿舍，获取当时当前宿舍管理员管理的所有宿舍ID
            for (DormBuild userBuild : userBuilds) {
                userBuildids.add(userBuild.getId());
            }

            request.setAttribute("userBuildids", userBuildids);
            request.setAttribute("user", user);
            request.setAttribute("mainRight", "dormManagerAddOrUpdate.jsp");
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
        } else if (null != action && action.equals("deleteOrAcive")) {
            //删除或激活
            String disabled = request.getParameter("disabled");

            //通过宿舍管理员ID获取宿舍管理员
            User user = userService.findById(Integer.parseInt(id));
            user.setDisabled(Integer.parseInt(disabled));

            userService.updateManager(user);

            //跳转到宿舍管理员列表页，查看所有的宿舍管理员
            //重定向，请求链断开，不能在下一个servlet或jsp中获取保存在request中的参数
            response.sendRedirect(getServletContext().getContextPath() + "/dormManager.action?action=list");
        }
    }
}

package com.cattle.house.controller;

import cn.hutool.core.util.ObjectUtil;
import com.cattle.house.bean.UserBean;
import com.cattle.house.response.Result;
import com.cattle.house.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户
 *
 * @author niujie
 * @date 2023/4/21 22:43
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "cattle/house/user", method = RequestMethod.POST)
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    private UserService userService;

    /**
     * 用户登录
     *
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/4/22
     */
    @RequestMapping("/login")
    public String login(@RequestBody UserBean user) {
        try {
            UserBean userBean = userService.loginIn(user);
            if (ObjectUtil.isNull(userBean)) {
                return Result.fail("用户名或密码错误！");
            }
            return Result.success("操作成功！", userBean);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 用户注销登录
     *
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/5/14
     */
    @RequestMapping("/loginOut")
    public String loginOut(@RequestBody UserBean user) {
        try {
            userService.loginOut(user);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 保存用户
     *
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/4/22
     */
    @RequestMapping("/saveUser")
    public String saveUser(@RequestBody UserBean user) {
        try {
            userService.saveUser(user);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 修改用户信息
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/4/22
     */
    @RequestMapping("/updateUser")
    public String updateUser(@RequestBody UserBean user) {
        try {
            userService.updateUser(user);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 删除用户
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/4/22
     */
    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestBody UserBean user) {
        try {
            userService.deleteUser(user);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 分页查询用户列表
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/4/22
     */
    @RequestMapping("/getUserList4Page")
    public String getUserList4Page(@RequestBody UserBean user) {
        try {
            PageInfo<UserBean> pageInfo = userService.getUserList4Page(user);
            return Result.success("操作成功！", pageInfo);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/5/7
     */
    @RequestMapping("/getUser")
    public String getUser(@RequestBody UserBean user) {
        try {
            UserBean userBean = userService.getUserByUserId(user.getUser_id());
            return Result.success("操作成功！", userBean);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 修改用户状态
     *
     * @param user user
     * @return java.lang.String
     * @author niujie
     * @date 2023/5/27
     */
    @RequestMapping("/updateUserState")
    public String updateUserState(@RequestBody UserBean user) {
        try {
            userService.updateUserState(user);
            return Result.success("操作成功！");
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询用户列表信息
     *
     * @return java.lang.String
     * @author niujie
     * @date 2023/5/27
     */
    @RequestMapping("/getUserOptions")
    public String getUserOptions() {
        try {
            List<UserBean> userBeans = userService.getUserOptions();
            return Result.success("操作成功！", userBeans);
        } catch (Exception e) {
            LOGGER.error("操作异常！", e);
            return Result.fail(e.getMessage());
        }
    }

}

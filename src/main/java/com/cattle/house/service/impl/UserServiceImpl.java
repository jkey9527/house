package com.cattle.house.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.cattle.house.bean.ContractBean;
import com.cattle.house.bean.PageBean;
import com.cattle.house.bean.UserBean;
import com.cattle.house.constant.RedisConstant;
import com.cattle.house.enums.UserStateEnum;
import com.cattle.house.mapper.ContractMapper;
import com.cattle.house.mapper.UserMapper;
import com.cattle.house.service.UserService;
import com.cattle.house.util.PageUtil;
import com.cattle.house.util.RedisUtil;
import com.cattle.house.util.UuIdUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务类
 *
 * @author niujie
 * @date 2023/4/21 22:40
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;

    private ContractMapper contractMapper;

    private RedisUtil redisUtil;

    @Override
    public UserBean loginIn(UserBean userBean) throws Exception {
        String userNo = userBean.getUser_no();
        String userPhone = userBean.getUser_phone();
        String userPassword = userBean.getUser_password();
        if ((StrUtil.isBlank(userNo) && StrUtil.isBlank(userPhone)) || StrUtil.isBlank(userPassword)) {
            throw new Exception("用户名或密码不能为空！");
        }
        UserBean user = userMapper.getUserBean(userBean);
        if (ObjectUtil.isNull(user)) {
            return null;
        }
        //设置登录过期时间
        redisUtil.set(user.getUser_id(), user.getUser_name(), RedisConstant.LOGIN_TIME_OUT);
        return user;
    }


    @Override
    public void loginOut(UserBean user) {
        if (ObjectUtil.isNull(user.getUser_id())) {
            return;
        }
        boolean hasKey = redisUtil.hasKey(user.getUser_id());
        if (hasKey) {
            redisUtil.deleteKey(user.getUser_id());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUser(UserBean userBean) throws Exception {
        try {
            List<UserBean> userList = getUserList(new UserBean());
            if (CollUtil.isNotEmpty(userList)) {
                List<UserBean> existsUserList = userList.stream().filter(v -> ObjectUtil.equal(v.getUser_no(), userBean.getUser_no())
                        || ObjectUtil.equal(v.getUser_phone(), userBean.getUser_phone())).toList();
                if (CollUtil.isNotEmpty(existsUserList)) {
                    throw new Exception("用户已存在！");
                }
            }
            ContractBean contractBean = new ContractBean();
            contractBean.setCon_no(userBean.getUser_contract_no());
            List<ContractBean> contractBeanList = contractMapper.getContractList(contractBean);
            if(CollUtil.isEmpty(contractBeanList)){
                throw new Exception("合同不存在！");
            }
            userBean.setUser_id(UuIdUtil.getUUID());
            String userIdCard = userBean.getUser_id_card();
            int size = userIdCard.length();
            String pass = userIdCard.substring(size - 6);
            userBean.setUser_password(pass);
            userMapper.saveUser(userBean);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUser(UserBean userBean) throws Exception {
        try {
            String userId = userBean.getUser_id();
            if (StrUtil.isBlank(userId)) {
                throw new Exception("参数{userId}异常");
            }
            UserBean user = getUserByUserId(userId);
            if (ObjectUtil.isNull(user)) {
                throw new Exception("删除失败，未查询到用户信息");
            }
            if (ObjectUtil.equal(user.getUser_state(), UserStateEnum.USE.getValue())) {
                throw new Exception("删除失败，请先停用，再删除");
            }
            userMapper.deleteUser(userBean);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public UserBean getUserByUserId(String userId) throws Exception {
        try {
            if (StrUtil.isBlank(userId)) {
                throw new Exception("参数{userId}异常");
            }
            return userMapper.getUserByUserId(userId);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(UserBean userBean) throws Exception {
        try {
            String userId = userBean.getUser_id();
            if (StrUtil.isBlank(userId)) {
                throw new Exception("参数{userId}异常");
            }
            UserBean user = getUserByUserId(userId);
            if (ObjectUtil.isNull(user)) {
                throw new Exception("修改失败，未查询到用户信息");
            }
            List<UserBean> userList = getUserList(new UserBean());
            if (CollUtil.isNotEmpty(userList)) {
                List<UserBean> existsUserList = userList.stream().filter(v -> (ObjectUtil.equal(v.getUser_no(), userBean.getUser_no())
                        || ObjectUtil.equal(v.getUser_phone(), userBean.getUser_phone())) && !ObjectUtil.equal(v.getUser_id(), userBean.getUser_id())).toList();
                if (CollUtil.isNotEmpty(existsUserList)) {
                    throw new Exception("用户已存在！");
                }
            }
            ContractBean contractBean = new ContractBean();
            contractBean.setCon_no(userBean.getUser_contract_no());
            List<ContractBean> contractBeanList = contractMapper.getContractList(contractBean);
            Integer userState = userBean.getUser_state();
            boolean use = ObjectUtil.equals(userState, UserStateEnum.USE.getValue());
            if(CollUtil.isEmpty(contractBeanList)&&use){
                throw new Exception("合同不存在！");
            }
            userMapper.updateUser(userBean);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<UserBean> getUserList(UserBean user) throws Exception {
        try {
            return userMapper.getUserList(user);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PageInfo<UserBean> getUserList4Page(UserBean user) throws Exception {
        try {
            PageBean pageBean = user.getPageBean();
            PageUtil.startPage(pageBean, "");
            List<UserBean> userList = userMapper.getUserList(user);
            PageInfo<UserBean> pageInfo = new PageInfo<>(userList);
            return pageInfo;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserState(UserBean user) throws Exception {
        try {
            UserBean userBean = getUserByUserId(user.getUser_id());
            if (ObjectUtil.isNull(userBean)) {
                throw new Exception("用户不存在，操作失败！");
            }
            userMapper.updateUserState(userBean);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<UserBean> getUserOptions() throws Exception {
        try {
            return userMapper.getUserOptions();
        } catch (Exception e) {
            LOGGER.error(e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 检查用户信息
     *
     * @param userBean userBean
     * @return void
     * @author niujie
     * @date 2023/4/22
     */
    private void checkUserInfo(UserBean userBean) throws Exception {
        String userNo = userBean.getUser_no();
        String userPhone = userBean.getUser_phone();
        String userPassword = userBean.getUser_password();
        if (StrUtil.isBlank(userNo) && StrUtil.isBlank(userPhone)) {
            throw new Exception("用户名/手机号不能同时不能为空！");
        }
        if (StrUtil.isBlank(userPassword)) {
            throw new Exception("密码不能为空！");
        }
    }
}

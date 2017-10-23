package com.mine.oa.service.impl;

import java.util.Map;

import com.mine.oa.dto.UserDataDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.mine.oa.dto.UserLoginDto;
import com.mine.oa.entity.UserPo;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.UserMapper;
import com.mine.oa.service.UserService;
import com.mine.oa.util.BeanUtil;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.util.Base64Utils;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public CommonResultVo<Map> login(UserLoginDto loginDto) {
        if (loginDto == null || BeanUtil.checkEmpty(loginDto, true) != null) {
            throw new InParamException("登录参数异常");
        }
        CommonResultVo<Map> resultVo = new CommonResultVo<>();
        UserPo userPo = new UserPo();
        userPo.setUserName(loginDto.getUserName());
        userPo.setPassword(DigestUtils.sha256Hex(loginDto.getPassword() + loginDto.getUserName()));
        userPo = userMapper.getByCondition(userPo);
        if (userPo == null) {
            resultVo.setCode(0);
            resultVo.setMsg("用户名或密码错误");
        } else {
            Map<String, String> map = Maps.newHashMap();
            map.put("token", Base64Utils.encodeToString(loginDto.getUserName().getBytes()));
            resultVo.success(map);
        }
        return resultVo;
    }

    @Override
    public CommonResultVo<UserPo> getByToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new InParamException("token异常");
        }
        UserPo userPo = new UserPo();
        userPo.setUserName(new String(Base64Utils.decodeFromString(token)));
        userPo = userMapper.getByCondition(userPo);
        if (userPo == null) {
            throw new InParamException("token异常");
        }
        CommonResultVo<UserPo> resultVo = new CommonResultVo<>();
        resultVo.success(userPo);
        return resultVo;
    }

    @Override
    public CommonResultVo updatePwd(String token, String oldPwd, String newPwd) {
        if (StringUtils.isBlank(token) || StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)) {
            throw new InParamException("参数异常");
        }
        UserPo userPo = new UserPo();
        String userName = new String(Base64Utils.decodeFromString(token));
        userPo.setUserName(userName);
        userPo.setPassword(DigestUtils.sha256Hex(oldPwd + userName));
        userPo = userMapper.getByCondition(userPo);
        CommonResultVo resultVo = new CommonResultVo();
        if (userPo == null) {
            resultVo.setCode(0);
            resultVo.setMsg("原始密码错误");
        } else {
            userPo = new UserPo();
            userPo.setUserName(userName);
            userPo.setPassword(DigestUtils.sha256Hex(newPwd + userName));
            if (userMapper.updatePwd(userPo) > 0) {
                resultVo.setCode(CommonResultVo.SUCCESS_CODE);
                resultVo.setMsg("密码修改成功，请重新登录。");
            } else {
                throw new InParamException("参数异常");
            }
        }
        return resultVo;
    }

    @Override
    public CommonResultVo<UserDataDto> findDataByUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            throw new InParamException("参数异常");
        }
        UserDataDto dataDto = userMapper.findDataByUserName(new String(Base64Utils.decodeFromString(userName)));
        if (dataDto == null) {
            throw new InParamException("参数异常");
        }
        CommonResultVo<UserDataDto> resultVo = new CommonResultVo<>();
        return resultVo.success(dataDto);
    }
}

package com.mine.oa.service.impl;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
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
            map.put("token", Base64Utils.encodeToString(loginDto.getPassword().getBytes()));
            resultVo.success(map);
        }
        return resultVo;
    }
}

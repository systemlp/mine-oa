package com.mine.oa.service.impl;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.mine.oa.dto.LoginInfoDTO;
import com.mine.oa.dto.UserDataDto;
import com.mine.oa.dto.UserLoginDto;
import com.mine.oa.entity.UserPO;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.UserMapper;
import com.mine.oa.service.UserService;
import com.mine.oa.util.BeanUtil;
import com.mine.oa.util.FileUtil;
import com.mine.oa.util.RsaUtil;
import com.mine.oa.vo.CommonResultVo;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public CommonResultVo<Map> login(UserLoginDto loginDto) {
        if (loginDto == null || BeanUtil.checkEmpty(loginDto, true) != null) {
            throw new InParamException("登录参数异常");
        }
        CommonResultVo<Map> resultVo = new CommonResultVo<>();
        UserPO userPo = new UserPO();
        userPo.setUserName(loginDto.getUserName());
        userPo.setPassword(DigestUtils.sha256Hex(loginDto.getPassword() + loginDto.getUserName()));
        userPo = userMapper.getByCondition(userPo);
        if (userPo == null) {
            resultVo.setCode(0);
            resultVo.setMsg("用户名或密码错误");
            return resultVo;
        }
        Map<String, String> map = Maps.newHashMap();
        String tokenPlain = userPo.getId() + " " + userPo.getUserName();
        String token = RsaUtil.encrypt(tokenPlain);
        map.put("token", token);
        return resultVo.success(map);
    }

    @Override
    public CommonResultVo<UserPO> getByToken() {
        UserPO userPo = LoginInfoDTO.get();
        userPo = userMapper.getByCondition(userPo);
        if (userPo == null) {
            throw new InParamException("token异常");
        }
        if (StringUtils.isNotBlank(userPo.getPhotoUrl())) {
            userPo.setPhotoUrl(FileUtil.getImgBase64(userPo.getPhotoUrl()));
        }
        return new CommonResultVo<UserPO>().success(userPo);
    }

    @Override
    public CommonResultVo updatePwd(String oldPwd, String newPwd) {
        if (StringUtils.isAnyBlank(oldPwd, newPwd)) {
            throw new InParamException();
        }
        UserPO userPo = LoginInfoDTO.get();
        Integer userId = userPo.getId();
        String userName = userPo.getUserName();
        userPo.setPassword(DigestUtils.sha256Hex(oldPwd + userPo.getUserName()));
        userPo = userMapper.getByCondition(userPo);
        CommonResultVo resultVo = new CommonResultVo();
        if (userPo == null) {
            resultVo.setCode(0);
            resultVo.setMsg("原始密码错误");
            return resultVo;
        }
        userPo = new UserPO();
        userPo.setId(userId);
        userPo.setUserName(userName);
        userPo.setPassword(DigestUtils.sha256Hex(newPwd + userName));
        if (userMapper.updatePwd(userPo) < 1) {
            throw new InParamException();
        }
        return resultVo.successMsg("密码修改成功，请重新登录。");
    }

    @Override
    public CommonResultVo<UserDataDto> findDataByUserName() {
        UserDataDto dataDto = userMapper.findDataByUserName(LoginInfoDTO.get().getUserName());
        if (dataDto == null) {
            throw new InParamException();
        }
        if (StringUtils.isNotBlank(dataDto.getPhotoUrl())) {
            dataDto.setPhotoUrl(FileUtil.getImgBase64(dataDto.getPhotoUrl()));
        }
        return new CommonResultVo<UserDataDto>().success(dataDto);
    }

    @Override
    public void uploadUserPhoto(MultipartFile userPhoto) {
        // 无法异步，文件上传时会先将文件临时存储在tomcat目录下，服务器响应后会马上删除该文件
        // taskExecutor.execute(() -> {
        // try {
        UserPO userPo = LoginInfoDTO.get();
        userPo.setPhotoUrl(fileUtil.uploadImg(userPhoto));
        userMapper.updatePhoto(userPo);
        // } catch (Exception e) {
        // LOGGER.error("用户头像修改失败", e);
        // }
        // });
    }
}

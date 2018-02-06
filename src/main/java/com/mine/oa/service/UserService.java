package com.mine.oa.service;

import java.util.Map;

import com.mine.oa.dto.UserDataDto;
import com.mine.oa.dto.UserLoginDto;
import com.mine.oa.entity.UserPO;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.web.multipart.MultipartFile;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface UserService {

    CommonResultVo<Map> login(UserLoginDto loginDto);

    CommonResultVo<UserPO> getByToken(String token);

    CommonResultVo updatePwd(String token, String oldPwd, String newPwd);

    CommonResultVo<UserDataDto> findDataByUserName(String userName);

    void uploadUserPhoto(String token, MultipartFile userPhoto);

}

package com.mine.oa.mapper;

import com.mine.oa.dto.UserDataDto;
import com.mine.oa.entity.UserPo;
import com.mine.oa.util.BaseMapper;

import java.util.List;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface UserMapper extends BaseMapper<UserPo> {

    List<UserPo> findAll();

    UserPo getByCondition(UserPo userPo);

    int updatePwd(UserPo userPo);

    UserDataDto findDataByUserName(String userName);

    int updatePhoto(UserPo userPo);

    List<String> findAllPhoto();

}

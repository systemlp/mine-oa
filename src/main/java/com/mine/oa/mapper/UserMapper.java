package com.mine.oa.mapper;

import com.mine.oa.dto.UserDataDto;
import com.mine.oa.entity.UserPO;
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
public interface UserMapper extends BaseMapper<UserPO> {

    List<UserPO> findAll();

    UserPO getByCondition(UserPO userPo);

    int updatePwd(UserPO userPo);

    UserDataDto findDataByUserName(String userName);

    int updatePhoto(UserPO userPo);

    List<String> findAllPhoto();

}

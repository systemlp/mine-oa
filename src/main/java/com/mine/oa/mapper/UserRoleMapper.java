package com.mine.oa.mapper;

import com.mine.oa.entity.UserRolePO;
import com.mine.oa.util.BaseMapper;
import com.mine.oa.vo.UserRoleVO;

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
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

    List<UserRoleVO> findByUserId(Integer userId);

}

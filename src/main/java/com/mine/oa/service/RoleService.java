package com.mine.oa.service;

import com.mine.oa.dto.RoleQueryDTO;
import com.mine.oa.entity.RolePO;
import com.mine.oa.vo.CommonResultVo;

import java.util.Set;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface RoleService {

    CommonResultVo insert(RolePO role);

    CommonResultVo delete(Integer id);

    CommonResultVo enable(Integer id);

    CommonResultVo update(RolePO role);

    CommonResultVo findPageByParam(RoleQueryDTO roleQuery);

    CommonResultVo findMenu(Integer id);

    CommonResultVo menuAuthorize(Integer id, Set<Integer> menuIdSet);
}

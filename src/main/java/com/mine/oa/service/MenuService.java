package com.mine.oa.service;

import com.mine.oa.entity.MenuPO;
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
public interface MenuService {

    CommonResultVo insert(MenuPO menu, String token);

    CommonResultVo delete(Integer id, String token);

    CommonResultVo update(MenuPO menu, String token);

    CommonResultVo findTree();

    CommonResultVo findAll();

    CommonResultVo findAllForUpdateParent(Integer id);

    CommonResultVo findByToken(String token);

}

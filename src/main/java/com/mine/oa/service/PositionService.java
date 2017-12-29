package com.mine.oa.service;

import com.github.pagehelper.PageInfo;
import com.mine.oa.dto.PositionDto;
import com.mine.oa.entity.PositionPo;
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
public interface PositionService {

    CommonResultVo<PageInfo<PositionPo>> findByParam(PositionDto param);

    CommonResultVo merge(PositionPo param, String token);

    CommonResultVo delete(Integer id, String token);

    CommonResultVo enable(Integer id, String token);

    CommonResultVo insert(PositionDto param, String token);

}

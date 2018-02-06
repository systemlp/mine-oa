package com.mine.oa.mapper;

import java.util.List;

import com.mine.oa.dto.PositionDto;
import com.mine.oa.entity.PositionPO;
import com.mine.oa.util.BaseMapper;

/***
 *
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface PositionMapper extends BaseMapper<PositionPO> {

    List<PositionPO> findByParam(PositionDto param);

    int merge(PositionPO param);

    int updateState(PositionPO param);

    int insert(PositionPO param);

}

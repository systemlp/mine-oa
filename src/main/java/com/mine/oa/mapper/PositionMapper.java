package com.mine.oa.mapper;

import com.mine.oa.dto.PositionDto;
import com.mine.oa.entity.PositionPo;

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
public interface PositionMapper {

    List<PositionPo> findByParam(PositionPo param);

    List<PositionDto> queryByParam(PositionDto param);

    int merge(PositionPo param);

    int updateState(PositionPo param);

    int insert(PositionPo param);

}

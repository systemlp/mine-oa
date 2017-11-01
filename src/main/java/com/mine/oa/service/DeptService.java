package com.mine.oa.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.mine.oa.dto.DeptDto;
import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.entity.DepartmentPo;
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
public interface DeptService {

    CommonResultVo<PageInfo<DeptDto>> findPageByParam(DeptQueryDto param);

    CommonResultVo<List<DepartmentPo>> findOptionalParnet(Integer id);

    CommonResultVo update(DepartmentPo param, String token);

    CommonResultVo delete(Integer id, String token);

}

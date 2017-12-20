package com.mine.oa.service;

import com.github.pagehelper.PageInfo;
import com.mine.oa.dto.EmployeeDto;
import com.mine.oa.dto.EmployeeQueryDto;
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
public interface EmployeeService {

    CommonResultVo<PageInfo<EmployeeDto>> findPageByParam(EmployeeQueryDto param);

}

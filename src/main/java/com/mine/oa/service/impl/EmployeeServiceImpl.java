package com.mine.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.dto.EmployeeDto;
import com.mine.oa.dto.EmployeeQueryDto;
import com.mine.oa.mapper.EmployeeMapper;
import com.mine.oa.service.EmployeeService;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public CommonResultVo<PageInfo<EmployeeDto>> findPageByParam(EmployeeQueryDto param) {
        PageHelper.startPage(param.getCurrent(), param.getPageSize());
        List<EmployeeDto> empList = employeeMapper.findByCondition(param);
        PageInfo<EmployeeDto> page = new PageInfo<>(empList);
        return new CommonResultVo<PageInfo<EmployeeDto>>().success(page);
    }
}

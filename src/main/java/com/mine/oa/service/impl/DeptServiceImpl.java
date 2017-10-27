package com.mine.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.dto.DeptDto;
import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.service.DeptService;
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
public class DeptServiceImpl implements DeptService {

    @Autowired
    DeptMapper deptMapper;

    @Override
    public CommonResultVo<PageInfo<DeptDto>> findPageByParam(DeptQueryDto param) {
        PageHelper.startPage(param.getCurrent(), param.getPageSize());
        List<DeptDto> deptList = deptMapper.findByParam(param);

        PageInfo<DeptDto> page = new PageInfo<>(deptList);
        return new CommonResultVo<PageInfo<DeptDto>>().success(page);
    }
}

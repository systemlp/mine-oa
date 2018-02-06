package com.mine.oa.mapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.TestBase;
import com.mine.oa.dto.DeptDto;
import com.mine.oa.entity.DepartmentPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2017-10-27.
 */
public class DeptMapperTest extends TestBase {

    @Autowired
    DeptMapper deptMapper;

    @Test
    public void findByParam() throws Exception {
        System.out.println(System.getProperty("user.dir"));
        PageHelper.startPage(1,10);
        List<DeptDto> deptList = deptMapper.findByParam(null);
        PageInfo<DeptDto> page = new PageInfo<>(deptList);
        System.out.println(page);
    }

    @Test
    public void queryByParam() throws Exception {
        PageHelper.startPage(1,1);
        DepartmentPO param = new DepartmentPO();
        param.setState(1);
        List<DepartmentPO> list = deptMapper.queryByParam(param);
        System.out.println(list);
        PageInfo<DepartmentPO> page = new PageInfo<>(list);
        System.out.println(page);
    }

}
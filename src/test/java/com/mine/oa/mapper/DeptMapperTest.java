package com.mine.oa.mapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.TestBase;
import com.mine.oa.dto.DeptDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by admin on 2017-10-27.
 */
public class DeptMapperTest extends TestBase {

    @Autowired
    DeptMapper deptMapper;

    @Test
    public void findByParam() throws Exception {
        PageHelper.startPage(-1,10);
        List<DeptDto> deptList = deptMapper.findByParam(null);
        PageInfo<DeptDto> page = new PageInfo<>(deptList);
        System.out.println(page);
    }

}
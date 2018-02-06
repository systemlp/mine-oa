package com.mine.oa.service;

import com.github.pagehelper.PageInfo;
import com.mine.oa.TestBase;
import com.mine.oa.dto.DeptDto;
import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.entity.DepartmentPO;
import com.mine.oa.vo.CommonResultVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2017-10-31.
 */
public class DeptServiceTest extends TestBase {

    @Autowired
    DeptService deptService;

    @Test
    public void findPageByParam() throws Exception {
        DeptQueryDto param = new DeptQueryDto();
        param.setCurrent(1);
        param.setPageSize(5);
        CommonResultVo<PageInfo<DeptDto>> deptPage = deptService.findPageByParam(param);
        System.out.println(deptPage.getData().getSize());
    }

    @Test
    public void findParent() throws Exception {
        CommonResultVo<List<DepartmentPO>> parent = deptService.findOptionalParnet(5);
        for (DepartmentPO departmentPo : parent.getData()) {
            System.out.println(departmentPo.getName());
        }
    }

}
package com.mine.oa.controller;

import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.service.DeptService;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    DeptService deptService;

    @RequestMapping("/findPageByParam")
    public CommonResultVo findPageByParam(@RequestBody DeptQueryDto param) {
        return deptService.findPageByParam(param);
    }

}

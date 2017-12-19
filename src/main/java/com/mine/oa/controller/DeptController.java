package com.mine.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.entity.DepartmentPo;
import com.mine.oa.service.DeptService;
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
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @PostMapping("/findPageByParam")
    public CommonResultVo findPageByParam(@RequestBody DeptQueryDto param) {
        return deptService.findPageByParam(param);
    }

    @GetMapping("/findOptional")
    public CommonResultVo findOptional() {
        return deptService.findOptional();
    }

    @GetMapping("/findOptionalParent/{id}")
    public CommonResultVo findOptionalParent(@PathVariable Integer id) {
        return deptService.findOptionalParnet(id);
    }

    @PostMapping("/merge")
    public CommonResultVo merge(@RequestBody DepartmentPo param, @RequestHeader String token) {
        return deptService.update(param, token);
    }

    @GetMapping("/delete/{id}")
    public CommonResultVo delete(@PathVariable Integer id, @RequestHeader String token) {
        return deptService.delete(id, token);
    }

    @GetMapping("/enable/{id}")
    public CommonResultVo enable(@PathVariable Integer id, @RequestHeader String token) {
        return deptService.enable(id, token);
    }

    @PostMapping("/insert")
    public CommonResultVo insert(@RequestBody DepartmentPo param, @RequestHeader String token) {
        return deptService.insert(param, token);
    }

}

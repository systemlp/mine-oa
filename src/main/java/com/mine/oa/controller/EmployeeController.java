package com.mine.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mine.oa.dto.EmployeeDto;
import com.mine.oa.dto.EmployeeQueryDto;
import com.mine.oa.service.EmployeeService;
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
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/findPageByParam")
    public CommonResultVo findPageByParam(@RequestBody EmployeeQueryDto param) {
        return employeeService.findPageByParam(param);
    }

    @PostMapping("/modify")
    public CommonResultVo modify(@RequestBody EmployeeDto param) {
        return employeeService.modify(param);
    }

    @PostMapping("/insert")
    public CommonResultVo insert(@RequestBody EmployeeDto param) {
        return employeeService.insert(param);
    }

    @GetMapping("/{id}/leave")
    public CommonResultVo leave(@PathVariable Integer id) {
        return employeeService.leave(id);
    }

    @GetMapping("/{id}/enable")
    public CommonResultVo enable(@PathVariable Integer id) {
        return employeeService.enable(id);
    }

}

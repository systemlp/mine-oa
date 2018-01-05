package com.mine.oa.controller;

import com.mine.oa.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CommonResultVo modify(@RequestBody EmployeeDto param, @RequestHeader String token) {
        return employeeService.modify(param, token);
    }

    @PostMapping("/insert")
    public CommonResultVo insert(@RequestBody EmployeeDto param, @RequestHeader String token) {
        return employeeService.insert(param, token);
    }

    @GetMapping("/{id}/leave")
    public CommonResultVo leave(@PathVariable Integer id, @RequestHeader String token) {
        return employeeService.leave(id, token);
    }

    @GetMapping("/{id}/enable")
    public CommonResultVo enable(@PathVariable Integer id, @RequestHeader String token) {
        return employeeService.enable(id, token);
    }

}

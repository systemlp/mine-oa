package com.mine.oa.controller;

import com.mine.oa.dto.RoleQueryDTO;
import com.mine.oa.entity.RolePO;
import com.mine.oa.service.RoleService;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/insert")
    public CommonResultVo insert(@RequestBody RolePO role) {
        return roleService.insert(role);
    }

    @GetMapping("/{id}/delete")
    public CommonResultVo delete(@PathVariable Integer id) {
        return roleService.delete(id);
    }

    @GetMapping("/{id}/enable")
    public CommonResultVo enable(@PathVariable Integer id) {
        return roleService.enable(id);
    }

    @PostMapping("/update")
    public CommonResultVo update(@RequestBody RolePO role) {
        return roleService.update(role);
    }

    @PostMapping("/findPageByParam")
    public CommonResultVo findPageByParam(@RequestBody RoleQueryDTO roleQuery) {
        return roleService.findPageByParam(roleQuery);
    }

    @GetMapping("/{id}/findMenu")
    public CommonResultVo findMenu(@PathVariable Integer id) {
        return roleService.findMenu(id);
    }

    @PostMapping("/{id}/menuAuthorize")
    public CommonResultVo menuAuthorize(@PathVariable Integer id, @RequestBody Set<Integer> menuIdSet) {
        return roleService.menuAuthorize(id, menuIdSet);
    }

}

package com.mine.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mine.oa.entity.MenuPO;
import com.mine.oa.service.MenuService;
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
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/insert")
    public CommonResultVo insert(@RequestBody MenuPO menu) {
        return menuService.insert(menu);
    }

    @GetMapping("/{id}/delete")
    public CommonResultVo delete(@PathVariable Integer id) {
        return menuService.delete(id);
    }

    @PostMapping("/update")
    public CommonResultVo update(@RequestBody MenuPO menu) {
        return menuService.update(menu);
    }

    @GetMapping("/findTree")
    public CommonResultVo findTree() {
        return menuService.findTree();
    }

    @GetMapping("/findAll")
    public CommonResultVo findAll() {
        return menuService.findAll();
    }

    @GetMapping("/{id}/findOptionalParent")
    public CommonResultVo findAll(@PathVariable Integer id) {
        return menuService.findAllForUpdateParent(id);
    }

    @GetMapping("/findByToken")
    public CommonResultVo findByToken() {
        return menuService.findByToken();
    }

}

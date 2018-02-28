package com.mine.oa.controller;

import com.mine.oa.dto.PageQueryDto;
import com.mine.oa.service.UserRoleService;
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
@RequestMapping("/userRole")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/{userId}/findByUserId")
    public CommonResultVo findByUserId(@PathVariable Integer userId, @RequestBody PageQueryDto pageQueryDto) {
        return userRoleService.findByUserId(userId, pageQueryDto);
    }

    @PostMapping("/{userId}/roleManage")
    public CommonResultVo roleManage(@PathVariable Integer userId, @RequestBody Set<Integer> roleIds, @RequestHeader String token) {
        return userRoleService.roleManage(userId, roleIds, token);
    }

}

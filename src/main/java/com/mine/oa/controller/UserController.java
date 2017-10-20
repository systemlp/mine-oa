package com.mine.oa.controller;

import com.mine.oa.dto.UserLoginDto;
import com.mine.oa.service.UserService;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResultVo login(@RequestBody UserLoginDto loginDto) {
        return userService.login(loginDto);
    }

    @RequestMapping(value = "/getUserByToken", method = RequestMethod.GET)
    public CommonResultVo getUserByToken(String token) {
        return userService.getByToken(token);
    }

    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public CommonResultVo updatePwd(@RequestBody Map<String, String> paramMap) {
        String token = paramMap.get("token");
        String oldPwd = paramMap.get("oldPwd");
        String newPwd = paramMap.get("newPwd");
        return userService.updatePwd(token, oldPwd, newPwd);
    }

}

package com.mine.oa.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mine.oa.dto.UserLoginDto;
import com.mine.oa.service.UserService;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public CommonResultVo login(@RequestBody UserLoginDto loginDto) {
        return userService.login(loginDto);
    }

    @GetMapping(value = "/getUserByToken")
    public CommonResultVo getUserByToken() {
        return userService.getByToken();
    }

    @PostMapping(value = "/updatePwd")
    public CommonResultVo updatePwd(@RequestBody Map<String, String> paramMap) {
        String oldPwd = paramMap.get("oldPwd");
        String newPwd = paramMap.get("newPwd");
        return userService.updatePwd(oldPwd, newPwd);
    }

    @GetMapping(value = "/findDataByUserName")
    public CommonResultVo findDataByUserName() throws Exception {
        return userService.findDataByUserName();
    }

    @PostMapping(value = "/uploadUserPhoto")
    public byte[] uploadUserPhoto(MultipartFile userPhoto) throws IOException {
        userService.uploadUserPhoto(userPhoto);
        return userPhoto.getBytes();
    }

}

package com.mine.oa.service;

import com.mine.oa.TestBase;
import com.mine.oa.dto.UserLoginDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestUserService extends TestBase {

    @Autowired
    UserService userService;

    @Test
    public void testLogin() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUserName("admin");
        loginDto.setPassword("123456");
        System.out.println(userService.login(loginDto));
    }

}

package com.mine.oa.mapper;

import com.mine.oa.TestBase;
import com.mine.oa.entity.UserPo;
import com.mine.oa.mapper.UserMapper;
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
public class TestUserMapper extends TestBase {
    @Autowired
    UserMapper userMapper;

    @Test
    public void testFindAll() {
        System.out.println(userMapper.findAll().get(0).getUserName());
    }

    @Test
    public void testUpdate() {
        UserPo userPo = new UserPo();
        userPo.setId(1);
        userPo.setEmail("");
        System.out.println(userMapper.updateByPrimaryKeySelective(userPo));
    }

    @Test
    public void testInsert() {
        UserPo userPo = new UserPo();
        userPo.setUserName("wfwww");
        userPo.setPassword("12313");
        userPo.setEmail("wfwww");
        System.out.println(userMapper.insertSelective(userPo));
        System.out.println(userPo.getId());
    }

}

package com.mine.oa.service;

import com.google.common.collect.Lists;
import com.mine.oa.TestBase;
import com.mine.oa.dto.UserLoginDto;
import com.mine.oa.util.OrderBinary;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    @Test
    public void testOrderBinary() {
        List<Integer> dataList = Lists.newArrayList(3, 2, 7, 7, 9, 6, 100, -5);
        System.out.println(StringUtils.join(dataList, ","));
        long cutTime = System.currentTimeMillis();
        OrderBinary<Integer> orderBinaryU = new OrderBinary<>(dataList);
        List<Integer> ascData = orderBinaryU.ascOrder();
        System.out.println(System.currentTimeMillis() - cutTime + " asc->" + StringUtils.join(ascData, ","));
        List<Integer> descData = orderBinaryU.descOrder();
        System.out.println("desc->" + StringUtils.join(descData, ","));
        System.out.println("min->" + orderBinaryU.min());
        System.out.println("max->" + orderBinaryU.max());
        int data = 5;
        System.out.println(data + (orderBinaryU.contain(data) ? " is found!" : " is not found!"));
        // dataList = Lists.newArrayList(3, 2, 7, 9, 6, 100, -5);
        cutTime = System.currentTimeMillis();
        dataList.sort(null);
        System.out.println(System.currentTimeMillis() - cutTime/* + "->" + StringUtils.join(ascData, ",") */);
    }

}

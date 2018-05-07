package com.mine.oa.dto;

import com.mine.oa.entity.UserPO;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class LoginInfoDTO {

    private static final ThreadLocal<UserPO> LOGIN_INFO = new ThreadLocal<>();

    public static void put(UserPO user) {
        LOGIN_INFO.set(user);
    }

    public static UserPO get() {
        return LOGIN_INFO.get();
    }

    public static void remove() {
        LOGIN_INFO.remove();
    }

}

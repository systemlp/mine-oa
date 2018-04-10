package com.mine.oa.util;

import com.mine.oa.dto.LoginInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o)
            throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            LOGGER.warn("用户未登录 ip：{}，请求地址：{}", getIpAddr(request), request.getRequestURL());
            return false;
        }
        LoginInfoDTO.put(RsaUtil.getUserByToken(token));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object o, Exception e) throws Exception {
        LoginInfoDTO.remove();
    }

    private String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = null;
        String unknown = "unknown";
        String[] headerArray = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP" };
        for (String header : headerArray) {
            ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !unknown.equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        if (StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            return request.getRemoteAddr();
        }
        return ip;
    }
}

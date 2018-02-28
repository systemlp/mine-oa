package com.mine.oa.service.impl;

import com.mine.oa.entity.UserRolePO;
import com.mine.oa.util.RsaUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.dto.PageQueryDto;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.UserRoleMapper;
import com.mine.oa.service.UserRoleService;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public CommonResultVo findByUserId(Integer userId, PageQueryDto page) {
        if (userId == null) {
            throw new InParamException();
        }
        PageHelper.startPage(page.getCurrent(), page.getPageSize());
        return new CommonResultVo<>().success(new PageInfo<>(userRoleMapper.findByUserId(userId)));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo roleManage(Integer userId, Set<Integer> roleIds, String token) {
        if (userId == null || StringUtils.isBlank(token)) {
            throw new InParamException();
        }
        UserRolePO record = new UserRolePO();
        record.setUserId(userId);
        userRoleMapper.delete(record);
        if (!CollectionUtils.isEmpty(roleIds)) {
            Date now = new Date();
            Integer loginUserId = RsaUtil.getUserByToken(token).getId();
            for (Integer roleId : roleIds) {
                record = new UserRolePO();
                record.setUserId(userId);
                record.setRoleId(roleId);
                record.setCreateTime(now);
                record.setCreateUserId(loginUserId);
                userRoleMapper.insert(record);
            }
        }
        return new CommonResultVo().successMsg("角色调整成功");
    }
}

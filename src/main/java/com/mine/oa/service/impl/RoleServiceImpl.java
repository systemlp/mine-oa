package com.mine.oa.service.impl;

import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.dto.RoleQueryDTO;
import com.mine.oa.entity.RolePO;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.RoleMapper;
import com.mine.oa.service.RoleService;
import com.mine.oa.util.RsaUtil;
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
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public CommonResultVo insert(RolePO role, String token) {
        if (role == null || StringUtils.isBlank(role.getName())) {
            throw new InParamException();
        }
        role.setCreateUserId(RsaUtil.getUserByToken(token).getId());
        if (roleMapper.insertSelective(role) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("新增成功");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo delete(Integer id, String token) {
        if (this.updateState(id, OaConstants.DELETE_STATE, token) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("删除成功");
    }

    @Override
    public CommonResultVo enable(Integer id, String token) {
        if (this.updateState(id, OaConstants.NORMAL_STATE, token) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("启用成功");
    }

    @Override
    public CommonResultVo update(RolePO role, String token) {
        if (!ObjectUtils.allNotNull(role, role.getId()) || StringUtils.isBlank(role.getName())) {
            throw new InParamException();
        }
        RolePO updateRole = new RolePO();
        updateRole.setId(role.getId());
        updateRole.setUpdateTime(new Date());
        updateRole.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        if (roleMapper.updateByPrimaryKeySelective(updateRole) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    public CommonResultVo findPageByParam(RoleQueryDTO roleQuery) {
        PageHelper.startPage(roleQuery.getCurrent(), roleQuery.getPageSize());
        RolePO role = new RolePO();
        role.setName(roleQuery.getName());
        role.setState(roleQuery.getState());
        return new CommonResultVo<>().success(new PageInfo<>(roleMapper.select(role)));
    }

    private int updateState(Integer id, Integer state, String token) {
        if (id == null || StringUtils.isBlank(token)) {
            throw new InParamException();
        }
        RolePO role = new RolePO();
        role.setId(id);
        role.setUpdateTime(new Date());
        role.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        role.setState(state);
        return roleMapper.updateByPrimaryKeySelective(role);
    }
}

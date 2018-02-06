package com.mine.oa.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.dto.EmployeeDto;
import com.mine.oa.dto.EmployeeQueryDto;
import com.mine.oa.entity.DepartmentPO;
import com.mine.oa.entity.EmployeePO;
import com.mine.oa.entity.PositionPO;
import com.mine.oa.entity.UserPO;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.mapper.EmployeeMapper;
import com.mine.oa.mapper.PositionMapper;
import com.mine.oa.mapper.UserMapper;
import com.mine.oa.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private UserMapper userMapper;
    @Value("${file.path.img}")
    private String imgPath;

    @Override
    public CommonResultVo<PageInfo<EmployeeDto>> findPageByParam(EmployeeQueryDto param) {
        PageHelper.startPage(param.getCurrent(), param.getPageSize());
        List<EmployeeDto> empList = employeeMapper.findByCondition(param);
        PageInfo<EmployeeDto> page = new PageInfo<>(empList);
        return new CommonResultVo<PageInfo<EmployeeDto>>().success(page);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo modify(EmployeeDto param, String token) {
        CommonResultVo checkResult = this.checkEmployee(param, token);
        if (checkResult != null) {
            return checkResult;
        }
        if (param.getId() == null) {
            throw new InParamException("参数异常");
        }
        UserPO loginUser = RsaUtil.getUserByToken(token);
        Date now = new Date();
        UserPO userPo = new UserPO();
        userPo.setId(param.getUserId());
        userPo.setEmail(param.getEmail());
        userPo.setUpdateUserId(loginUser.getId());
        userPo.setUpdateTime(now);
        EmployeePO employee = new EmployeePO();
        employee.setUpdateUserId(loginUser.getId());
        employee.setUpdateTime(now);
        BeanUtils.copyProperties(param, employee);
        if (userMapper.updateByPrimaryKeySelective(userPo) < 1
                && employeeMapper.updateByPrimaryKeySelective(employee) < 1) {
            throw new InParamException("参数异常");
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo insert(EmployeeDto param, String token) {
        CommonResultVo checkResult = this.checkEmployee(param, token);
        if (checkResult != null) {
            return checkResult;
        }
        UserPO queryParam = new UserPO();
        queryParam.setUserName(param.getUserName());
        if (userMapper.selectOne(queryParam) != null) {
            return new CommonResultVo().warn("用户名已存在");
        }
        UserPO loginUser = RsaUtil.getUserByToken(token);
        UserPO userPo = this.initUser(param, loginUser);
        if (userMapper.insertSelective(userPo) < 1) {
            throw new InParamException("参数异常");
        }
        EmployeePO employee = new EmployeePO();
        BeanUtils.copyProperties(param, employee);
        employee.setUserId(userPo.getId());
        employee.setCreateUserId(loginUser.getId());
        if (employeeMapper.insertSelective(employee) > 0) {
            return new CommonResultVo().successMsg("新增成功");
        }
        throw new InParamException("参数异常");
    }

    @Override
    @Transactional
    public CommonResultVo leave(Integer id, String token) {
        changeState(id, token, OaConstants.DELETE_STATE);
        return new CommonResultVo().successMsg("离职成功，对应用户已失效");
    }

    @Override
    @Transactional
    public CommonResultVo enable(Integer id, String token) {
        changeState(id, token, OaConstants.NORMAL_STATE);
        return new CommonResultVo().successMsg("恢复成功，对应用户已重新启用");
    }

    private CommonResultVo checkEmployee(EmployeeDto param, String token) {
        if (param == null
                || StringUtils.isAnyBlank(param.getUserName(), param.getEmail(), param.getName(), param.getCardNo(),
                        param.getMobile(), token)
                || !ObjectUtils.allNotNull(param.getSex(), param.getCardType(), param.getEntryDate(), param.getDeptId(),
                        param.getPositionId())) {
            throw new InParamException("参数异常");
        }
        DepartmentPO dept = deptMapper.selectByPrimaryKey(param.getDeptId());
        if (dept == null) {
            throw new InParamException(String.format("dept id不存在%s", param.getDeptId()));
        }
        if (OaConstants.DELETE_STATE == dept.getState()) {
            return new CommonResultVo().warn("啊哦，在您操作期间所属部门已被删除啦");
        }
        PositionPO posi = positionMapper.selectByPrimaryKey(param.getPositionId());
        if (posi == null) {
            throw new InParamException(String.format("position id不存在%s", param.getPositionId()));
        }
        if (OaConstants.DELETE_STATE == posi.getState()) {
            return new CommonResultVo().warn("啊哦，在您操作期间职位已被删除啦");
        }
        return null;
    }

    private UserPO initUser(EmployeeDto param, UserPO loginUser) {
        UserPO userPo = new UserPO();
        userPo.setUserName(param.getUserName());
        userPo.setPassword(DigestUtils.sha256Hex(OaConstants.DEFAULT_PASSWORD + param.getUserName()));
        userPo.setEmail(param.getEmail());
        userPo.setPhotoUrl(imgPath + OaConstants.DEFAULT_PHOTO);
        userPo.setCreateUserId(loginUser.getId());
        return userPo;
    }

    private void changeState(Integer id, String token, int state) {
        if (id == null || id < 1 || StringUtils.isBlank(token)) {
            throw new InParamException("参数异常");
        }
        EmployeePO emp = employeeMapper.selectByPrimaryKey(id);
        if (emp == null) {
            throw new InParamException(String.format("employee id不存在%s", id));
        }
        UserPO user = userMapper.selectByPrimaryKey(emp.getUserId());
        if (user == null) {
            throw new InParamException(String.format("user id不存在%s", emp.getUserId()));
        }
        Date now = new Date();
        UserPO loginUser = RsaUtil.getUserByToken(token);
        EmployeePO leaveEmp = new EmployeePO();
        leaveEmp.setId(id);
        leaveEmp.setUpdateTime(now);
        leaveEmp.setUpdateUserId(loginUser.getId());
        leaveEmp.setState(state);
        UserPO leaveUser = new UserPO();
        leaveUser.setId(emp.getUserId());
        leaveUser.setUpdateTime(now);
        leaveUser.setUpdateUserId(loginUser.getId());
        leaveUser.setState(state);
        if (userMapper.updateByPrimaryKeySelective(leaveUser) < 1
                || employeeMapper.updateByPrimaryKeySelective(leaveEmp) < 1) {
            throw new InParamException("参数异常");
        }
    }

}

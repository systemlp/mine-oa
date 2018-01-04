package com.mine.oa.service.impl;

import java.util.Date;
import java.util.List;

import com.mine.oa.entity.UserPo;
import com.mine.oa.mapper.UserMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.dto.EmployeeDto;
import com.mine.oa.dto.EmployeeQueryDto;
import com.mine.oa.entity.DepartmentPo;
import com.mine.oa.entity.EmployeePo;
import com.mine.oa.entity.PositionPo;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.mapper.EmployeeMapper;
import com.mine.oa.mapper.PositionMapper;
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
        if (param == null
                || StringUtils.isAnyBlank(param.getUserName(), param.getEmail(), param.getName(), param.getCardNo(),
                        param.getMobile(), token)
                || !ObjectUtils.allNotNull(param.getSex(), param.getCardType(), param.getEntryDate(), param.getDeptId(),
                        param.getPositionId())) {
            throw new InParamException("参数异常");
        }
        DepartmentPo dept = deptMapper.selectByPrimaryKey(param.getDeptId());
        if (dept == null) {
            throw new InParamException("参数异常");
        }
        if (OaConstants.DELETE_STATE == dept.getState()) {
            return new CommonResultVo().warn("啊哦，在您操作期间所属部门已被删除啦");
        }
        PositionPo posi = positionMapper.selectByPrimaryKey(param.getPositionId());
        if (posi == null) {
            throw new InParamException("参数异常");
        }
        if (OaConstants.DELETE_STATE == posi.getState()) {
            return new CommonResultVo().warn("啊哦，在您操作期间职位已被删除啦");
        }
        Date now = new Date();
        EmployeePo employee = new EmployeePo();
        employee.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        employee.setUpdateTime(now);
        BeanUtils.copyProperties(param, employee);
        UserPo userPo = new UserPo();
        userPo.setId(param.getUserId());
        userPo.setEmail(param.getEmail());
        userPo.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        userPo.setUpdateTime(now);
        if (employeeMapper.updateByPrimaryKeySelective(employee) > 0
                && userMapper.updateByPrimaryKeySelective(userPo) > 0) {
            return new CommonResultVo().successMsg("修改成功");
        }
        throw new InParamException("参数异常");
    }
}

package com.mine.oa.service.impl;

import java.util.List;

import com.mine.oa.entity.EmployeePo;
import com.mine.oa.util.RsaUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.dto.EmployeeDto;
import com.mine.oa.dto.EmployeeQueryDto;
import com.mine.oa.dto.PositionDto;
import com.mine.oa.entity.DepartmentPo;
import com.mine.oa.entity.PositionPo;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.mapper.EmployeeMapper;
import com.mine.oa.mapper.PositionMapper;
import com.mine.oa.service.EmployeeService;
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
        DepartmentPo queryDeptParam = new DepartmentPo();
        queryDeptParam.setId(param.getDeptId());
        List<DepartmentPo> deptList = deptMapper.queryByParam(queryDeptParam);
        if (CollectionUtils.isEmpty(deptList)) {
            throw new InParamException("参数异常");
        }
        if (OaConstants.DELETE_STATE == deptList.get(0).getState()) {
            return new CommonResultVo().warn("啊哦，在您操作期间所属部门已被删除啦");
        }
        PositionDto queryPosiParam = new PositionDto();
        queryPosiParam.setId(param.getPositionId());
        List<PositionPo> posiList = positionMapper.findByParam(queryPosiParam);
        if (CollectionUtils.isEmpty(posiList)) {
            throw new InParamException("参数异常");
        }
        if (OaConstants.DELETE_STATE == posiList.get(0).getState()) {
            return new CommonResultVo().warn("啊哦，在您操作期间职位已被删除啦");
        }
        EmployeePo employee = new EmployeePo();
        employee.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        BeanUtils.copyProperties(param, employee);
        if (employeeMapper.modify(employee) > 0) {
            // throw new RuntimeException();
            return new CommonResultVo().successMsg("修改成功");
        }
        throw new InParamException("参数异常");
    }
}

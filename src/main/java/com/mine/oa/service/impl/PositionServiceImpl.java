package com.mine.oa.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.dto.PositionDto;
import com.mine.oa.entity.DepartmentPo;
import com.mine.oa.entity.EmployeePo;
import com.mine.oa.entity.PositionPo;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.mapper.EmployeeMapper;
import com.mine.oa.mapper.PositionMapper;
import com.mine.oa.service.PositionService;
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
public class PositionServiceImpl implements PositionService {

    // private static final Logger LOGGER = LoggerFactory.getLogger(PositionServiceImpl.class);

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public CommonResultVo<PageInfo<PositionDto>> querybyParam(PositionDto param) {
        PageHelper.startPage(param.getCurrent(), param.getPageSize());
        List<PositionDto> deptList = positionMapper.queryByParam(param);
        PageInfo<PositionDto> page = new PageInfo<>(deptList);
        return new CommonResultVo<PageInfo<PositionDto>>().success(page);
    }

    @Override
    public CommonResultVo merge(PositionPo param, String token) {
        if (param == null || StringUtils.isAnyBlank(param.getName(), token)) {
            throw new InParamException("参数异常");
        }
        DepartmentPo queryParam = new DepartmentPo();
        queryParam.setId(param.getDeptId());
        List<DepartmentPo> parentDept = deptMapper.queryByParam(queryParam);
        if (CollectionUtils.isEmpty(parentDept)) {
            throw new InParamException("参数异常");
        }
        if (parentDept.get(0).getState() == OaConstants.DELETE_STATE) {
            return new CommonResultVo().warn("啊哦，在您操作期间所属部门被删除了，请重新选择吧-_-");
        }
        param.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        if (positionMapper.merge(param) < 1) {
            throw new RuntimeException();
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    public CommonResultVo delete(Integer id, String token) {
        if (id == null || StringUtils.isBlank(token)) {
            throw new InParamException("参数异常");
        }
        EmployeePo employeePo = new EmployeePo();
        employeePo.setPositionId(id);
        employeePo.setState(OaConstants.NORMAL_STATE);
        if (!CollectionUtils.isEmpty(employeeMapper.findByParam(employeePo))) {
            return new CommonResultVo().warn("无法删除，该职位下存在员工！");
        }
        PositionPo param = new PositionPo();
        param.setId(id);
        param.setState(OaConstants.DELETE_STATE);
        param.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        if (positionMapper.updateState(param) < 1) {
            throw new InParamException("参数异常");
        }
        return new CommonResultVo().successMsg("删除成功");
    }

    @Override
    public CommonResultVo enable(Integer id, String token) {
        if (id == null || StringUtils.isBlank(token)) {
            throw new InParamException("参数异常");
        }
        PositionPo param = new PositionPo();
        param.setId(id);
        param.setState(OaConstants.NORMAL_STATE);
        param.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        if (positionMapper.updateState(param) < 1) {
            throw new InParamException("参数异常");
        }
        return new CommonResultVo().successMsg("启用成功");
    }

    @Override
    public CommonResultVo insert(PositionPo param, String token) {
        if (param == null || StringUtils.isAnyBlank(param.getName(), token) || param.getDeptId() == null) {
            throw new InParamException("参数异常");
        }
        PositionPo queryParam = new PositionPo();
        queryParam.setName(param.getName());
        if (!CollectionUtils.isEmpty(positionMapper.findByParam(queryParam))) {
            return new CommonResultVo().warn("已存在相同名称职位");
        }
        DepartmentPo queryDeptParam = new DepartmentPo();
        queryDeptParam.setId(param.getDeptId());
        List<DepartmentPo> dept = deptMapper.queryByParam(queryDeptParam);
        if (CollectionUtils.isEmpty(dept)) {
            throw new InParamException("参数异常");
        }
        if (dept.get(0).getState() == OaConstants.DELETE_STATE) {
            return new CommonResultVo().warn("啊哦，在您操作期间所属部门被删除了，请重新选择吧-_-");
        }
        param.setCreateUserId(RsaUtil.getUserByToken(token).getId());
        if (positionMapper.insert(param) < 1) {
            throw new InParamException("参数异常");
        }
        return new CommonResultVo().successMsg("新增成功");
    }
}

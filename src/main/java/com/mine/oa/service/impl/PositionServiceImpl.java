package com.mine.oa.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.dto.LoginInfoDTO;
import com.mine.oa.dto.PositionDto;
import com.mine.oa.entity.EmployeePO;
import com.mine.oa.entity.PositionPO;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.mapper.EmployeeMapper;
import com.mine.oa.mapper.PositionMapper;
import com.mine.oa.service.PositionService;
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
    public CommonResultVo<PageInfo<PositionPO>> findByParam(PositionDto param) {
        PageHelper.startPage(param.getCurrent(), param.getPageSize());
        List<PositionPO> deptList = positionMapper.findByParam(param);
        PageInfo<PositionPO> page = new PageInfo<>(deptList);
        return new CommonResultVo<PageInfo<PositionPO>>().success(page);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo merge(PositionPO param) {
        if (param == null || StringUtils.isAnyBlank(param.getName())) {
            throw new InParamException();
        }
        PositionDto positionDto = new PositionDto();
        positionDto.setName(param.getName());
        if (!CollectionUtils.isEmpty(positionMapper.findByParam(positionDto))) {
            return new CommonResultVo().warn("已存在相同名称职位");
        }
        param.setUpdateUserId(LoginInfoDTO.get().getId());
        if (positionMapper.merge(param) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo delete(Integer id) {
        if (id == null) {
            throw new InParamException();
        }
        EmployeePO employeePo = new EmployeePO();
        employeePo.setPositionId(id);
        employeePo.setState(OaConstants.NORMAL_STATE);
        if (!CollectionUtils.isEmpty(employeeMapper.findByParam(employeePo))) {
            return new CommonResultVo().warn("无法删除，该职位下存在员工！");
        }
        PositionPO param = new PositionPO();
        param.setId(id);
        param.setState(OaConstants.DELETE_STATE);
        param.setUpdateUserId(LoginInfoDTO.get().getId());
        if (positionMapper.updateState(param) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("删除成功");
    }

    @Override
    public CommonResultVo enable(Integer id) {
        if (id == null) {
            throw new InParamException();
        }
        PositionPO param = new PositionPO();
        param.setId(id);
        param.setState(OaConstants.NORMAL_STATE);
        param.setUpdateUserId(LoginInfoDTO.get().getId());
        if (positionMapper.updateState(param) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("启用成功");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo insert(PositionDto param) {
        if (param == null || StringUtils.isAnyBlank(param.getName())) {
            throw new InParamException();
        }
        if (!CollectionUtils.isEmpty(positionMapper.findByParam(param))) {
            return new CommonResultVo().warn("已存在相同名称职位");
        }
        PositionPO position = new PositionPO();
        position.setName(param.getName());
        position.setCreateUserId(LoginInfoDTO.get().getId());
        if (positionMapper.insert(position) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("新增成功");
    }
}

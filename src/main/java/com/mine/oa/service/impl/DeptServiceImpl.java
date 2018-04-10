package com.mine.oa.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.dto.DeptDto;
import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.dto.LoginInfoDTO;
import com.mine.oa.entity.DepartmentPO;
import com.mine.oa.entity.EmployeePO;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.mapper.EmployeeMapper;
import com.mine.oa.service.DeptService;
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
public class DeptServiceImpl implements DeptService {

    // private static final Logger LOGGER = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public CommonResultVo<PageInfo<DeptDto>> findPageByParam(DeptQueryDto param) {
        PageHelper.startPage(param.getCurrent(), param.getPageSize());
        List<DeptDto> deptList = deptMapper.findByParam(param);
        PageInfo<DeptDto> page = new PageInfo<>(deptList);
        return new CommonResultVo<PageInfo<DeptDto>>().success(page);
    }

    @Override
    public CommonResultVo<List<DepartmentPO>> findOptionalParnet(Integer id) {
        if (id == null) {
            throw new InParamException();
        }
        DepartmentPO param = new DepartmentPO();
        param.setState(OaConstants.NORMAL_STATE);
        // Example example = new Example(DepartmentPo.class);
        // example.createCriteria().andEqualTo("state", OaConstants.NORMAL_STATE);
        List<DepartmentPO> deptList = deptMapper.select(param);
        param.setId(id);
        Map<Integer, DepartmentPO> deptMap = Maps.newHashMap();
        for (DepartmentPO departmentPo : deptList) {
            if (id.compareTo(departmentPo.getId()) != 0) {
                deptMap.put(departmentPo.getId(), departmentPo);
            }
        }
        List<Integer> notIdList = findNotOptionalParnetId(id, deptList);
        for (Integer notId : notIdList) {
            deptMap.remove(notId);
        }
        return new CommonResultVo<List<DepartmentPO>>().success(Lists.newArrayList(deptMap.values()));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo update(DepartmentPO param) {
        if (param == null || StringUtils.isAnyBlank(param.getName())) {
            throw new InParamException();
        }
        if (deptMapper.getNameCount(param) > 0) {
            return new CommonResultVo().warn("已存在相同名称部门");
        }
        DepartmentPO parentDept = deptMapper.selectByPrimaryKey(param.getParentId());
        if (parentDept == null) {
            throw new InParamException();
        }
        if (parentDept.getState() == OaConstants.DELETE_STATE) {
            return new CommonResultVo().warn("啊哦，在您操作期间父级部门被删除了，请重新选择吧-_-");
        }
        param.setUpdateUserId(LoginInfoDTO.get().getId());
        if (deptMapper.merge(param) < 1) {
            throw new RuntimeException();
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo delete(Integer id) {
        DepartmentPO param = new DepartmentPO();
        param.setParentId(id);
        param.setState(OaConstants.NORMAL_STATE);
        List<DepartmentPO> deptList = deptMapper.select(param);
        param.setId(id);
        String msg = "";
        if (!CollectionUtils.isEmpty(deptList)) {
            msg = "无法删除，该部门下存在子部门";
        }
        EmployeePO employeePo = new EmployeePO();
        employeePo.setDeptId(id);
        employeePo.setState(OaConstants.NORMAL_STATE);
        if (!CollectionUtils.isEmpty(employeeMapper.findByParam(employeePo))) {
            if (StringUtils.isBlank(msg)) {
                msg = "无法删除，该职位下存在员工";
            } else {
                msg += "及员工";
            }
        }
        if (StringUtils.isNotBlank(msg)) {
            return new CommonResultVo().warn(msg + "！");
        }
        param.setState(OaConstants.DELETE_STATE);
        param.setUpdateUserId(LoginInfoDTO.get().getId());
        if (deptMapper.updateState(param) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("删除成功");
    }

    @Override
    public CommonResultVo enable(Integer id) {
        DepartmentPO param = new DepartmentPO();
        param.setId(id);
        param.setState(OaConstants.NORMAL_STATE);
        param.setUpdateUserId(LoginInfoDTO.get().getId());
        if (deptMapper.updateState(param) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("启用成功");
    }

    @Override
    public CommonResultVo<List<DepartmentPO>> findOptional() {
        DepartmentPO param = new DepartmentPO();
        param.setState(OaConstants.NORMAL_STATE);
        List<DepartmentPO> deptList = deptMapper.select(param);
        return new CommonResultVo<List<DepartmentPO>>().success(deptList);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo insert(DepartmentPO param) {
        if (param == null || StringUtils.isAnyBlank(param.getName())) {
            throw new InParamException();
        }
        DepartmentPO queryParam = new DepartmentPO();
        queryParam.setName(param.getName());
        if (deptMapper.getNameCount(queryParam) > 0) {
            return new CommonResultVo().warn("已存在相同名称部门");
        }
        queryParam.setName(null);
        queryParam.setId(param.getParentId());
        List<DepartmentPO> parentDept = deptMapper.queryByParam(queryParam);
        if (CollectionUtils.isEmpty(parentDept)) {
            throw new InParamException();
        }
        if (parentDept.get(0).getState() == OaConstants.DELETE_STATE) {
            return new CommonResultVo().warn("啊哦，在您操作期间父级部门被删除了，请重新选择吧-_-");
        }
        param.setCreateUserId(LoginInfoDTO.get().getId());
        if (deptMapper.insert(param) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("新增成功");
    }

    /**
     * 获取当前部门id不可选父级部门id
     * 
     * @param id 当前部门id
     * @param deptList 部门
     * @return 不可选父级部门id
     */
    private List<Integer> findNotOptionalParnetId(Integer id, List<DepartmentPO> deptList) {
        List<Integer> idList = Lists.newArrayList();
        for (DepartmentPO departmentPo : deptList) {
            if (departmentPo.getParentId() == null || id.compareTo(departmentPo.getParentId()) != 0) {
                continue;
            }
            idList.add(departmentPo.getId());
            idList.addAll(findNotOptionalParnetId(departmentPo.getId(), deptList));
        }
        return idList;
    }

}

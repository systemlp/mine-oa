package com.mine.oa.service.impl;

import java.util.List;
import java.util.Map;

import com.mine.oa.constant.OaConstants;
import com.mine.oa.entity.PositionPo;
import com.mine.oa.mapper.PositionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mine.oa.dto.DeptDto;
import com.mine.oa.dto.DeptQueryDto;
import com.mine.oa.entity.DepartmentPo;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.DeptMapper;
import com.mine.oa.service.DeptService;
import com.mine.oa.util.RsaUtil;
import com.mine.oa.vo.CommonResultVo;
import org.springframework.util.CollectionUtils;

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
    private PositionMapper positionMapper;

    @Override
    public CommonResultVo<PageInfo<DeptDto>> findPageByParam(DeptQueryDto param) {
        PageHelper.startPage(param.getCurrent(), param.getPageSize());
        List<DeptDto> deptList = deptMapper.findByParam(param);
        PageInfo<DeptDto> page = new PageInfo<>(deptList);
        return new CommonResultVo<PageInfo<DeptDto>>().success(page);
    }

    @Override
    public CommonResultVo<List<DepartmentPo>> findOptionalParnet(Integer id) {
        if (id == null) {
            throw new InParamException("参数异常");
        }
        DepartmentPo param = new DepartmentPo();
        param.setState(OaConstants.NORMAL_STATE);
        List<DepartmentPo> deptList = deptMapper.queryByParam(param);
        param.setId(id);
        Map<Integer, DepartmentPo> deptMap = Maps.newHashMap();
        for (DepartmentPo departmentPo : deptList) {
            if (id.compareTo(departmentPo.getId()) != 0) {
                deptMap.put(departmentPo.getId(), departmentPo);
            }
        }
        List<Integer> notIdList = findNotOptionalParnetId(id, deptList);
        for (Integer notId : notIdList) {
            deptMap.remove(notId);
        }
        return new CommonResultVo<List<DepartmentPo>>().success(Lists.newArrayList(deptMap.values()));
    }

    @Override
    public CommonResultVo update(DepartmentPo param, String token) {
        if (param == null || StringUtils.isAnyBlank(param.getName(), token)) {
            throw new InParamException("参数异常");
        }
        DepartmentPo queryParam = new DepartmentPo();
        queryParam.setId(param.getParentId());
        List<DepartmentPo> parentDept = deptMapper.queryByParam(queryParam);
        if(CollectionUtils.isEmpty(parentDept)){
            throw new InParamException("参数异常");
        }
        if(parentDept.get(0).getState() == OaConstants.DELETE_STATE){
            return new CommonResultVo().warn("啊哦，在您操作期间父级部门被删除了，请重新选择吧-_-");
        }
        param.setUpdateUserId(RsaUtil.getUserIdByToken(token));
        if (deptMapper.merge(param) < 1) {
            throw new RuntimeException();
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    public CommonResultVo delete(Integer id, String token) {
        if (id == null || StringUtils.isBlank(token)) {
            throw new InParamException("参数异常");
        }
        DepartmentPo param = new DepartmentPo();
        param.setParentId(id);
        param.setState(OaConstants.NORMAL_STATE);
        List<DepartmentPo> deptList = deptMapper.queryByParam(param);
        param.setId(id);
        String msg = "";
        if (!CollectionUtils.isEmpty(deptList)) {
            msg = "无法删除，该部门下存在子部门";
        }
        PositionPo positionPo = new PositionPo();
        positionPo.setDeptId(id);
        positionPo.setState(OaConstants.NORMAL_STATE);
        if (!CollectionUtils.isEmpty(positionMapper.findByParam(positionPo))) {
            if (StringUtils.isBlank(msg)) {
                msg = "无法删除，该部门下存在职位";
            } else {
                msg += "及职位";
            }
        }
        if (StringUtils.isNotBlank(msg)) {
            return new CommonResultVo().warn(msg + "！");
        }
        param.setState(OaConstants.DELETE_STATE);
        param.setUpdateUserId(RsaUtil.getUserIdByToken(token));
        if (deptMapper.updateState(param) < 1) {
            throw new InParamException("参数异常");
        }
        return new CommonResultVo().successMsg("删除成功");
    }

    @Override
    public CommonResultVo enable(Integer id, String token) {
        if (id == null || StringUtils.isBlank(token)) {
            throw new InParamException("参数异常");
        }
        DepartmentPo param = new DepartmentPo();
        param.setId(id);
        param.setState(OaConstants.NORMAL_STATE);
        param.setUpdateUserId(RsaUtil.getUserIdByToken(token));
        if (deptMapper.updateState(param) < 1) {
            throw new InParamException("参数异常");
        }
        return new CommonResultVo().successMsg("启用成功");
    }

    @Override
    public CommonResultVo<List<DepartmentPo>> findOptional() {
        DepartmentPo param = new DepartmentPo();
        param.setState(OaConstants.NORMAL_STATE);
        List<DepartmentPo> deptList = deptMapper.queryByParam(param);
        return new CommonResultVo<List<DepartmentPo>>().success(deptList);
    }

    @Override
    public CommonResultVo insert(DepartmentPo param, String token) {
        if (param == null || StringUtils.isAnyBlank(param.getName(), token)) {
            throw new InParamException("参数异常");
        }
        DepartmentPo queryParam = new DepartmentPo();
        queryParam.setName(param.getName());
        if (!CollectionUtils.isEmpty(deptMapper.queryByParam(queryParam))) {
            return new CommonResultVo().warn("已存在相同名称部门");
        }
        queryParam.setName(null);
        queryParam.setId(param.getParentId());
        List<DepartmentPo> parentDept = deptMapper.queryByParam(queryParam);
        if(CollectionUtils.isEmpty(parentDept)){
            throw new InParamException("参数异常");
        }
        if(parentDept.get(0).getState() == OaConstants.DELETE_STATE){
            return new CommonResultVo().warn("啊哦，在您操作期间父级部门被删除了，请重新选择吧-_-");
        }
        param.setCreateUserId(RsaUtil.getUserIdByToken(token));
        if (deptMapper.insert(param) < 1) {
            throw new InParamException("参数异常");
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
    private List<Integer> findNotOptionalParnetId(Integer id, List<DepartmentPo> deptList) {
        List<Integer> idList = Lists.newArrayList();
        for (DepartmentPo departmentPo : deptList) {
            if (departmentPo.getParentId() == null || id.compareTo(departmentPo.getParentId()) != 0) {
                continue;
            }
            idList.add(departmentPo.getId());
            idList.addAll(findNotOptionalParnetId(departmentPo.getId(), deptList));
        }
        return idList;
    }

}

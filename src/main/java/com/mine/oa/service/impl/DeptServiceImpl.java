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
        param.setId(id);
        param.setState(OaConstants.NORMAL_STATE);
        List<DepartmentPo> deptList = deptMapper.queryByParam(param);
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
        CommonResultVo<List<DepartmentPo>> resultVo = new CommonResultVo<>();
        return resultVo.success(Lists.newArrayList(deptMap.values()));
    }

    @Override
    public CommonResultVo update(DepartmentPo param, String token) {
        if (param == null || StringUtils.isAnyBlank(param.getName(), token)) {
            throw new InParamException("参数异常");
        }
        param.setUpdateUserId(RsaUtil.getUserIdByToken(token));
        if(deptMapper.merge(param) < 1 ){
            throw new RuntimeException();
        }
        CommonResultVo result = new CommonResultVo();
        result.setCode(CommonResultVo.SUCCESS_CODE);
        result.setMsg("修改成功");
        return result;
    }

    @Override
    public CommonResultVo delete(Integer id, String token) {
        if (id == null || StringUtils.isBlank(token)) {
            throw new InParamException("参数异常");
        }
        DepartmentPo param = new DepartmentPo();
        param.setId(id);
        param.setParentId(id);
        param.setState(OaConstants.NORMAL_STATE);
        List<DepartmentPo> deptList = deptMapper.queryByParam(param);
        String msg = "";
        if(!CollectionUtils.isEmpty(deptList)){
            msg = "无法删除，该部门下存在子部门";
        }
        PositionPo positionPo = new PositionPo();
        positionPo.setDeptId(id);
        positionPo.setState(OaConstants.NORMAL_STATE);
        if (!CollectionUtils.isEmpty(positionMapper.findByParam(positionPo))) {
            if (StringUtils.isBlank(msg)){
                msg = "无法删除，该部门下存在职位";
            }else{
                msg += "及职位";
            }
        }
        CommonResultVo result = new CommonResultVo();
        if (StringUtils.isNotBlank(msg)) {
            result.setCode(CommonResultVo.WARN_CODE);
            result.setMsg(msg + "！");
            return result;
        }
        param.setState(OaConstants.DELETE_STATE);
        param.setUpdateUserId(RsaUtil.getUserIdByToken(token));
        if (deptMapper.delete(param) > 0) {
            result.setCode(CommonResultVo.SUCCESS_CODE);
            result.setMsg("删除成功");
        }
        return result;
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

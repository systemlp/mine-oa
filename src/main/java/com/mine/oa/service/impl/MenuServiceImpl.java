package com.mine.oa.service.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mine.oa.constant.OaConstants;
import com.mine.oa.entity.MenuPO;
import com.mine.oa.exception.InParamException;
import com.mine.oa.mapper.MenuMapper;
import com.mine.oa.service.MenuService;
import com.mine.oa.util.RsaUtil;
import com.mine.oa.vo.CommonResultVo;
import com.mine.oa.vo.TreeNodeVO;

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
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo insert(MenuPO menu, String token) {
        CommonResultVo result = checkMenu(menu, token);
        if (result != null) {
            return result;
        }
        menu.setCreateTime(new Date());
        menu.setCreateUserId(RsaUtil.getUserByToken(token).getId());
        menu.setState(OaConstants.NORMAL_STATE);
        if (menuMapper.insert(menu) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("新增成功");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommonResultVo delete(Integer id, String token) {
        if (id == null || StringUtils.isBlank(token)) {
            throw new InParamException();
        }
        MenuPO menu = new MenuPO();
        menu.setParentId(id);
        menu.setState(OaConstants.NORMAL_STATE);
        if (menuMapper.selectOne(menu) != null) {
            return new CommonResultVo().warn("该菜单下包含子菜单，无法删除");
        }
        menu = new MenuPO();
        menu.setId(id);
        menu.setState(OaConstants.DELETE_STATE);
        menu.setUpdateTime(new Date());
        menu.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        if (menuMapper.updateByPrimaryKeySelective(menu) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("删除成功");
    }

    @Override
    public CommonResultVo update(MenuPO menu, String token) {
        CommonResultVo result = checkMenu(menu, token);
        if (result != null) {
            return result;
        }
        MenuPO updateMenu = new MenuPO();
        BeanUtils.copyProperties(menu, updateMenu);
        updateMenu.setUpdateTime(new Date());
        updateMenu.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        updateMenu.setCreateTime(null);
        updateMenu.setCreateUserId(null);
        updateMenu.setState(null);
        if (menuMapper.updateByPrimaryKeySelective(menu) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    public CommonResultVo findTree() {
        List<MenuPO> menuList = findAllMenu();
        return new CommonResultVo<List<TreeNodeVO>>().success(buildTree(menuList));
    }

    private List<TreeNodeVO> buildTree(List<MenuPO> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            return Lists.newArrayList();
        }
        List<TreeNodeVO> treeNodes = convertToTreeNode(menuList);
        List<TreeNodeVO> rootList = Lists.newArrayList();
        for (TreeNodeVO treeNode : treeNodes) {
            if (treeNode.getParentId() == null) {
                rootList.add(treeNode);
            }
            for (TreeNodeVO node : treeNodes) {
                if (Objects.equals(node.getParentId(), treeNode.getId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(Lists.newArrayList());
                    }
                    treeNode.getChildren().add(node);
                }
            }
        }
        return rootList;
    }

    private List<TreeNodeVO> convertToTreeNode(List<MenuPO> menuList) {
        List<TreeNodeVO> tree = Lists.newArrayList();
        TreeNodeVO node;
        for (MenuPO menu : menuList) {
            node = new TreeNodeVO();
            node.setId(menu.getId());
            node.setParentId(menu.getParentId());
            node.setTitle(menu.getTitle());
            node.setUrl(menu.getUrl());
            node.setIcon(menu.getIcon());
            tree.add(node);
        }
        return tree;
    }

    @Override
    public CommonResultVo findAll() {
        return new CommonResultVo<List<MenuPO>>().success(findAllMenu());
    }

    @Override
    public CommonResultVo findAllForUpdateParent(Integer id) {
        List<MenuPO> menuList = findAllMenu();
        Map<Integer, MenuPO> deptMap = Maps.newHashMap();
        for (MenuPO menu : menuList) {
            if (id.compareTo(menu.getId()) != 0) {
                deptMap.put(menu.getId(), menu);
            }
        }
        Set<Integer> notIdSet = findNotOptionalParnetId(id, menuList);
        for (Integer notId : notIdSet) {
            deptMap.remove(notId);
        }
        return new CommonResultVo<List<MenuPO>>().success(Lists.newArrayList(deptMap.values()));
    }

    /***
     * 参数校验
     * 
     * @param menu 菜单
     * @param token 密钥
     * @return 校验结果
     */
    private CommonResultVo checkMenu(MenuPO menu, String token) {
        if (menu == null || StringUtils.isAnyBlank(menu.getTitle(), menu.getUrl(), token) || menu.getSort() == null) {
            throw new InParamException();
        }
        MenuPO menuQuery = new MenuPO();
        menuQuery.setId(menu.getId());
        menuQuery.setTitle(menu.getTitle());
        menuQuery.setUrl(menu.getUrl());
        menuQuery.setState(OaConstants.NORMAL_STATE);
        MenuPO oldMenu = menuMapper.checkExists(menuQuery);
        if (oldMenu != null) {
            String warnMsg = null;
            if (StringUtils.equals(oldMenu.getTitle(), menu.getTitle())) {
                warnMsg = "已存在相同名称菜单";
            }
            if (StringUtils.equals(oldMenu.getUrl(), menu.getUrl())) {
                warnMsg = "已存在相同路径菜单";
            }
            return new CommonResultVo().warn(warnMsg);
        }
        if (menu.getParentId() != null) {
            MenuPO parent = menuMapper.selectByPrimaryKey(menu.getParentId());
            if (parent == null) {
                throw new InParamException(String.format("父级菜单不存在%s", menu.getParentId()));
            }
            if (parent.getState() == OaConstants.DELETE_STATE) {
                return new CommonResultVo().warn("啊哦，在您操作期间父级菜单被删除了，请重新选择吧-_-");
            }
        }
        if (menu.getId() != null) {
            MenuPO self = menuMapper.selectByPrimaryKey(menu.getId());
            if (self == null) {
                throw new InParamException();
            }
            if (OaConstants.DELETE_STATE == self.getState()) {
                return new CommonResultVo().warn("啊哦，在您操作期间该菜单被删除了，请重新选择吧-_-");
            }
        }
        return null;
    }

    private List<MenuPO> findAllMenu() {
        return menuMapper.findAll(OaConstants.NORMAL_STATE);
    }

    /**
     * 获取当前菜单id不可选父级菜单id
     *
     * @param id 当前菜单id
     * @param menuList 菜单
     * @return 不可选父级菜单id
     */
    private Set<Integer> findNotOptionalParnetId(Integer id, List<MenuPO> menuList) {
        Set<Integer> idSet = Sets.newHashSet();
        for (MenuPO menu : menuList) {
            if (!Objects.equals(id, menu.getId())
                    && (menu.getParentId() == null || !Objects.equals(id, menu.getParentId()))) {
                continue;
            }
            idSet.add(menu.getId());
            idSet.addAll(findNotOptionalParnetId(menu.getId(), menuList));
        }
        return idSet;
    }
}

package com.mine.oa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
        menu.setState(OaConstants.DELETE_STATE);
        menu.setUpdateTime(new Date());
        menu.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        if (menuMapper.updateByPrimaryKey(menu) < 1) {
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
        menu.setUpdateTime(new Date());
        menu.setUpdateUserId(RsaUtil.getUserByToken(token).getId());
        if (menuMapper.updateByPrimaryKey(menu) < 1) {
            throw new InParamException();
        }
        return new CommonResultVo().successMsg("修改成功");
    }

    @Override
    public CommonResultVo findTree() {
        List<MenuPO> menuList = findAllMenu();
        return new CommonResultVo<List<TreeNodeVO>>().success(convertToTree(menuList));
    }

    private static List<TreeNodeVO> convertToTree(List<MenuPO> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            return Lists.newArrayList();
        }
        List<TreeNodeVO> tree = Lists.newArrayList();
        for (MenuPO menu : menuList) {
            if (menu.getParentId() == null) {
                initTree(menu, menuList, tree);
            }
        }
        return tree;
    }

    private static void initTree(MenuPO menu, List<MenuPO> menuList, List<TreeNodeVO> nodeList) {
        TreeNodeVO root = new TreeNodeVO();
        root.setTitle(menu.getTitle());
        root.setUrl(menu.getUrl());
        root.setIcon(menu.getIcon());
        nodeList.add(root);
        setChildren(root, menu.getId(), menuList);
    }

    private static void setChildren(TreeNodeVO root, Integer rootId, List<MenuPO> menuList) {
        List<TreeNodeVO> children = Lists.newArrayList();
        for (MenuPO node : menuList) {
            if (rootId.equals(node.getParentId())) {
                initTree(node, menuList, children);
            }
        }
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        root.setChildren(children);
    }

    // public static void main(String[] args) {
    // List<MenuPO> menuList = Lists.newArrayList();
    // MenuPO menu1 = new MenuPO();
    // menu1.setId(1);
    // MenuPO menu2 = new MenuPO();
    // menu2.setId(2);
    // menu2.setParentId(1);
    // MenuPO menu3 = new MenuPO();
    // menu3.setId(3);
    // menu3.setParentId(1);
    // MenuPO menu4 = new MenuPO();
    // menu4.setId(4);
    // MenuPO menu5 = new MenuPO();
    // menu5.setId(5);
    // menu5.setParentId(4);
    // MenuPO menu6 = new MenuPO();
    // menu6.setId(6);
    // menu6.setParentId(4);
    // menuList.add(menu1);
    // menuList.add(menu2);
    // menuList.add(menu3);
    // menuList.add(menu4);
    // menuList.add(menu5);
    // menuList.add(menu6);
    // System.out.println(JSON.toJSONString(convertToTree(menuList)));
    // }

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
        List<Integer> notIdList = findNotOptionalParnetId(id, menuList);
        for (Integer notId : notIdList) {
            deptMap.remove(notId);
        }
        return new CommonResultVo<List<MenuPO>>().success(Lists.newArrayList(deptMap.values()));
    }

    private CommonResultVo checkMenu(MenuPO menu, String token) {
        if (menu == null || StringUtils.isAnyBlank(menu.getTitle(), menu.getUrl(), token) || menu.getSort() == null) {
            throw new InParamException();
        }
        MenuPO menuQuery = new MenuPO();
        menuQuery.setTitle(menu.getTitle());
        menuQuery.setUrl(menu.getUrl());
        menuQuery.setState(OaConstants.DELETE_STATE);
        MenuPO oldMenu;
        if (menu.getId() == null) {
            oldMenu = menuMapper.selectOne(menuQuery);
        } else {
            oldMenu = menuMapper.updateCheckExists(menuQuery);
        }
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
    private List<Integer> findNotOptionalParnetId(Integer id, List<MenuPO> menuList) {
        List<Integer> idList = Lists.newArrayList();
        for (MenuPO menu : menuList) {
            if (menu.getParentId() == null || id.compareTo(menu.getParentId()) != 0) {
                continue;
            }
            idList.add(menu.getId());
            idList.addAll(findNotOptionalParnetId(menu.getId(), menuList));
        }
        return idList;
    }
}

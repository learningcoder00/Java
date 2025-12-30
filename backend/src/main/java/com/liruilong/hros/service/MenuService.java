package com.liruilong.hros.service;

import com.liruilong.hros.mapper.MenuMapper;
import com.liruilong.hros.mapper.MenuRoleMapper;
import com.liruilong.hros.model.Menu;
import com.liruilong.hros.model.OpLog;
import com.liruilong.hros.service.utils.Hruitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

// 菜单处理
@Transactional
@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

    @Autowired
    MenuRoleMapper menuRoleMapper;

    @Autowired
    OplogService oplogService;

    // 根据角色id获取权限菜单
    public List<Menu> getMenusById() {
        return menuMapper.getMenusByHrId(Hruitls.getCurrent().getId());
    }

    // 获取所有权限菜单
    // @Cacheable
    public List<Menu> getAllMenusWithRole() {
        return menuMapper.getAllMenusWithRole();
    }

    // 菜单数据初始化，系統設置裏的系統菜單處理。

    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }

    // 获取当前角色权限
    public List<Integer> getMidsByRid(Integer rid) {

        return menuRoleMapper.getMidsByRid(rid);

    }

    // 更新信息

    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {

        oplogService.addOpLog(new OpLog((byte) 1, new Date(), "角色权限变更", Hruitls.getCurrent().getName()));

        menuRoleMapper.deleteByRid(rid);
        if (mids != null && mids.length > 0) {
            Integer result = menuRoleMapper.insertRecord(rid, mids);
            return result == mids.length;
        } else {
            return true;
        }
    }
}

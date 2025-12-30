package com.liruilong.hros.controller.systems;

import com.liruilong.hros.model.Menu;
import com.liruilong.hros.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 菜单加载
@RestController
@RequestMapping("/system/config")
public class SystemConfigController {
    @Autowired
    MenuService menuService;

    // 系統菜單加載

    @GetMapping("/menu")
    public List<Menu> getMenusByHrId(){
        return menuService.getMenusById();
    }
}

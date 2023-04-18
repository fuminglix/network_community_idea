package com.haue.controller;

import com.haue.pojo.entity.Menu;
import com.haue.service.MenuService;
import com.haue.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult getList(String menuName, String status){
        return menuService.getList(menuName,status);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("{id}")
    public ResponseResult showMenuInfo(@PathVariable("id") Long id){
        return menuService.showMenuInfo(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long id){
        return menuService.deleteMenu(id);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        return menuService.treeSelect();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id") Long roleId){
        return menuService.roleMenuTreeSelect(roleId);
    }
}

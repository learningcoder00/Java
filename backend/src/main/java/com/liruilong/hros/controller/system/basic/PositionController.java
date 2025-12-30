package com.liruilong.hros.controller.system.basic;


import com.liruilong.hros.model.Position;
import com.liruilong.hros.model.RespBean;
import com.liruilong.hros.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 职位

@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {
    @Autowired
    PositionService positionService;

    // 返回显示信息

    @GetMapping("/")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/all")
    public List<Position> getRealAllPositions() {
        return positionService.getRealAllPositions();
    }

    // 添加

    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        if (positionService.addPosition(position) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    // 更新
    @PutMapping("/")
    public RespBean updatePositions(@RequestBody Position position) {
        if (positionService.updatePositions(position) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @PostMapping("/serch/")
    public List<Position> serchPositions(@RequestBody Position position){
        return  positionService.serchPositions(position);

    }
    // 删除

    @DeleteMapping("/{id}")
    public RespBean deletePositionById(@PathVariable Integer id) {
        if (positionService.deletePositionById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    // 批量删除

    @DeleteMapping("/")
    public RespBean deletePositionsByIds(Integer[] ids) {
        if (positionService.deletePositionsByIds(ids) == ids.length) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}

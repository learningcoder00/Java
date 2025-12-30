package com.liruilong.hros.service;

import com.liruilong.hros.mapper.EmployeeremoveMapper;
import com.liruilong.hros.model.Employeeremove;
import com.liruilong.hros.service.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class EmployeeremoveService {

    @Autowired
    EmployeeremoveMapper employeeremoveMapper;
    @Autowired
    OplogService oplogService;
    public Integer addEmploymove(Employeeremove employeeremove) {
       oplogService.addOpLog(LogUtils.addLog((byte) 6,"员工调动:eid="+employeeremove.getEid()));
       return employeeremoveMapper.insertSelective(employeeremove);
    }
}

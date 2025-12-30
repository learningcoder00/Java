package com.liruilong.hros.mapper;

import com.liruilong.hros.model.Hr;
import com.liruilong.hros.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface HrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hr record);

    int insertSelective(Hr record);

    Hr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hr record);

    int updateByPrimaryKey(Hr record);

    int updatePasswordByPrimaryKey(Hr record);

    int updateWorkDate(Hr record);

    // 登录

    Hr loadUserByUsername(String username);

    Hr loadUserByEmployeeId(Integer employeeId);

    // 获取当前用户的所用角色

    List<Role> getHrRolesById(Integer id);

    List<Hr> getAllHRs(@Param("id") Integer id, @Param("name") String name);


    List<Hr>  getAllHrsExceptCurrentHr(Integer id);
}
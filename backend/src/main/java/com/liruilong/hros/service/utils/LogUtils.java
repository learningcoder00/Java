package com.liruilong.hros.service.utils;

import com.liruilong.hros.model.OpLog;

import java.util.Date;


public class LogUtils {

    public static OpLog addLog(Byte type, String msg){
        return new OpLog(type,new Date(),msg,Hruitls.getCurrent().getName() );
    }
}

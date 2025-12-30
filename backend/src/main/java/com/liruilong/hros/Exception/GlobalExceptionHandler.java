
package com.liruilong.hros.Exception;

import com.liruilong.hros.model.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Logger;

// 全局异常处理

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = Logger.getLogger("GlobalExceptionHandler");

    // 业务运行时异常：直接把 message 返回给前端，避免前端只看到“未知错误”
    @ExceptionHandler(RuntimeException.class)
    public RespBean runtimeException(RuntimeException e) {
        logger.warning("RuntimeException: " + e.getMessage());
        return RespBean.error(e.getMessage() == null ? "系统异常,操作失败" : e.getMessage());
    }

    // 兜底异常：避免 500 无提示
    @ExceptionHandler(Exception.class)
    public RespBean exception(Exception e) {
        logger.warning("Exception: " + e.getMessage());
        return RespBean.error("系统异常,操作失败");
    }
    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException e) {
        // 处理唯一约束、外键等完整性异常
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据有关联数据,操作异常!");
        }
        return RespBean.error("数据库异常,操作失败");
    }
/*    @ExceptionHandler(IllegalStateException.class)
    public void ValidateCodeException(IllegalStateException e) {

        logger.warning("Cannot call sendError() after the response has been committed " +"异常");

    }
    @ExceptionHandler(NullPointerException.class)
    public RespBean ValidateCodeException(NullPointerException e) {
        return RespBean.error("系统错误提示:空指针异常");
    }*/
}

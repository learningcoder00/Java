package com.liruilong.hros.controller;

import com.liruilong.hros.model.RespBean;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@RestController
public class LoginController {

    private static Logger Loggerlogger = Logger.getLogger("LoginController");

    @GetMapping("/login")
    public RespBean login(){
        return RespBean.error("尚未登录,请登录!");
    }
    @GetMapping(value = "/auth/code")
    public Map<String, Object> getCode(HttpServletRequest request){
        // 说明：
        String result;
        String imgBase64;

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        if (engine != null) {
            // 算术类型 https://gitee.com/whvse/EasyCaptcha
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
            // 几位数运算，默认是两位
            captcha.setLen(2);
            // 获取运算的结果
            result = captcha.text();
            result = "0.0".equals(result) ? "0" : result;
            imgBase64 = captcha.toBase64();
        } else {
            // 降级为普通字符验证码（不依赖 ScriptEngine）
            SpecCaptcha captcha = new SpecCaptcha(111, 36, 4);
            result = captcha.text();
            imgBase64 = captcha.toBase64();
        }

        Loggerlogger.warning("生成的验证码：" + result);
        request.getSession().setAttribute("yanzhengma", result);

        Map<String,Object> imgResult = new HashMap<>(2);
        imgResult.put("img", imgBase64);
        return imgResult;
    }
}

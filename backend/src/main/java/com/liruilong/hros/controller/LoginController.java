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

/**
 * @Description :

 * @Date: 2019/12/19 19:58
 */
@RestController
public class LoginController {

    private static Logger Loggerlogger = Logger.getLogger("LoginController");

    @GetMapping("/login")
    public RespBean login(){
        return RespBean.error("尚未登录,请登录!");
    }
    @GetMapping(value = "/auth/code")
    public Map<String, Object> getCode(HttpServletRequest request){
        /**
         * 说明：
         * easy-captcha 的 ArithmeticCaptcha 在较新的 JDK(>=15) 上可能拿不到 JavaScript ScriptEngine（Nashorn 已移除），
         * 进而在 captcha.text() 处抛 NPE。为了兼容你的本机 JDK，这里做降级：
         * - 能拿到 ScriptEngine：使用算术验证码
         * - 拿不到 ScriptEngine：使用普通字符验证码（SpecCaptcha）
         */
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

package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(HttpServletRequest request){
        // 因为每次打开页面都将跳转到这里
        // 所以在这里验证登录时是否有Cookie，如果有则跳过登录界面
        Cookie[] cookies = request.getCookies();
        // 拿到Cookie之后需再次进行验证，因为可能会在登录中修改密码等情况
        String loginAct = null;
        String loginPwd = null;
        // 遍历Cookie得到键和值
        for(Cookie cookie:cookies){
            String name = cookie.getName();
            if("loginAct".equals(name)){
                loginAct = cookie.getValue();
                continue;
            }
            if("loginPwd".equals(name)){
                loginPwd = cookie.getValue();
            }
        }
        if(loginAct != null && loginPwd != null){
            // 在这里进行验证
            Map<String,Object> map = new HashMap<>();
            map.put("loginAct", loginAct);
            map.put("loginPwd",MD5Util.getMD5(loginPwd));
            User user = userService.queryByLoginActAndLoginPwd(map);
            request.getSession().setAttribute(Contants.SESSION_USER,user);
            return "redirect:/workbench/index.do";
        }else{
            return "settings/qx/user/login";
        }

    }

    // 处理这个请求之前定义返回结果类ReturnObject和常量类Contants
    @RequestMapping("/settings/qx/user/login.do")
    // returnObject对象可能是实体对象，可能是list集合
    // 前端使用json来对returnObject对象进行解析，所以在返回之前需要添加注解@responseBody
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request,
                                      HttpServletResponse response, HttpSession session){
        // 在控制层需要对用户输入的信息进行后端验证，并且将信息传递到业务逻辑层
        // 封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        // 在加入密码时记得加密
        map.put("loginPwd", MD5Util.getMD5(loginPwd));
        User user = userService.queryByLoginActAndLoginPwd(map);
        String ip = request.getRemoteAddr();
        System.out.println(ip);
        // 后端验证从数据库查到的数据
        ReturnObject returnObject = new ReturnObject();
        // 后端进行数据有效性的验证
        if(user == null){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("用户名或密码错误");
        }else{
            // 如果当前时间比失效时间大
            if(DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime())>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockState())){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
                returnObject.setMessage("账号被封禁");
            }else if(!user.getAllowIps().contains(ip)){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
                returnObject.setMessage("ip地址被封禁");
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                // 登录成功后将user保存在session中
                session.setAttribute(Contants.SESSION_USER,user);

                // 判断用户是否需要免登陆功能
                if("true".equals(isRemPwd)){ // 需要
                    Cookie cookie1 = new Cookie("loginAct",loginAct);
                    Cookie cookie2 = new Cookie("loginPwd",loginPwd);
                    cookie1.setMaxAge(10*24*60*60);
                    cookie2.setMaxAge(10*24*60*60);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                }else{
//                    不需要，也可以不写
                    Cookie cookie1 = new Cookie("loginAct",loginAct);
                    Cookie cookie2 = new Cookie("loginPwd",loginPwd);
                    // 当设定0秒时，将不保存Cookie
                    cookie1.setMaxAge(0);
                    cookie2.setMaxAge(0);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                }
            }
        }
        // 返回数据给前端
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
        // 退出应该销毁Cookie和Session，采用以下方法清除Cookie
        // 如果MaxAge为0，表示删除该Cookie，想要修改某个cookie只能使用一个同名的cookie来覆盖原来的cookie，来达到修改的目的
        // 但是要注意：修改、删除cookie时，新建的cookie除了value和maxAge之外的所有属性必须与原cookie完全相同，否则浏览器将
        // 视为两个cookie不予覆盖
        Cookie cookie1 = new Cookie("loginAct",null);
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        Cookie cookie2 = new Cookie("loginPwd",null);
        cookie2.setMaxAge(0);
        response.addCookie(cookie2);
        // 手动销毁Session
        session.invalidate();
        return "redirect:/";
    }
}

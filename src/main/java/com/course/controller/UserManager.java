package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Log4j
@RestController
@Api(value = "v1", description = "用户管理系统")
@RequestMapping("v1")
public class UserManager {

    @Autowired
    private SqlSessionTemplate template;

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody User user){
        int i = template.selectOne("login", user);
        log.info("查询到的结果是" + i );
        if(i == 1){
            log.info("登录的用户是: " + user.getUserName());
            Cookie  cookie = new Cookie("login", "true");
            response.addCookie(cookie);
            return true;
        }
        System.out.println("错误的用户名或密码，登录失败");
        log.info("你的名字，我的姓氏");
        return false;
    }

    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public boolean addUser(HttpServletRequest request, @RequestBody User user){
        Boolean x = veryfiCookies(request);
        int result = 0;
        if (x){
            result = template.insert("addUser", user);
        }
        if(result > 0){
            log.info("添加用户的数量是：" + result);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "获取用户（列表）信息接口", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user ){
        Boolean x = veryfiCookies(request);
        if (x){
            List<User> users = template.selectList("getUserInfo", user);
            log.info("getUserInfo获取到的用户数量是: " + users.size());
            return users;
        } else {
            return null;
        }
    }

    @ApiOperation(value = "更新用户接口", httpMethod = "POST")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public int updateUserInfo(HttpServletRequest request, @RequestBody User user){
        Boolean x = veryfiCookies(request);
        log.info("获取的cookie值为"+request.getCookies());
        int i = 0;
        if (x){
            log.info("更新的用户是"+user);
            i = template.update("updateUserInfo", user);
        }
        log.info("更新用户的条目数为：" + i);
        return i;

    }

    private Boolean veryfiCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("veryfiCookies接口校验的Cookie信息是："+ cookies);
        if(Objects.isNull(cookies)){
            log.info("cookies为空");
            return false;
        }

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("login") && cookie.getValue().equals("true")){
                log.info("cookies验证通过");
                return true;
            }
        }
        return false;
    }
}



package com.ye.wm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ye.wm.common.R;
import com.ye.wm.entity.User;
import com.ye.wm.service.UserService;
import com.ye.wm.utils.SMSUtils;
import com.ye.wm.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){



        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
          String s = ValidateCodeUtils.generateValidateCode(4).toString();

          log.info(s);

            session.setAttribute(phone,s);
            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,s);

            R.success("发送成功");
        }

return R.error("发送失败");

    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){

        String phone = map.get("phone").toString();

        String code = map.get("code").toString();

        Object scode = session.getAttribute(phone);

        if (scode != null && scode.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();

            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);

            if (user ==null){
                user = new User();
                user.setPhone(phone);

                userService.save(user);
            }

            session.setAttribute("user",user.getId());

            return R.success(user);

        }

        return R.error("登录失败");

    }

}

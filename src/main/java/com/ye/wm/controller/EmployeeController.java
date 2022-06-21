package com.ye.wm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ye.wm.common.R;
import com.ye.wm.entity.Category;
import com.ye.wm.entity.Employee;
import com.ye.wm.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 员工登录
 */


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //md5处理密码
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查询数据库是否有这个用户名
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //判断用户名是否有这个用户名
        if (emp==null){
            return R.error("登录失败,用户名或者密码错误");
        }
        //对比数据库密码
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败,用户名或者密码错误");
        }

        //对比数据库状态
        if (emp.getStatus()==0){
            return R.error("登录失败,账号已经用");
        }
        //登录成功封装数据
        request.getSession().setAttribute("employee",emp.getId());
        //返回成功对象
        return R.success(emp);
    }

    /**
     * 员工账号退出
     * @param request
     * @return
     */

    @PostMapping("/logout")
    public R<String > logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){

        log.info("添加员工：{}",employee.toString());

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

/*
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
*/

        employeeService.save(employee);

        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //分页构造器
        Page pageInfo = new Page(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        //添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询方法
        employeeService.page(pageInfo,wrapper);

        //返回成功信息
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param request
     * @param employee
     * @return
     */

    @PutMapping
    public R<String> upDate(HttpServletRequest request,@RequestBody Employee employee){

        log.info("更新信息，update");

/*        Long user = (Long) request.getSession().getAttribute("employee");

        employee.setUpdateUser(user);

        employee.setUpdateTime(LocalDateTime.now());*/

        employeeService.updateById(employee);

        return R.success("更新成功");
    }


    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){

        log.info("根据id查询 getById");

        Employee emp = employeeService.getById(id);

        if (emp!=null) {
            return R.success(emp);
        }
        return R.error("查询失败");
    }



}

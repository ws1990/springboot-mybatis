package com.ws.study.springboot.mybatis.edu.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ws.study.springboot.mybatis.edu.entity.Student;
import com.ws.study.springboot.mybatis.edu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author 王松
 * @since 2019-01-23
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("list")
    public Object getStudent(@RequestParam String name) {
        Wrapper<Student> wrapper = Wrappers.<Student>lambdaQuery()
                .like(Student::getUsername, name);
        List<Student> list = studentService.list(wrapper);
        System.out.println(list);

        return list;
    }

}

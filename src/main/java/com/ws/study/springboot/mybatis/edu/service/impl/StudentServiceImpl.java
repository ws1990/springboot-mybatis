package com.ws.study.springboot.mybatis.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ws.study.springboot.mybatis.edu.entity.Student;
import com.ws.study.springboot.mybatis.edu.mapper.StudentMapper;
import com.ws.study.springboot.mybatis.edu.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author 王松
 * @since 2019-01-23
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> findByName(String name) {
        Wrapper<Student> wrapper = Wrappers.<Student>lambdaQuery().like(Student::getUsername, name);
        return studentMapper.selectList(wrapper);
    }

    @Override
    public List<Student> findByNameUseMapper(String name) {
        return studentMapper.selectByUsername("%" + name + "%");
    }
}

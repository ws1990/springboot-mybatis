package com.ws.study.springboot.mybatis.edu.service.impl;

import com.ws.study.springboot.mybatis.edu.entity.Student;
import com.ws.study.springboot.mybatis.edu.mapper.StudentMapper;
import com.ws.study.springboot.mybatis.edu.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}

package com.ws.study.springboot.mybatis.edu.mapper;

import com.ws.study.springboot.mybatis.edu.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 学生表 Mapper 接口
 * </p>
 *
 * @author 王松
 * @since 2019-01-23
 */
public interface StudentMapper extends BaseMapper<Student> {

    List<Student> selectByUsername(@Param("username") String username);

}

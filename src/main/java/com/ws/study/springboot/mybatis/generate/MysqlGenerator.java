package com.ws.study.springboot.mybatis.generate;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author 王松
 * @version 1.0
 * @date 19-1-21 下午2:05
 */
public class MysqlGenerator {

    private static final String database = "springboot-mybatis";
    private static final String url = "jdbc:mysql://localhost:3306/" + database + "?useUnicode=true&useSSL=false&characterEncoding=utf8";
    private static final String driverName = "com.mysql.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "123456";

    private static String basePath = "";
    private static String mapperPath = "";


    public static void main(String[] args) {
        generate("王松","com.ws.study.springboot.mybatis.edu", "edu");
    }


    /**
     * 自动生成代码
     * @param author 作者
     * @param packageName 包名
     * @param tablePrefix 表前缀
     */
    public static void generate(String author, String packageName, String... tablePrefix) {

        // 全局配置
        GlobalConfig gc = initGlobalConfig(author, packageName);
        // 数据源配置
        DataSourceConfig dsc = initDataSourceConfig();
        // 包配置
        PackageConfig pc = new PackageConfig().setParent(packageName);
        // 模板引擎配置
        FreemarkerTemplateEngine templateEngine = new FreemarkerTemplateEngine();


        //每一个entity都需要单独设置InjectionConfig, StrategyConfig和TemplateConfig
        Map<String, String> names = new JdbcRepository().getEntityNames(tablePrefix);
        if (names == null || names.isEmpty()) {
            return;
        }
        for (String tableName : names.keySet()) {
            // 代码生成器
            AutoGenerator mpg = new AutoGenerator();
            mpg.setGlobalConfig(gc);
            mpg.setDataSource(dsc);
            mpg.setPackageInfo(pc);
            mpg.setTemplateEngine(templateEngine);

            // 自定义配置
            InjectionConfig cfg = initInjectionConfig(packageName);
            mpg.setCfg(cfg);

            // 策略配置
            StrategyConfig strategy = initStrategyConfig(tableName, tablePrefix);
            mpg.setStrategy(strategy);

            // 模板配置
            // mapper文件
            String mapperFile = mapperPath
                    + "/" + names.get(tableName) + "Mapper" + StringPool.DOT_JAVA;
            TemplateConfig tc = initTemplateConfig(mapperFile);
            mpg.setTemplate(tc);

            //开始执行
            mpg.execute();
        }
    }


    /**
     * 配置数据源
     * @return
     */
    private static DataSourceConfig initDataSourceConfig() {
        return new DataSourceConfig()
                .setUrl(url)
                .setDriverName(driverName)
                .setUsername(userName)
                .setPassword(password);
    }

    /**
     * 全局配置
     * @return
     */
    private static GlobalConfig initGlobalConfig(String author, String packageName) {
        GlobalConfig gc = new GlobalConfig();
        String tmp = MysqlGenerator.class.getResource("").getPath();
        String codeDir = tmp.substring(0, tmp.indexOf("/target"));
        basePath = codeDir + "/src/main/java";
        mapperPath = basePath + "/" + packageName.replace(".", "/") + "/mapper";
        System.out.println("basePath = " + basePath + "\nmapperPath = " + mapperPath);
        gc.setOutputDir(basePath);
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setServiceName("%sService");
        gc.setFileOverride(true);

        return gc;
    }

    /**
     * 自定义配置
     * @param packageName
     * @return
     */
    private static InjectionConfig initInjectionConfig(String packageName) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                //自定义输入文件名称
                return mapperPath
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);

        return cfg;
    }

    /**
     * 策略配置
     * @param tableName 数据库表名
     * @return
     */
    private static StrategyConfig initStrategyConfig(String tableName, String... tablePrefix) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setTablePrefix(tablePrefix);
        strategy.setInclude(tableName);
        strategy.setRestControllerStyle(true);

        return strategy;
    }

    /**
     * 覆盖Entity以及xml
     * @param mapperFile
     * @return
     */
    private static TemplateConfig initTemplateConfig(String mapperFile) {
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        //如果当前Entity已经存在,那么仅仅覆盖Entity
        File file = new File(mapperFile);
        if (file.exists()) {
            tc.setController(null);
            tc.setMapper(null);
            tc.setService(null);
            tc.setServiceImpl(null);
            tc.setEntityKt(null);
        }

        return tc;
    }



    public static class JdbcRepository {
        private static Pattern linePattern = Pattern.compile("_(\\w)");
        private JdbcOperations jdbcOperations;
        public JdbcRepository() {
            DataSource dataSource = DataSourceBuilder.create()
                    //如果不指定类型，那么默认使用连接池，会存在连接不能回收而最终被耗尽的问题
                    .type(DriverManagerDataSource.class)
                    .driverClassName(driverName)
                    .url(url)
                    .username(userName)
                    .password(password)
                    .build();
            this.jdbcOperations = new JdbcTemplate(dataSource);
        }

        /**
         * 获取所有实体类的名字,实体类由数据库表名转换而来.
         * 例如: 表前缀为auth,完整表名为auth_first_second,那么entity则为FirstSecond
         * @param tablePrefixArray 数据库表名前缀,可能为空
         * @return
         */
        public Map<String, String> getEntityNames(String... tablePrefixArray) {
            //该sql语句目前支持mysql
            String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + database + "'";
            if (tablePrefixArray != null && tablePrefixArray.length != 0) {
                sql += " and (";
                for (String prefix : tablePrefixArray) {
                    sql += " or table_name like '" + prefix + "_%'";
                }
                sql += ")";
            }
            sql = sql.replaceFirst("or", "");
            List<String> tableNames = jdbcOperations.query(sql, SingleColumnRowMapper.newInstance(String.class));
            if (CollectionUtils.isEmpty(tableNames)) {
                return new HashMap<>();
            }


            Map<String, String> result = new HashMap<>();
            tableNames.forEach(
                    tableName -> {
                        String entityName = underlineToCamel(tableName);
                        if (tablePrefixArray != null && tablePrefixArray.length != 0) {
                            for (String prefix : tablePrefixArray) {
                                //如果有前缀,需要去掉前缀
                                if (tableName.startsWith(prefix)) {
                                    String tableNameRemovePrefix = tableName.substring((prefix + "_").length());
                                    entityName = underlineToCamel(tableNameRemovePrefix);
                                }
                            }
                        }

                        result.put(tableName, entityName);
                    }
            );


            return result;
        }


        /**
         * 下划线转驼峰
         *
         * @param str
         * @return
         */
        private static String underlineToCamel(String str) {
            if (null == str || "".equals(str)) {
                return str;
            }
            str = str.toLowerCase();
            Matcher matcher = linePattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            }
            matcher.appendTail(sb);

            str = sb.toString();
            str = str.substring(0, 1).toUpperCase() + str.substring(1);

            return str;
        }

    }
}

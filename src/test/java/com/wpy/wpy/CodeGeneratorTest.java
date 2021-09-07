package com.wpy.wpy;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGeneratorTest {


    public static void main(String[] args) {
        // 代码生成器
        String packageName="com.wpy";
        String outPath="D://AutoCode";
        String[] strings={"sys_user","sys_user_role","sys_role","sys_log","sys_dict","read_history","novel_share","novel_chapter","novel","file","conmment","collection","catalog"};
        create(packageName,outPath,strings);
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "/src/main/java");
//        gc.setServiceName("%sService");
//        gc.setAuthor("wpy");
//        gc.setOpen(false);
//        // gc.setSwagger2(true); 实体属性 Swagger2 注解
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://localhost:3306/onlineread?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
//        // dsc.setSchemaName("public");
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        dsc.setUsername("root");
//        dsc.setPassword("root");
//        mpg.setDataSource(dsc);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("com.wpy");
//        pc.setController("controller");
//        pc.setService("service");
//        pc.setServiceImpl("service.impl");
//        pc.setMapper("mapper");
//        pc.setEntity("entity");
//        pc.setXml("mapper.mapper");
//        mpg.setPackageInfo(pc);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
////        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
//        strategy.setEntityLombokModel(true);
//        strategy.setRestControllerStyle(true);
//        strategy.setEntitySerialVersionUID(true);
//        strategy.setEntityTableFieldAnnotationEnable(true);
//        strategy.setInclude("user");
//        strategy.setControllerMappingHyphenStyle(true);
//        mpg.setStrategy(strategy);
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
//        mpg.execute();
    }

    private static void create(String packageName, String outPath, String... tableName) {
        GlobalConfig config=new GlobalConfig();
        String dbUrl="jdbc:mysql://localhost:3306/onlineread?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        DataSourceConfig dataSourceConfig=new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("root")
                .setDriverName("com.mysql.cj.jdbc.Driver");
        StrategyConfig strategyConfig=new StrategyConfig();
        strategyConfig
                .setRestControllerStyle(true)
                .setEntitySerialVersionUID(true)
                .setCapitalMode(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityTableFieldAnnotationEnable(true)
                .setEntityLombokModel(true)
                .setInclude(tableName);
        config.setActiveRecord(true)
                .setFileOverride(true)
                .setEnableCache(false)
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setAuthor("pywang")
                .setOutputDir(outPath)
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController");

        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setPackageInfo(
                        new PackageConfig()
                        .setParent(packageName)
                        .setController("controller")
                        .setEntity("entity")
                        .setService("service")
                        .setMapper("mapper")
                ).execute();

    }

}
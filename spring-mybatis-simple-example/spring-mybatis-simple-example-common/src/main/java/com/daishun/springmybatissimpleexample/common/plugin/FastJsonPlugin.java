package com.daishun.springmybatissimpleexample.common.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author daishun
 * @since 2019/8/4
 */
public class FastJsonPlugin extends PluginAdapter {

    private FullyQualifiedJavaType jsonFieldAnnotation;


    public FastJsonPlugin() {
        this.jsonFieldAnnotation = new FullyQualifiedJavaType("com.alibaba.fastjson.annotation.JSONField");
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String jdbcTypeName = introspectedColumn.getJdbcTypeName();
        if ("DATE".equalsIgnoreCase(jdbcTypeName)){
            field.addAnnotation("@JSONField(format = \"yyyy-MM-dd\")");
            topLevelClass.addImportedType(jsonFieldAnnotation);
        }else if ("TIME".equalsIgnoreCase(jdbcTypeName)
                ||"DATETIME".equalsIgnoreCase(jdbcTypeName)
                ||"TIMESTAMP".equalsIgnoreCase(jdbcTypeName)){
            field.addAnnotation("@JSONField(format = \"yyyy-MM-dd HH:mm:ss\")");
            topLevelClass.addImportedType(jsonFieldAnnotation);
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}

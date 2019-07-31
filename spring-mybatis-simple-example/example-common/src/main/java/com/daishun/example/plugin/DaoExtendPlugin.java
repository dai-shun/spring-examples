package com.daishun.example.plugin;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.config.TableConfiguration;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author daishun
 * @since 2019/7/30
 */
public class DaoExtendPlugin extends PluginAdapter {

    private String targetJavaPackage;

    private String targetJavaProject;

    private String targetXmlPackage;

    private String targetXmlProject;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> result = super.contextGenerateAdditionalJavaFiles(introspectedTable);
        if (result == null) {
            result = new ArrayList<>();
        }
        List<GeneratedJavaFile> javaFiles = this.createJavaFiles();
        result.addAll(javaFiles);
        return result;
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> result = super.contextGenerateAdditionalXmlFiles(introspectedTable);
        if (result == null) {
            result = new ArrayList<>();
        }
        List<GeneratedXmlFile> daoXmlFiles = this.createXmlFiles();
        result.addAll(daoXmlFiles);
        return result;
    }

    private List<GeneratedXmlFile> createXmlFiles() {
        List<GeneratedXmlFile> xmlFiles = new ArrayList<>();
        List<TableConfiguration> configurations = this.context.getTableConfigurations();
        for (TableConfiguration configuration : configurations) {
            String domainObjectName = configuration.getDomainObjectName();
            String daoName = domainObjectName + "Dao";
            if (this.shouldCreateXmlFile(daoName)) {
                String mapperName = domainObjectName + "Mapper";
                GeneratedXmlFile xmlFile = this.createXmlFile(mapperName, daoName);
                xmlFiles.add(xmlFile);
            }
        }
        return xmlFiles;
    }

    private GeneratedXmlFile createXmlFile(String mapperName, String daoName) {
        Document document = this.createXmlDocument(mapperName, daoName);
        return new GeneratedXmlFile(document, daoName + ".xml", this.targetXmlPackage, this.targetXmlProject,
                true, this.context.getXmlFormatter());
    }

    private List<GeneratedJavaFile> createJavaFiles() {
        List<GeneratedJavaFile> javaFiles = new ArrayList<>();
        List<TableConfiguration> configurations = this.context.getTableConfigurations();
        for (TableConfiguration configuration : configurations) {
            String domainObjectName = configuration.getDomainObjectName();
            String daoName = domainObjectName + "Dao";
            if (this.shouldCreateJavaFile(daoName)) {
                CompilationUnit unit = this.createInterfaceClass(daoName);
                GeneratedJavaFile javaFile = this.createJavaFile(unit);
                javaFiles.add(javaFile);
            }
        }
        return javaFiles;
    }

    private GeneratedJavaFile createJavaFile(CompilationUnit unit) {
        JavaFormatter javaFormatter = this.context.getJavaFormatter();
        return new GeneratedJavaFile(unit, this.targetJavaProject, Charsets.UTF_8.name(), javaFormatter);
    }

    private CompilationUnit createInterfaceClass(String fileName) {
        String fullyQualifiedClassName = this.targetJavaPackage + "." + fileName;
        Interface mapperInterface = new Interface(fullyQualifiedClassName);
        mapperInterface.setVisibility(JavaVisibility.PUBLIC);
        return mapperInterface;
    }

    private boolean shouldCreateJavaFile(String fileName) {
        String filePath = this.getJavaFilePath(fileName);
        return !isFileExist(filePath);
    }

    private boolean shouldCreateXmlFile(String fileName) {
        String filePath = this.getXmlFilePath(fileName);
        return !isFileExist(filePath);
    }

    private String getXmlFilePath(String fileName) {
        List<String> packages = Arrays.asList(this.targetXmlPackage.split("\\."));
        List<String> fileNames = new ArrayList<>();
        fileNames.add(this.getUserDir());
        fileNames.add(this.targetXmlProject);
        fileNames.addAll(packages);
        fileNames.add(fileName);
        return Joiner.on(File.separator).join(fileNames) + ".xml";
    }

    private String getJavaFilePath(String fileName) {
        List<String> packages = Arrays.asList(this.targetJavaPackage.split("\\."));
        List<String> fileNames = new ArrayList<>();
        fileNames.add(this.getUserDir());
        fileNames.add(this.targetJavaProject);
        fileNames.addAll(packages);
        fileNames.add(fileName);
        return Joiner.on(File.separator).join(fileNames) + ".java";
    }

    private String getUserDir() {
        return System.getProperty("user.dir");
    }


    private Document createXmlDocument(String mapperName, String daoName) {
        Document document = new Document(
                XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        XmlElement rootElement = this.createMapperElement(mapperName, daoName);
        document.setRootElement(rootElement);
        return document;
    }

    private XmlElement createMapperElement(String mapperName, String daoName) {
        XmlElement mapperElement = new XmlElement("mapper");
        Attribute namespace = new Attribute("namespace", this.targetJavaPackage + "." + daoName);
        mapperElement.addAttribute(namespace);
        mapperElement.addElement(this.createBaseResultMap(mapperName));
        return mapperElement;
    }

    private XmlElement createBaseResultMap(String mapperName) {
        String mapperPackage = this.context.getJavaClientGeneratorConfiguration().getTargetPackage();
        String modelPackage = this.context.getJavaModelGeneratorConfiguration().getTargetPackage();
        String extendsAttr = String.format("%s.%s.BaseResultMap", mapperPackage, mapperName);
        String typeAttr = String.format("%s.%s", modelPackage, mapperName.replace("Mapper", ""));
        XmlElement resultMap = new XmlElement("resultMap");
        Attribute resultMapId = new Attribute("id", "BaseResultMap");
        Attribute resultMapExtends = new Attribute("extends", extendsAttr);
        Attribute resultMapType = new Attribute("type", typeAttr);
        resultMap.addAttribute(resultMapId);
        resultMap.addAttribute(resultMapExtends);
        resultMap.addAttribute(resultMapType);
        return resultMap;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String targetXmlPackage = this.properties.getProperty("targetXmlPackage");
        if (StringUtils.hasText(targetXmlPackage)) {
            this.targetXmlPackage = targetXmlPackage;
        } else {
            this.targetXmlPackage = this.getDefaultXmlPackage();
        }
        String targetXmlProject = this.properties.getProperty("targetXmlProject");
        if (StringUtils.hasText(targetXmlProject)) {
            this.targetXmlProject = targetXmlProject;
        } else {
            this.targetXmlProject = this.context.getSqlMapGeneratorConfiguration().getTargetProject();
        }
        String targetJavaPackage = this.properties.getProperty("targetJavaPackage");
        if (StringUtils.hasText(targetJavaPackage)) {
            this.targetJavaPackage = targetJavaPackage;
        } else {
            this.targetJavaPackage = this.getDefaultJavaPackage();
        }
        String targetJavaProject = this.properties.getProperty("targetJavaProject");
        if (StringUtils.hasText(targetJavaProject)) {
            this.targetJavaProject = targetJavaProject;
        } else {
            this.targetJavaProject = this.context.getJavaClientGeneratorConfiguration().getTargetProject();
        }
    }

    private String getDefaultJavaPackage() {
        String packageName = this.context.getJavaClientGeneratorConfiguration().getTargetPackage();
        return this.getDefaultPackage(packageName);
    }

    private String getDefaultXmlPackage() {
        String packageName = this.context.getSqlMapGeneratorConfiguration().getTargetPackage();
        return this.getDefaultPackage(packageName);
    }

    private String getDefaultPackage(String packageName) {
        String[] arr = packageName.split("\\.");
        if (arr.length > 0) {
            arr[arr.length - 1] = "dao";
            return Joiner.on(".").join(arr);
        }
        return packageName;
    }

    private boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }
}

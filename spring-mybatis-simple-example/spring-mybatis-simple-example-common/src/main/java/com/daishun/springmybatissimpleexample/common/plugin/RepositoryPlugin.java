package com.daishun.springmybatissimpleexample.common.plugin;

import com.alibaba.fastjson.JSON;
import com.daishun.springmybatissimpleexample.common.utils.FileUtils;
import com.google.common.base.Charsets;
import lombok.Data;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author daishun
 * @since 2019/7/30
 */
public class RepositoryPlugin extends PluginAdapter {

    private RepositoryConfig repositoryConfig;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> result = super.contextGenerateAdditionalJavaFiles(introspectedTable);
        FileCreator<GeneratedJavaFile> fileCreator = new JavaDaoCreator(this.repositoryConfig, this.context);
        return fileCreator.getAllFiles(result);
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> result = super.contextGenerateAdditionalXmlFiles(introspectedTable);
        FileCreator<GeneratedXmlFile> fileCreator = new XmlDaoCreator(this.repositoryConfig, this.context);
        return fileCreator.getAllFiles(result);
    }


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.repositoryConfig = JSON.parseObject(JSON.toJSONString(this.properties), RepositoryConfig.class);
    }

    @Data
    private static class RepositoryConfig {
        private String targetJavaPackage;

        private String targetJavaProject;

        private String targetXmlPackage;

        private String targetXmlProject;
    }


    interface FileCreator<T extends GeneratedFile> {

        boolean shouldCreateFile(TableConfiguration configuration);

        T createFile(TableConfiguration configuration);

        List<T> getAllFiles(List<T>  list);
    }

    static abstract class AbstractFileCreator<T extends GeneratedFile> implements FileCreator<T>{

        private RepositoryConfig repositoryConfig;

        private Context context;

        AbstractFileCreator(RepositoryConfig repositoryConfig, Context context) {
            this.context = context;
            this.repositoryConfig = repositoryConfig;
        }

        @Override
        public List<T> getAllFiles(List<T> list) {
            if (list == null){
                list = new ArrayList<>();
            }
            List<T> result = this.createFiles();
            list.addAll(result);
            return list;
        }

        private List<T> createFiles() {
            List<T> generatedFiles = new ArrayList<>();
            List<TableConfiguration> configurations = this.context.getTableConfigurations();
            for (TableConfiguration configuration : configurations) {
                if (this.shouldCreateFile(configuration)) {
                    T generatedFile = this.createFile(configuration);
                    generatedFiles.add(generatedFile);
                }
            }
            return generatedFiles;
        }
    }

    static class JavaDaoCreator extends AbstractFileCreator<GeneratedJavaFile> {

        private String targetPackage;

        private String targetPackagePath;

        private String targetProject;

        private String targetProjectPath;

        private Context context;

        @Override
        public boolean shouldCreateFile(TableConfiguration configuration) {
            String fileName = this.getFileName(configuration);
            File file = FileUtils.getFile(this.targetPackagePath, fileName);
            return !file.exists();
        }

        @Override
        public GeneratedJavaFile createFile(TableConfiguration configuration) {
            String className = this.getClassName(configuration);
            Interface mapperInterface = new Interface(String.format("%s.%s", this.targetPackage, className));
            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            JavaFormatter javaFormatter = this.context.getJavaFormatter();
            return new GeneratedJavaFile(mapperInterface, this.targetProject, Charsets.UTF_8.name(), javaFormatter);
        }

        JavaDaoCreator(RepositoryConfig repositoryConfig, Context context) {
            super(repositoryConfig, context);
            String userDir = FileUtils.getUserDir();
            this.targetPackage = repositoryConfig.targetJavaPackage;
            this.targetProject = repositoryConfig.targetJavaProject;
            this.targetProjectPath = FileUtils.getFile(userDir, targetProject).getPath();
            this.targetPackagePath = FileUtils.getFile(this.targetProjectPath, this.targetPackage.replaceAll("\\.", File.separator)).getPath();
            this.context = context;
        }


        private String getFileName(TableConfiguration configuration) {
            return this.getClassName(configuration) + ".java";
        }

        private String getClassName(TableConfiguration configuration) {
            return configuration.getDomainObjectName() + "Dao";
        }

    }

    static class XmlDaoCreator extends AbstractFileCreator<GeneratedXmlFile> {

        private String targetPackage;

        private String targetPackagePath;

        private String targetProject;

        private String targetProjectPath;

        private RepositoryConfig repositoryConfig;

        private Context context;

        @Override
        public boolean shouldCreateFile(TableConfiguration configuration) {
            String fileName = this.getFileName(configuration);
            File xmlFile = FileUtils.getFile(this.targetPackagePath, fileName);
            return !xmlFile.exists();
        }

        @Override
        public GeneratedXmlFile createFile(TableConfiguration configuration) {
            Document document = this.createXmlDocument(configuration);
            String fileName = this.getFileName(configuration);
            return new GeneratedXmlFile(document, fileName, this.targetPackage, this.targetProject,
                    true, this.context.getXmlFormatter());
        }

        private Document createXmlDocument(TableConfiguration configuration) {
            Document document = new Document(
                    XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                    XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
            XmlElement rootElement = this.createMapperElement(configuration);
            document.setRootElement(rootElement);
            return document;
        }

        private XmlElement createMapperElement(TableConfiguration configuration) {
            String xmlName = configuration.getDomainObjectName()+"Dao";
            String targetJavaPackage = this.repositoryConfig.getTargetJavaPackage();
            XmlElement mapperElement = new XmlElement("mapper");
            Attribute namespace = new Attribute("namespace", targetJavaPackage + "." + xmlName);
            mapperElement.addAttribute(namespace);
            mapperElement.addElement(this.createBaseResultMap(configuration));
            return mapperElement;
        }

        private XmlElement createBaseResultMap(TableConfiguration configuration) {
            String mapperName = configuration.getDomainObjectName() + "Mapper";
            String mapperPackage = this.context.getJavaClientGeneratorConfiguration().getTargetPackage();
            String modelPackage = this.context.getJavaModelGeneratorConfiguration().getTargetPackage();
            String extendsAttr = String.format("%s.%s.BaseResultMap", mapperPackage, mapperName);
            String typeAttr = String.format("%s.%s", modelPackage, configuration.getDomainObjectName());
            XmlElement resultMap = new XmlElement("resultMap");
            Attribute resultMapId = new Attribute("id", "BaseResultMap");
            Attribute resultMapExtends = new Attribute("extends", extendsAttr);
            Attribute resultMapType = new Attribute("type", typeAttr);
            resultMap.addAttribute(resultMapId);
            resultMap.addAttribute(resultMapExtends);
            resultMap.addAttribute(resultMapType);
            return resultMap;
        }

        private String getFileName(TableConfiguration configuration) {
            return configuration.getDomainObjectName() + "Dao.xml";
        }

        XmlDaoCreator(RepositoryConfig repositoryConfig, Context context) {
            super(repositoryConfig, context);
            String userDir = FileUtils.getUserDir();
            this.repositoryConfig = repositoryConfig;
            this.targetPackage = repositoryConfig.getTargetXmlPackage();
            this.targetProject = repositoryConfig.getTargetXmlProject();
            this.targetProjectPath = FileUtils.getFile(userDir, targetProject).getPath();
            this.targetPackagePath = FileUtils.getFile(this.targetProjectPath,
                    this.targetPackage.replaceAll("\\.", File.separator)).getPath();
            this.context = context;
        }
    }
}

package com.daishun.springmybatissimpleexample.dao;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 行锁
 *
 * @author daishun
 * @since 2019/7/30
 */
public class RowLockProvider extends MapperTemplate {

    public RowLockProvider(Class<?> mapperClass, MapperHelper mapperHelper) {

        super(mapperClass, mapperHelper);
    }

    /**
     * 行锁sql生成
     *
     * @param ms MappedStatement
     * @return 行锁sql
     */
    public String selectByPrimaryKeyForUpdate(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        return SqlHelper.selectAllColumns(entityClass) +
                SqlHelper.fromTable(entityClass, this.tableName(entityClass)) +
                SqlHelper.wherePKColumns(entityClass) +
                " FOR UPDATE";
    }
}

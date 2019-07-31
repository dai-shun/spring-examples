package com.daishun.example.dao;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用mapper
 * @author daishun
 * @since 2019/7/30
 */
public interface MainMapper<T> extends BaseMapper<T>, ExampleMapper<T>, MySqlMapper<T> {

    /**
     * 根据主键id获取记录并加行锁
     *
     * @param primaryKey 主键id
     * @return 记录
     */
    @SelectProvider(type = RowLockProvider.class, method = "dynamicSQL")
    T selectByPrimaryKeyForUpdate(Object primaryKey);
}

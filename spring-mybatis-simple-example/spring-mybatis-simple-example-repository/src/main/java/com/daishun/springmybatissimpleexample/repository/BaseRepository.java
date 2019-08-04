package com.daishun.springmybatissimpleexample.repository;

import javax.annotation.Resource;

/**
 * @author daishun
 * @since 2019/7/30
 */
public abstract class BaseRepository<T> {
    @Resource
    protected MainMapper<T> mainMapper;

    public int insert(T model) {
        return mainMapper.insertSelective(model);
    }

    public int update(T model) {
        return mainMapper.updateByPrimaryKeySelective(model);
    }

    public T getByIdWithLock(Long id) {
        return mainMapper.selectByPrimaryKeyForUpdate(id);
    }

    public T getById(Long id) {
        return mainMapper.selectByPrimaryKey(id);
    }
}

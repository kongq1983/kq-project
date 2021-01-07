package com.kq.common.store.support;

import com.kq.common.store.DataStore;

/**
 * @author kq
 * @date 2021-01-07 16:13
 * @since 2020-0630
 */
public class DbDataStore implements DataStore {


    @Override
    public void save(String content) {
        System.out.println("数据库持久化!");
    }
}

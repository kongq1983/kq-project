package com.kq.common.util;

/**
 * @author kq
 * @date 2021-01-06 17:19
 * @since 2020-0630
 */
public class Holder<T> {

    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

}
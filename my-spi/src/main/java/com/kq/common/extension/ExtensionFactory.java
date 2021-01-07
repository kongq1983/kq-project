package com.kq.common.extension;

/**
 * @author kq
 * @date 2021-01-06 17:16
 * @since 2020-0630
 */
@SPI("spi")
public interface ExtensionFactory {

    /**
     * Get extension.
     *
     * @param type object type.
     * @param name object name.
     * @return object instance.
     */
    <T> T getExtension(Class<T> type, String name);

}

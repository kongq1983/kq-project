package com.kq.common.store.support;


import com.kq.common.extension.Adaptive;
import com.kq.common.extension.ExtensionLoader;
import com.kq.common.store.DataStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kq
 * @date 2021-01-07 16:35
 * @since 2020-0630
 */
@Adaptive
public class AdaptiveDataStore implements DataStore {

    private final List<DataStore> factories;

    public AdaptiveDataStore() {

        ExtensionLoader<DataStore> loader = ExtensionLoader.getExtensionLoader(DataStore.class);
        List<DataStore> list = new ArrayList<>();
//        for (String name : loader.getSupportedExtensions()) {
//            list.add(loader.getExtension(name));
//        }
        list.add(loader.getExtension("nosql"));
        list.add(loader.getExtension("db"));

        factories = Collections.unmodifiableList(list);
    }


    @Override
    public void save(String content) {
        System.out.println("AdaptiveDataStore save "+content);

    }
}

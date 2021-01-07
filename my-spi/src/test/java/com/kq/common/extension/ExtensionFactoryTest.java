package com.kq.common.extension;

import com.kq.common.store.DataStore;
import org.junit.Test;

/**
 * @author kq
 * @date 2021-01-07 14:51
 * @since 2020-0630
 */
public class ExtensionFactoryTest {



    @Test
    public void test(){
        ExtensionFactory extensionFactory = ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension();

        System.out.println("extensionFactory:"+extensionFactory);

    }

    @Test
    public void testDataStore() {
        DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getAdaptiveExtension();
        dataStore.save("hello");
    }


}

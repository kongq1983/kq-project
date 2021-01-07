package com.kq.common.extension;

import com.kq.common.compiler.support.JdkCompiler;
import com.kq.common.util.ClassUtils;
import com.kq.common.util.Holder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * @author kq
 * @date 2021-01-06 17:12
 * @since 2020-0630
 */
public class ExtensionLoader<T> {

    private static final String SERVICES_DIRECTORY = "META-INF/services/";
    private static final String DUBBO_DIRECTORY = "META-INF/dubbo/";
    private static final String DUBBO_INTERNAL_DIRECTORY = DUBBO_DIRECTORY + "internal/";

    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();
    private final Holder<Object> cachedAdaptiveInstance = new Holder<>();
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();
    private volatile Throwable createAdaptiveInstanceError;
    private volatile Class<?> cachedAdaptiveClass = null;
    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");
    private String cachedDefaultName;



    private final Class<?> type;

    private final ExtensionFactory objectFactory;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
        objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
    }


    /** 获取ExtensionLoader */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) { // 非接口，则报错
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }
        if (!withExtensionAnnotation(type)) { // 是否有SPI注解
            throw new IllegalArgumentException("Extension type (" + type +
                    ") is not an extension, because it is NOT annotated with @" + SPI.class.getSimpleName() + "!");
        }

        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) { // 不存在
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type)); //如果不存在，则初始化1个ExtensionLoader 类型是type 并放入EXTENSION_LOADERS
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    public T getAdaptiveExtension() {
        Object instance = cachedAdaptiveInstance.get();
        if (instance == null) {
            if (createAdaptiveInstanceError == null) {
                synchronized (cachedAdaptiveInstance) {
                    instance = cachedAdaptiveInstance.get();
                    if (instance == null) {
                        try {
                            instance = createAdaptiveExtension();
                            cachedAdaptiveInstance.set(instance);
                        } catch (Throwable t) {
                            createAdaptiveInstanceError = t;
                            throw new IllegalStateException("Failed to create adaptive instance: " + t.toString(), t);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("Failed to create adaptive instance: " + createAdaptiveInstanceError.toString(), createAdaptiveInstanceError);
            }
        }

        return (T) instance;
    }

    private T createAdaptiveExtension() {
        try {
            return injectExtension((T) getAdaptiveExtensionClass().newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Can't create adaptive extension " + type + ", cause: " + e.getMessage(), e);
        }
    }

    /**
     * 这里不处理
     * @param instance
     * @return
     */
    private T injectExtension(T instance) {
        return instance;
    }

    private Class<?> getAdaptiveExtensionClass() {
        getExtensionClasses();
        if (cachedAdaptiveClass != null) {
            return cachedAdaptiveClass;
        }
        return cachedAdaptiveClass = createAdaptiveExtensionClass();
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses(); // 加载class
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    com.kq.common.compiler.Compiler compiler = new JdkCompiler();

    private Class<?> createAdaptiveExtensionClass() {
//        String code = new AdaptiveClassCodeGenerator(type, cachedDefaultName).generate();
        String code = null;
        ClassLoader classLoader = findClassLoader();
//        org.apache.dubbo.common.compiler.Compiler compiler = ExtensionLoader.getExtensionLoader(org.apache.dubbo.common.compiler.Compiler.class).getAdaptiveExtension();
        return compiler.compile(code, classLoader);
    }

    private static ClassLoader findClassLoader() {
        return ClassUtils.getClassLoader(ExtensionLoader.class);
    }

    // synchronized in getExtensionClasses
    private Map<String, Class<?>> loadExtensionClasses() {
        cacheDefaultExtensionName();

        Map<String, Class<?>> extensionClasses = new HashMap<>();
//        loadDirectory(extensionClasses, DUBBO_INTERNAL_DIRECTORY, type.getName());
//        loadDirectory(extensionClasses, DUBBO_INTERNAL_DIRECTORY, type.getName().replace("org.apache", "com.alibaba"));
//        loadDirectory(extensionClasses, DUBBO_DIRECTORY, type.getName());
//        loadDirectory(extensionClasses, DUBBO_DIRECTORY, type.getName().replace("org.apache", "com.alibaba"));
//        loadDirectory(extensionClasses, SERVICES_DIRECTORY, type.getName());
//        loadDirectory(extensionClasses, SERVICES_DIRECTORY, type.getName().replace("org.apache", "com.alibaba"));
        return extensionClasses;
    }


    /** 设置spi的名称，如果指定了
     * extract and cache default extension name if exists
     */
    private void cacheDefaultExtensionName() {
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        if (defaultAnnotation != null) {
            String value = defaultAnnotation.value();
            if ((value = value.trim()).length() > 0) {
                String[] names = NAME_SEPARATOR.split(value);
                if (names.length > 1) {
                    throw new IllegalStateException("More than 1 default extension name on extension " + type.getName()
                            + ": " + Arrays.toString(names));
                }
                if (names.length == 1) {
                    cachedDefaultName = names[0];
                }
            }
        }
    }


    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }



}

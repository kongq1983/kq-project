package com.kq.common.compiler.support;

import com.kq.common.extension.ExtensionLoader;
import com.kq.common.util.ClassUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;

/**
 * @author kq
 * @date 2021-01-06 18:36
 * @since 2020-0630
 */
public class JdkCompilerTest {

    public static void main(String[] args) throws Exception {
        com.kq.common.compiler.Compiler compiler = new JdkCompiler();

        String content = IOUtils.toString(JdkCompilerTest.class.getResourceAsStream("/resources/Helloworld"), Charset.forName("UTF-8"));

//        System.out.println("content="+content);

        Class clazz = compiler.compile(content,findClassLoader());
        System.out.println("clazz="+clazz);

        Object obj = clazz.newInstance();

        BeanUtils.setProperty(obj,"name","king");

        String name = BeanUtils.getProperty(obj,"name");

        System.out.println("name="+name);
    }

    private static ClassLoader findClassLoader() {
        return ClassUtils.getClassLoader(ExtensionLoader.class);
    }



}

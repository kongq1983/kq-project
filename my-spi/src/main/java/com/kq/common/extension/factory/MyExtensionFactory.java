/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kq.common.extension.factory;


import com.kq.common.extension.ExtensionFactory;

/**
 * 每次都初始化
 */
public class MyExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {

        try {
            T t = (T) type.newInstance();
            System.out.println("MyExtensionFactory type="+type+" , t="+t);
            return t;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString() {
        return "MyExtensionFactory{}";
    }
}

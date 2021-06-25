package com.qytx.JvmTest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hjb
 * @date 2021/6/26
 */
public class XClassLoader extends ClassLoader {

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        InputStream is = null;
        Class<?> clazz = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(name + ".xlass");
            int length = is.available();
            byte[] byteArray = new byte[length];
            is.read(byteArray);
            byte[] classBytes = new byte[length];
            for (int i = 0; i < length; i++) {
                classBytes[i] = (byte) (255 - byteArray[i]);
            }
            clazz = defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e){
            throw new ClassNotFoundException(name);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return clazz;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassLoader classLoader = new XClassLoader();
        Class<?> clazz = classLoader.loadClass("Hello");
        /*for (Method m : clazz.getDeclaredMethods()) {
            System.out.println(clazz.getSimpleName() + "." + m.getName());
        }*/
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(instance);

    }
}

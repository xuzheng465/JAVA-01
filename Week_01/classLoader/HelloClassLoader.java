package week1.jvm;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这个文件自定义了一个类加载器。读取一个文件的字节，然后用offset decode整个文件字节 再用defineClass把decode后的字节加载回去。
 */
public class HelloClassLoader extends ClassLoader {
    private final static int OFFSET = 255;
    private final static String FILE_NAME = "Hello.xlass";

    public static void main(String[] args) {
        try {
            Object obj = new HelloClassLoader().findClass("Hello").getConstructor().newInstance();
            Class<?> classType = obj.getClass();
            Method aMethod = classType.getMethod("hello");
            aMethod.invoke(obj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] source = loadFileToBytes(FILE_NAME);
        if (source == null) {
            throw new ClassNotFoundException(FILE_NAME + " not found.");
        }
        byte[] bytes = decode(source);

        return defineClass(name, bytes, 0, bytes.length);
    }

    // 将fileName指明的文件加载到byte数组里
    private byte[] loadFileToBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        byte[] bytes = null;
        try {
            in = new FileInputStream(file);
            bytes = new byte[in.available()];
            in.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    // 通过offset解码字节数组
    private byte[] decode(byte[] source) {
        byte[] bytes = new byte[source.length];
        for (int i = 0; i < source.length; i++) {
            bytes[i] = (byte) (OFFSET - source[i]);
        }

        return bytes;
    }

}

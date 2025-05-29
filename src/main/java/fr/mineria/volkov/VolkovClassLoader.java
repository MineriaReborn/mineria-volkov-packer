package fr.mineria.volkov;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class VolkovClassLoader extends ClassLoader {

    private final Map<String, byte[]> classMap;
    private final Map<String, byte[]> resourceMap;

    public VolkovClassLoader(Map<String, byte[]> classMap, Map<String, byte[]> resourceMap) {
        super(ClassLoader.getSystemClassLoader());
        this.classMap = classMap;
        this.resourceMap = resourceMap;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classMap.containsKey(name)) {
            byte[] bytes = classMap.get(name);
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (resourceMap.containsKey(name)) {
            byte[] data = resourceMap.get(name);
            return new ByteArrayInputStream(data);
        }
        return super.getResourceAsStream(name);
    }
}

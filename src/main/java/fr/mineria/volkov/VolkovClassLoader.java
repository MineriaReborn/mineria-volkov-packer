package fr.mineria.volkov;

import java.util.Map;

public class VolkovClassLoader extends ClassLoader {

    private final Map<String, byte[]> classMap;

    public VolkovClassLoader(Map<String, byte[]> classMap) {
        super(ClassLoader.getSystemClassLoader());
        this.classMap = classMap;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classMap.containsKey(name)) {
            byte[] bytes = classMap.get(name);
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }
}


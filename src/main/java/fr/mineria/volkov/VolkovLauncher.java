package fr.mineria.volkov;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class VolkovLauncher {

    private String secretKey;
    private String encryptedJarPath;
    private String mainClass;
    private String[] args;

    public VolkovLauncher withKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public VolkovLauncher withEncryptedPath(String encryptedJarPath) {
        this.encryptedJarPath = encryptedJarPath;
        return this;
    }

    public VolkovLauncher withMainClass(String mainClass) {
        this.mainClass = mainClass;
        return this;
    }

    public VolkovLauncher withArgs(String[] args) {
        this.args = args;
        return this;
    }

    public void launch() throws Exception {
        byte[] encryptedJar = Files.readAllBytes(Paths.get(encryptedJarPath));
        byte[] decryptedJar = decryptJar(encryptedJar, secretKey);
        Map<String, byte[]> classMap = extractClasses(decryptedJar);

        ClassLoader loader = new VolkovClassLoader(classMap);
        Class<?> mainClassRef = loader.loadClass(mainClass);
        Method mainMethod = mainClassRef.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) args);
    }

    private byte[] decryptJar(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, spec);
        return cipher.doFinal(data);
    }

    private Map<String, byte[]> extractClasses(byte[] jarBytes) throws Exception {
        Map<String, byte[]> classes = new HashMap<>();

        try (JarInputStream jarStream = new JarInputStream(new ByteArrayInputStream(jarBytes))) {
            JarEntry entry;
            while ((entry = jarStream.getNextJarEntry()) != null) {
                if (!entry.getName().endsWith(".class")) continue;

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] temp = new byte[4096];
                int read;
                while ((read = jarStream.read(temp)) != -1) {
                    buffer.write(temp, 0, read);
                }

                String className = entry.getName()
                        .replace("/", ".")
                        .replace(".class", "");

                classes.put(className, buffer.toByteArray());
            }
        }

        return classes;
    }
}
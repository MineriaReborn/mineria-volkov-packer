package fr.mineria.volkov;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
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
        Map<String, Map<String, byte[]>> jarContent = decryptAndExtractJar(encryptedJarPath, secretKey);
        Map<String, byte[]> classMap = jarContent.get("classes");
        Map<String, byte[]> resourceMap = jarContent.get("resources");

        ClassLoader loader = new VolkovClassLoader(classMap, resourceMap);
        Class<?> mainClassRef = loader.loadClass(mainClass);
        Method mainMethod = mainClassRef.getMethod("main", String[].class);

        mainMethod.invoke(null, (Object) args);
    }

    private Map<String, Map<String, byte[]>> decryptAndExtractJar(String inputPath, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[16];
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, spec, ivSpec);

        try (InputStream fis = new FileInputStream(inputPath);
             CipherInputStream cis = new CipherInputStream(fis, cipher);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return extractJar(new ByteArrayInputStream(bos.toByteArray()));
        }
    }

    private Map<String, Map<String, byte[]>> extractJar(InputStream decryptedJarStream) throws Exception {
        Map<String, byte[]> classMap = new HashMap<>();
        Map<String, byte[]> resourceMap = new HashMap<>();

        try (JarInputStream jarInputStream = new JarInputStream(decryptedJarStream)) {
            JarEntry entry;

            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                if (entry.isDirectory()) continue;

                String entryName = entry.getName();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = jarInputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                byte[] data = baos.toByteArray();

                if (entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                    classMap.put(className, data);
                } else {
                    resourceMap.put(entryName, data);
                }
            }
        }

        Map<String, Map<String, byte[]>> result = new HashMap<>();
        result.put("classes", classMap);
        result.put("resources", resourceMap);

        return result;
    }
}

package fr.mineria.volkov;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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
        try (InputStream fis = new FileInputStream(inputPath)) {

            byte[] iv = new byte[16];
            int readBytes = fis.read(iv);
            if (readBytes != 16) {
                throw new IOException("Unable to read full IV (16 bytes required)");
            }
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length != 16 && keyBytes.length != 32) {
                throw new IllegalArgumentException("Key must be 16 (AES-128) or 32 (AES-256) bytes after UTF-8 encoding.");
            }
            SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, spec, ivSpec);

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = cis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }

                return extractJar(new ByteArrayInputStream(bos.toByteArray()));
            }
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

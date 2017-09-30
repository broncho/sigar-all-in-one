package com.github.broncho.sigar;

import org.hyperic.sigar.Sigar;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Author: ZhangXiao
 * Created: 2017/9/30
 */
public final class GoSigar {
    
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    
    private static final String SIGAR_LIB = "sigarlib";
    
    private static Sigar sigar;
    
    private static final String JAR_NAME = "sigar-all-in-one-1.6.4.jar";
    
    private GoSigar() {
    
    }
    
    public static Sigar sigar() {
        String path = System.getProperty("java.class.path", "");
        StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);
        String root = System.getProperty("java.io.tmpdir", System.getProperty("user.home", System.getProperty("user.dir")));
        
        
        String sigarlibPath = "";
        while (tok.hasMoreTokens()) {
            path = tok.nextToken();
            File file = new File(path);
            if (file.isFile() && path.endsWith(JAR_NAME)) {
                extractSigarLib(root, file.getAbsolutePath());
                sigarlibPath = root + File.separator + SIGAR_LIB;
                break;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.isDirectory() && pathname.getName().equals(SIGAR_LIB);
                    }
                });
                if (files != null && files.length > 0) {
                    sigarlibPath = files[0].getAbsolutePath();
                    break;
                }
            }
        }
        
        if (sigarlibPath.equals("")) {
            throw new RuntimeException("Not found " + JAR_NAME + " or " + SIGAR_LIB + " in java.class.path");
        }
        
        if (sigar == null) {
            System.setProperty(
                    JAVA_LIBRARY_PATH,
                    System.getProperty(JAVA_LIBRARY_PATH, "")
                            + File.pathSeparator
                            + sigarlibPath
            );
            sigar = new Sigar();
        }
        return sigar;
    }
    
    private static void extractSigarLib(String root, String jar) {
        JarFile jarFile;
        try {
            jarFile = new JarFile(jar);
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                JarEntry entry = jarEntryEnumeration.nextElement();
                if (!entry.getName().startsWith(SIGAR_LIB)) {
                    continue;
                }
                if (entry.isDirectory()) {
                    File dir = new File(root, entry.getName());
                    dir.mkdirs();
                } else {
                    File entryFile = new File(root, entry.getName());
                    if (!entryFile.exists()) {
                        String parentPath = entryFile.getParent();
                        File parentDir = new File(parentPath);
                        parentDir.mkdirs();
                        
                    }
                    InputStream fis = null;
                    FileOutputStream fos = null;
                    try {
                        fis = jarFile.getInputStream(entry);
                        fos = new FileOutputStream(entryFile);
                        int count;
                        byte[] buf = new byte[8192];
                        while ((count = fis.read(buf)) != -1) {
                            fos.write(buf, 0, count);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

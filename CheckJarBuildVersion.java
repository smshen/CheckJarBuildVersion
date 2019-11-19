package cn.shenshaomin;

import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author shenshaomin
 * @version 1.0
 * @date 15/12/7
 */
public class CheckJarBuildVersion {


    public static void parseJavaClassFile(InputStream classByteStream, String jarFileName) throws Exception {
        DataInputStream dataInputStream = new DataInputStream(classByteStream);
        int magicNumber = dataInputStream.readInt();
        if (magicNumber == 0xCAFEBABE) {
            int minorVer = dataInputStream.readUnsignedShort();
            int majorVer = dataInputStream.readUnsignedShort();
            // do something here with major & minor numbers
            // javap -verbose SomeClass | grep major
            // J2SE 8.0 = 52 (0x34 hex)
            // J2SE 7.0 = 51 (0x33 hex)
            // J2SE 6.0 = 50 (0x32 hex)
            // J2SE 5.0 = 49 (0x31 hex)
            // JDK  1.4 = 48 (0x30 hex)
            // JDK  1.3 = 47 (0x2F hex)
            // JDK  1.2 = 46 (0x2E hex)
            // JDK  1.1 = 45 (0x2D hex)

            // 大于jdk1.6
            if (majorVer >= 51) {
                System.out.println("jarFileName:" + jarFileName + ", minorVer:" + minorVer + ", majorVer" + majorVer);
            }
        }
    }

    public static void check(String classPath) throws Exception {
        File dir = new File(classPath);
        File[] jarFiles = dir.listFiles();
        boolean flag = true;
        for (File jarFile : jarFiles) {
            if (jarFile.getName().endsWith(".jar")) {
                JarFile jar = new JarFile(jarFile);
                flag = true;
                Enumeration<JarEntry> enumeration = jar.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = enumeration.nextElement();
                    if (jarEntry.getName().endsWith(".class")) {
                        if (flag) {
                            flag = false;
                            InputStream input = jar.getInputStream(jarEntry);
                            parseJavaClassFile(input, jar.getName());
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        args = new String[]{"/Users/ice/Downloads/lib"};
        for (String arg : args) {
            check(arg);
        }
    }
}

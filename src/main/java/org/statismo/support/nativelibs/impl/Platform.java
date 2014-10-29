package org.statismo.support.nativelibs.impl;

public class Platform {
    // Utility class only, can't be instantiated
    private Platform() {}

    public static final String PLATFORM_UNKNOWN = "UNKNOWN";
    public static final String PLATFORM_WIN32 = "windows_x86";
    public static final String PLATFORM_WIN64 = "windows_amd64";
    public static final String PLATFORM_LINUX64 = "linux_amd64";
    public static final String PLATFORM_MAC64 = "mac_x86_64";

    public static boolean isLinux() {
        String p = getPlatform();
        return p.equals(PLATFORM_LINUX64);
    }

    public static boolean isWindows() {
        String p = getPlatform();
        return p.equals(PLATFORM_WIN64) || p.equals(PLATFORM_WIN32);
    }

    public static boolean isMac() {
        return getPlatform().equals(PLATFORM_MAC64);
    }

    public static boolean isUnknown() {
        return getPlatform().equals(PLATFORM_UNKNOWN);
    }

    private static String platform = null;
    private static final Object lock = new Object();

    public static String getPlatform() {
        if (platform == null) {
            synchronized (lock) {
                if (platform == null) {
                    try {
                        String os = System.getProperty("os.name").trim().toLowerCase();
                        int space = os.indexOf(" ");
                        if (space > 0) {
                            os = os.substring(0, space);
                        }
                        String arch = System.getProperty("os.arch").trim().toLowerCase();
                        platform = os + "_" + arch;
                    } catch (Throwable t) {
                        platform = PLATFORM_UNKNOWN;
                    }
                }
            }
        }
        return platform;
    }
}

package com.example.xingxiaogang.animationdemo.utils;

import java.io.FileInputStream;

public class LinuxUtils {
    public LinuxUtils() {
    }

    public static String getCurrentProcessName() {
        FileInputStream var0 = null;

        String var5 = null;
        try {
            var0 = new FileInputStream("/proc/self/cmdline");
            byte[] var1 = new byte[256];

            int var2;
            int var3;
            for (var2 = 0; (var3 = var0.read()) > 0 && var2 < var1.length; var1[var2++] = (byte) var3) {
                ;
            }

            if (var2 <= 0) {
                return null;
            }

            String var4 = new String(var1, 0, var2, "UTF-8");
            var5 = var4;
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (var0 != null) {
                try {
                    var0.close();
                } catch (Exception var15) {
                    ;
                }
            }

        }

        return var5;
    }
}

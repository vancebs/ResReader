package com.hf.resreader.resreader.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Fan on 2015/8/21.
 * Resource ID reader
 */
public class ResIdReader {
    public static final String TAG = "ResIdReader";

    private static final String INTERNAL_PKG = "com.android.internal";
    public static final String[] NATIVE_PKGS = new String[]{
            "android",
            INTERNAL_PKG
    };

    private ResIdReader() {
        // hide constructor
    }

    protected static boolean isNativePkg(String pkg) {
        if (pkg == null || pkg.isEmpty()) {
            return true;
        }

        for (String p : NATIVE_PKGS) {
            if (p.equals(pkg)) {
                return true;
            }
        }

        return false;
    }

    public static int getResId(Context context, String pkg, String type, String key) {
        if (isNativePkg(pkg)) {
            return getResId2(pkg, type, key);
        }

        return context.getResources().getIdentifier(key, type, pkg);
    }

    /**
     * Another way to get resource ID.
     * @param pkg package name
     * @param type resource type
     * @param key resource type (name)
     * @return resource ID
     */
    private static int getResId2( String pkg, String type, String key) {
        int resId = -1;
        try {
            String clazzName = pkg + ".R$" + type;
            Class<?> clazz = Class.forName(clazzName);
            Field field = clazz.getField(key);
            resId = field.getInt(null);
        } catch (ClassNotFoundException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
            Log.w(TAG, "getResId2()# " + String.format("[pkg: %s, type: %s, key: %s]", pkg, type, key), e);
        }

        return resId;
    }


    public static Context getPkgContext(Context context, String pkg) {
        Context resContext;

        if (isNativePkg(pkg)) {
            resContext = context;
        } else {
            resContext = createContext(context, pkg);
            if (resContext == null) {
                resContext = context;
            }
        }

        return resContext;
    }

    private static Context createContext(Context context, String pkg) {
        try {
            return context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Create context failed! pkg: " + pkg, e);
            return null;
        }
    }
}

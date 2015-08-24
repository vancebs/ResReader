package com.hf.resreader.resreader;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by Fan on 2015/8/21.
 * Array resource reader
 */
public class ArrayResReader extends AbstractStringTypeResReader {
    public static final String TAG = "ArrayResReader";

    @Override
    public String getValue(Context resContext, int resId) {
        String value;
        Resources res = resContext.getResources();

        try {
            int[] intArray = res.getIntArray(resId);
            String[] strArray = res.getStringArray(resId);
            boolean isIntEmpty = isEmptyArray(intArray);
            boolean isStrEmpty = isEmptyArray(strArray);

            if (isIntEmpty == isStrEmpty) {
                value = array2String(intArray) + array2String(strArray);
            } else if (isIntEmpty) {
                value = array2String(strArray);
            } else if (isStrEmpty) {
                value = array2String(intArray);
            } else {
                value = "Should not be here";
            }

        } catch (Resources.NotFoundException e) {
            Log.d(TAG, "Cannot find resource.", e);
            value = "Failed to get the resource";
        }

        return value;
    }

    private boolean isEmptyArray(int[] array) {
        for (int i : array) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmptyArray(String[] array) {
        for (String s : array) {
            if (s != null) {
                return false;
            }
        }
        return true;
    }

    private String array2String(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append(">> getIntArray()\n");
        for (int i=0; i<array.length; i++) {
            sb.append("[").append(i).append("] ").append(array[i]).append("\n");
        }
        return sb.toString();
    }

    private String array2String(String[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append(">> getStringArray()\n");
        for (int i=0; i<array.length; i++) {
            sb.append("[").append(i).append("] ").append(array[i]).append("\n");
        }
        return sb.toString();
    }
}

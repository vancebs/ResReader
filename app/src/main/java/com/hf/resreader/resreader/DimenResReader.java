package com.hf.resreader.resreader;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by Fan on 2015/8/21.
 * Dimen resource reader
 */
public class DimenResReader extends AbstractStringTypeResReader {
    public static final String TAG = "DimenResReader";

    private static final String VALUE_FORMAT = "getDimension(): %fdp\ngetDimensionPixelOffset(): %dpx\ngetDimensionPixelSize: %dpx";

    @Override
    public String getValue(Context resContext, int resId) {
        // ResourceNotFoundException Already handled by super class
        Resources res = resContext.getResources();
        float dp = res.getDimension(resId);
        int px = res.getDimensionPixelOffset(resId);
        int size = res.getDimensionPixelSize(resId);
        return String.format(VALUE_FORMAT, dp, px, size);
    }
}

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
        Resources res = resContext.getResources();
        String value;
        try {
            float dp = res.getDimension(resId);
            int px = res.getDimensionPixelOffset(resId);
            int size = res.getDimensionPixelSize(resId);
            value = String.format(VALUE_FORMAT, dp, px, size);
        } catch (Resources.NotFoundException e) {
            Log.d(TAG, "Resource not found.", e);
            value = "Resource not found";
        }

        return value;
    }
}

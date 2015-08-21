package com.hf.resreader.resreader;

import android.content.Context;

/**
 * Created by Fan on 2015/8/20.
 * Boolean Reader
 */
public class BooleanResReader extends AbstractStringTypeResReader {
    @Override
    public String getValue(Context resContext, int resId) {
        return String.valueOf(resContext.getResources().getBoolean(resId));
    }
}

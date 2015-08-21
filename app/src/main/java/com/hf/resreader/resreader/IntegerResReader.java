package com.hf.resreader.resreader;

import android.content.Context;

/**
 * Created by Fan on 2015/8/20.
 * Integer Reader
 */
public class IntegerResReader extends AbstractStringTypeResReader {
    @Override
    public String getValue(Context resContext, int resId) {
        return String.valueOf(resContext.getResources().getInteger(resId));
    }
}

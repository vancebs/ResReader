package com.hf.resreader.resreader;

import android.content.Context;

/**
 * Created by Fan on 2015/8/20.
 */
public class StringResReader extends AbstractStringTypeResReader {
    @Override
    public String getValue(Context resContext, int resId) {
        return resContext.getResources().getString(resId);
    }
}

package com.hf.resreader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hf.resreader.resreader.BooleanResReader;
import com.hf.resreader.resreader.IResReader;
import com.hf.resreader.resreader.IntegerResReader;
import com.hf.resreader.resreader.InvalidTypeResReader;
import com.hf.resreader.resreader.StringResReader;

/**
 * Created by Fan on 2015/8/20.
 * Reader Manager
 */
public class ResReaderManager {
    private static final String TYPE_INTEGER = "integer";
    private static final String TYPE_BOOLEAN = "bool";
    private static final String TYPE_STRING = "string";

    private ViewGroup mContainer;
    private Context mContext;

    // Save last type & view.In case we can reuse the view if type is not changed
    private int mLastValueViewResId = -1;
    private View mLastValueView = null;

    public ResReaderManager(Context context, ViewGroup container) {
        mContext = context;
        mContainer = container;
    }

    private static IResReader getReader(String type) {
        if (TYPE_INTEGER.equals(type)) {
            return new IntegerResReader();
        } else if (TYPE_BOOLEAN.equals(type)) {
            return new BooleanResReader();
        } else if (TYPE_STRING.equals(type)) {
            return new StringResReader();
        } else {
            return new InvalidTypeResReader();
        }
    }

    public void read( String pkg, String type, String key) {
        // read the resource
        IResReader reader = getReader(type);

        // read the value and reuse last view if possible
        View view;
        if (reader.getViewResId() == mLastValueViewResId) {
            view = reader.read(mContext, mLastValueView, pkg, type, key);
        } else {
            mLastValueViewResId = reader.getViewResId();
            view = reader.read(mContext, null, pkg, type, key);
        }

        // update value view
        if (view != mLastValueView) {
            mLastValueView = view;
            mContainer.removeAllViews();
            mContainer.addView(mLastValueView);
        }

        // update visibility
        if (mContainer.getVisibility() != View.VISIBLE) {
            mContainer.setVisibility(View.VISIBLE);
        }
    }

    public void clear() {
        mContainer.setVisibility(View.INVISIBLE);
    }
}

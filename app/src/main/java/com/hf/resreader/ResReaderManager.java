package com.hf.resreader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hf.resreader.resreader.ArrayResReader;
import com.hf.resreader.resreader.BooleanResReader;
import com.hf.resreader.resreader.ColorResReader;
import com.hf.resreader.resreader.DimenResReader;
import com.hf.resreader.resreader.DrawableResReader;
import com.hf.resreader.resreader.IResReader;
import com.hf.resreader.resreader.IntegerResReader;
import com.hf.resreader.resreader.InvalidTypeResReader;
import com.hf.resreader.resreader.StringResReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fan on 2015/8/20.
 * Reader Manager
 */
public class ResReaderManager {
    public static final String TAG = "ResReaderManager";

    public static final String TYPE_ANIM = "anim";
    public static final String TYPE_ANIMATOR = "animator";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_ATTR = "attr";
    public static final String TYPE_BOOLEAN = "bool";
    public static final String TYPE_COLOR = "color";
    public static final String TYPE_DIMEN = "dimen";
    public static final String TYPE_DRAWABLE = "drawable";
    public static final String TYPE_FRACTION = "fraction";
    public static final String TYPE_ID = "id";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_INTERPOLATOR = "interpolator";
    public static final String TYPE_LAYOUT = "layout";
    public static final String TYPE_MENU = "menu";
    public static final String TYPE_MIPMAP = "mipmap";
    public static final String TYPE_PLURALS = "plurals";
    public static final String TYPE_RAW = "raw";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_STYLE = "style";
    public static final String TYPE_TRANSITION = "transition";
    public static final String TYPE_XML = "xml";
    public static final String TYPE_STYLEABLE = "styleable";

    private static Map<String, Class<?>> mReaderMap = null;

    private ViewGroup mContainer;
    private Context mContext;

    // Save last type & view.In case we can reuse the view if type is not changed
    private int mLastValueViewResId = -1;
    private View mLastValueView = null;

    public ResReaderManager(Context context, ViewGroup container) {
        mContext = context;
        mContainer = container;
    }

    private static Map<String, Class<?>> getReaderMap() {
        if (mReaderMap == null) {
            mReaderMap = new HashMap<>();

            mReaderMap.put(TYPE_ANIM, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_ANIMATOR, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_ARRAY, ArrayResReader.class);
            mReaderMap.put(TYPE_ATTR, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_BOOLEAN, BooleanResReader.class);
            mReaderMap.put(TYPE_COLOR, ColorResReader.class);
            mReaderMap.put(TYPE_DIMEN, DimenResReader.class);
            mReaderMap.put(TYPE_DRAWABLE, DrawableResReader.class);
            mReaderMap.put(TYPE_FRACTION, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_ID, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_INTEGER, IntegerResReader.class);
            mReaderMap.put(TYPE_INTERPOLATOR, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_LAYOUT, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_MENU, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_MIPMAP, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_PLURALS, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_RAW, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_STRING, StringResReader.class);
            mReaderMap.put(TYPE_STYLE, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_TRANSITION, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_XML, InvalidTypeResReader.class); // TODO
            mReaderMap.put(TYPE_STYLEABLE, InvalidTypeResReader.class); // TODO
        }

        return mReaderMap;
    }

    private static IResReader getReader(String type) {
        Class<?> clazz = getReaderMap().get(type);
        if (clazz == null) {
            return new InvalidTypeResReader();
        } else {
            try {
                return (IResReader) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                Log.d(TAG, "new ResReader instance failed.", e);
                return new InvalidTypeResReader();
            }
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

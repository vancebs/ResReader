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

    private static final IResReader INVALID_TYPE_RES_READER = new InvalidTypeResReader();
    private static final Map<String, IResReader> RES_READER_MAP = new HashMap<String, IResReader>(){
        {
            put(TYPE_ANIM, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_ANIMATOR, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_ARRAY, new ArrayResReader());
            put(TYPE_ATTR, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_BOOLEAN, new BooleanResReader());
            put(TYPE_COLOR, new ColorResReader());
            put(TYPE_DIMEN, new DimenResReader());
            put(TYPE_DRAWABLE, new DrawableResReader());
            put(TYPE_FRACTION, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_ID, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_INTEGER, new IntegerResReader());
            put(TYPE_INTERPOLATOR, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_LAYOUT, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_MENU, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_MIPMAP, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_PLURALS, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_RAW, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_STRING, new StringResReader());
            put(TYPE_STYLE, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_TRANSITION, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_XML, INVALID_TYPE_RES_READER); // TODO
            put(TYPE_STYLEABLE, INVALID_TYPE_RES_READER); // TODO
        }
    };

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
        IResReader reader = RES_READER_MAP.get(type);
        if (reader == null) {
            Log.w(TAG, "Invalid type: " + type);
            reader = new InvalidTypeResReader();
        }

        return reader;
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

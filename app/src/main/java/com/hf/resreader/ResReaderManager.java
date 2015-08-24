package com.hf.resreader;

import android.content.Context;
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

/**
 * Created by Fan on 2015/8/20.
 * Reader Manager
 */
public class ResReaderManager {
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
        if (TYPE_ANIM.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_ANIMATOR.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_ARRAY.equals(type)) {
            return new ArrayResReader();
        } else if (TYPE_ATTR.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_BOOLEAN.equals(type)) {
            return new BooleanResReader();
        } else if (TYPE_COLOR.equals(type)) {
            return new ColorResReader();
        } else if (TYPE_DIMEN.equals(type)) {
            return new DimenResReader();
        } else if (TYPE_DRAWABLE.equals(type)) {
            return new DrawableResReader();
        } else if (TYPE_FRACTION.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_ID.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_INTEGER.equals(type)) {
            return new IntegerResReader();
        } else if (TYPE_INTERPOLATOR.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_LAYOUT.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_MENU.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_MIPMAP.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_PLURALS.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_RAW.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_STRING.equals(type)) {
            return new StringResReader();
        } else if (TYPE_STYLE.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_TRANSITION.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_XML.equals(type)) {
            return new InvalidTypeResReader(); // TODO
        } else if (TYPE_STYLEABLE.equals(type)) {
            return new InvalidTypeResReader(); // TODO
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

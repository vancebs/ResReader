package com.hf.resreader.resreader;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hf.resreader.R;
import com.hf.resreader.resreader.utils.ResIdReader;

/**
 * Created by Fan on 2015/8/20.
 * An abstract class that for all resources can be displayed as a string.
 * Resource types, like integer, boolean, dimen ... can extends this class for simple coding.
 */
public abstract class AbstractStringTypeResReader implements IResReader {
    @Override
    public int getViewResId() {
        return R.layout.string_value_view;
    }

    @Override
    public View read(Context context, View convertView, String pkg, String type, String key) {
        if (convertView == null) {
            convertView = View.inflate(context, getViewResId(), null);
        }

        TextView valueView = (TextView) convertView.findViewById(R.id.value);
        TextView resIdView = (TextView) convertView.findViewById(R.id.res_id);

        // get package context
        Context resContext = ResIdReader.getPkgContext(context, pkg);

        // get resource id
        int resId = ResIdReader.getResId(resContext, pkg, type, key);

        // set values
        resIdView.setText(String.valueOf(resId));
        valueView.setText(getValue(resContext, resId));

        return convertView;
    }

    public abstract String getValue(Context resContext, int resId);
}

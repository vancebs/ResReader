package com.hf.resreader.resreader;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hf.resreader.R;
import com.hf.resreader.resreader.utils.ResIdReader;

/**
 * Created by Fan on 2015/8/21.
 * Color resource reader
 */
public class ColorResReader implements IResReader {
    private static final String COLOR_STRING_FORMAT = "0x%X (%d)";

    @Override
    public View read(Context context, View convertView, String pkg, String type, String key) {
        if (convertView == null) {
            convertView = View.inflate(context, getViewResId(), null);
        }

        TextView resIdView = (TextView) convertView.findViewById(R.id.res_id);
        FrameLayout blockView = (FrameLayout) convertView.findViewById(R.id.color_block);
        TextView valueView = (TextView) convertView.findViewById(R.id.color_value);

        // get package context
        Context resContext = ResIdReader.getPkgContext(context, pkg);

        // get resource id
        int resId = ResIdReader.getResId(resContext, pkg, type, key);

        // get color
        int color;
        String msg;
        try {
            color = resContext.getResources().getColor(resId);
            msg = String.format(COLOR_STRING_FORMAT, color, color);
        } catch (Resources.NotFoundException e) {
            color = 0x000000;
            msg = "Resource not found";
        }

        // set values
        resIdView.setText(String.valueOf(resId));
        blockView.setBackgroundColor(color);
        valueView.setText(msg);

        return convertView;
    }

    @Override
    public int getViewResId() {
        return R.layout.color_value_view;
    }
}

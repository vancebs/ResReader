package com.hf.resreader.resreader;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hf.resreader.R;
import com.hf.resreader.resreader.utils.ResIdReader;

/**
 * Created by Fan on 2015/8/21.
 * Color resource reader
 */
public class ColorResReader implements IResReader {
    public static final String TAG = "ColorResReader";

    private static final String COLOR_STRING_FORMAT = "0x%X (%d)";

    @Override
    public View read(Context context, View convertView, String pkg, String type, String key) {
        View view = convertView;
        if (view == null) {
            view = View.inflate(context, getViewResId(), null);
        }

        TextView resIdView = (TextView) view.findViewById(R.id.res_id);
        ImageView blockView = (ImageView) view.findViewById(R.id.color_block);
        TextView valueView = (TextView) view.findViewById(R.id.color_value);

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
            Log.d(TAG, "Resource not found. So set color as 0x000000", e);
            color = 0x000000;
            msg = context.getResources().getString(R.string.err_not_found);
        }

        // set values
        resIdView.setText(String.valueOf(resId));
        blockView.setBackgroundColor(color);
        valueView.setText(msg);

        return view;
    }

    @Override
    public int getViewResId() {
        return R.layout.color_value_view;
    }
}

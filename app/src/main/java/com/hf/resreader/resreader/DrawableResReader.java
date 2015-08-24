package com.hf.resreader.resreader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hf.resreader.R;
import com.hf.resreader.resreader.utils.ResIdReader;

/**
 * Created by Fan on 2015/8/21.
 * Drawable resource reader
 */
public class DrawableResReader implements IResReader {
    public static final String TAG = "DrawableResReader";

    @Override
    public View read(Context context, View convertView, String pkg, String type, String key) {
        View view = convertView;
        if (view == null) {
            view = View.inflate(context, getViewResId(), null);
        }

        TextView resIdView = (TextView) view.findViewById(R.id.res_id);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        // get package context
        Context resContext = ResIdReader.getPkgContext(context, pkg);

        // get resource id
        int resId = ResIdReader.getResId(resContext, pkg, type, key);

        // get drawable
        Drawable drawable;
        try {
            drawable = resContext.getResources().getDrawable(resId, null);
        } catch (Resources.NotFoundException e) {
            Log.d(TAG, "Resource not found. So set drawable as null", e);
            drawable = null;
        }

        // set values
        if (drawable == null) {
            resIdView.setText(String.format("%d\nResource not found", resId));
            imageView.setImageDrawable(null);
        } else {
            resIdView.setText(String.valueOf(resId));
            imageView.setImageDrawable(drawable);
        }

        return view;
    }

    @Override
    public int getViewResId() {
        return R.layout.drawable_value_view;
    }
}

package com.hf.resreader.resreader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
    @Override
    public View read(Context context, View convertView, String pkg, String type, String key) {
        if (convertView == null) {
            convertView = View.inflate(context, getViewResId(), null);
        }

        TextView resIdView = (TextView) convertView.findViewById(R.id.res_id);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        // get package context
        Context resContext = ResIdReader.getPkgContext(context, pkg);

        // get resource id
        int resId = ResIdReader.getResId(resContext, pkg, type, key);

        // get drawable
        Drawable drawable;
        try {
            drawable = resContext.getResources().getDrawable(resId, null);
        } catch (Resources.NotFoundException e) {
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

        return convertView;
    }

    @Override
    public int getViewResId() {
        return R.layout.drawable_value_view;
    }
}

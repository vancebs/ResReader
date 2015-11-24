package com.hf.resreader.resreader;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hf.resreader.R;
import com.hf.resreader.resreader.utils.ResIdReader;

/**
 * Created by Fan on 2015/8/25.
 * Layout resource reader
 */
public class LayoutResReader implements IResReader {
    public static final String TAG = "LayoutResReader";

    @Override
    public View read(Context context, View convertView, String pkg, String type, String key) {
        View view = convertView;
        if (view == null) {
            view = View.inflate(context, getViewResId(), null);
        }

        TextView resIdView = (TextView) view.findViewById(R.id.res_id);
        TextView msgView = (TextView) view.findViewById(R.id.msg);
        FrameLayout layoutView = (FrameLayout) view.findViewById(R.id.layout_container);

        // get package context
        Context resContext = ResIdReader.getPkgContext(context, pkg);

        // get resource id
        int resId = ResIdReader.getResId(resContext, pkg, type, key);

        // get layout
        layoutView.removeAllViews();
        try {
            View layout = View.inflate(resContext, resId, null);
            msgView.setVisibility(View.GONE);
            layoutView.addView(layout);
        } catch (Resources.NotFoundException e) {
            Log.d(TAG, "Resource not found.", e);
            msgView.setText(R.string.err_not_found);
            msgView.setVisibility(View.VISIBLE);
        }

        // set res id
        resIdView.setText(String.valueOf(resId));

        return view;
    }

    @Override
    public int getViewResId() {
        return R.layout.layout_value_view;
    }
}

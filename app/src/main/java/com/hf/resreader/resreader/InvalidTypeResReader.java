package com.hf.resreader.resreader;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hf.resreader.R;

/**
 * Created by Fan on 2015/8/20.
 * Invalid Reader. Used when there's an error.
 */
public class InvalidTypeResReader implements IResReader {
    @Override
    public int getViewResId() {
        return R.layout.string_value_view;
    }

    @Override
    public View read(Context context, View convertView, String pkg, String type, String key) {
        View view = convertView;
        if (view == null) {
            view = View.inflate(context, getViewResId(), null);
        }

        TextView valueView = (TextView) view.findViewById(R.id.value);
        TextView resIdView = (TextView) view.findViewById(R.id.res_id);

        resIdView.setText(String.valueOf(-1));
        valueView.setText(R.string.err_type);

        return view;
    }
}

package com.hf.resreader.resreader;

import android.content.Context;
import android.view.View;

/**
 * Created by Fan on 2015/8/20.
 * Interface for reader
 */
public interface IResReader {
    /**
     * Read the value according to the given pkg, type and key. This method should present the value in a view and return it.
     * In order to get better performance, there's a parameter convertView for reuse.
     * @param context context of the current activity
     * @param convertView view for reuse
     * @param pkg package name
     * @param type resource type
     * @param key resource key (name)
     * @return view with value
     */
    View read(Context context, View convertView, String pkg, String type, String key);

    /**
     * Resource ID of the view returned by {@link #read(Context, View, String, String, String)}.
     * It is usually used to consider whether the view could be reused.
     * @return resource id.
     */
    int getViewResId();
}

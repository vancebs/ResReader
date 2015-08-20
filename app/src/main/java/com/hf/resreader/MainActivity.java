package com.hf.resreader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "ResReader";

    private static final String TYPE_INTEGER = "integer";
    private static final String TYPE_BOOLEAN = "bool";
    private static final String TYPE_STRING = "string";

    private static final String[] TYPES = {
            TYPE_INTEGER,
            TYPE_BOOLEAN,
            TYPE_STRING
    };

    private static final String INTERNAL_PKG = "com.android.internal";
    private static final String[] NATIVE_PKGS = new String[]{
            "android",
            INTERNAL_PKG
    };

    private Spinner mType;
    private AutoCompleteTextView mPkg;
    private AutoCompleteTextView mKey;
    private TextView mValue;
    private TextView mResId;

    private int mValueTypeIndex = 0;
    private String mValueType;
    private String mValuePkg;
    private String mValueKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mType = (Spinner) findViewById(R.id.type);
        mPkg = (AutoCompleteTextView) findViewById(R.id.pkg);
        mKey = (AutoCompleteTextView) findViewById(R.id.key);
        mValue = (TextView) findViewById(R.id.value);
        mResId = (TextView) findViewById(R.id.resid);

        // get pkg auto complete
        setPkgAutoComplete();

        mPkg.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && updatePkgValue()) {
                    // key auto complete
                    setKeyAutoComplete();
                }
            }
        });

        mType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (updateTypeValue()) {
                    setKeyAutoComplete();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                if (updateTypeValue()) {
                    setKeyAutoComplete();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.read) {
            onReadClicked();
            return true;
        } else if (item.getItemId() == R.id.clear) {
            onClearClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onReadClicked() {
        updateTypeValue();
        updatePkgValue();
        updateKeyValue();

        Context ctxt = getPkgContext(mValuePkg);
        int resId = getResId(ctxt, mValuePkg, mValueType, mValueKey);
        String value = getValueOfResId(ctxt, mValueType, resId);
        mResId.setText(String.valueOf(resId));
        mValue.setText(value);
    }

    private void onClearClicked() {
        mValue.setText("");
        mResId.setText("");
    }

    private boolean isNativePkg(String pkg) {
        if (pkg == null || pkg.isEmpty()) {
            return true;
        }

        for (String p : NATIVE_PKGS) {
            if (p.equals(pkg)) {
                return true;
            }
        }

        return false;
    }

    private int getResId(Context context, String pkg, String type, String key) {
        if (isNativePkg(pkg)) {
            return getResId2(key, type, pkg);
        }

        return context.getResources().getIdentifier(key, type, pkg);
    }

    private int getResId2(String key, String type, String pkg) {
        final String INFO_FORMAT = "[pkg: %s, type: %s, key: %s]";

        int resId = -1;
        try {
            String clazzName = pkg + ".R$" + type;
            Class<?> clazz = Class.forName(clazzName);
            Field field = clazz.getField(key);
            resId = field.getInt(null);
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "getResId2()# " + String.format(INFO_FORMAT, pkg, type, key), e);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "getResId2()# " + String.format(INFO_FORMAT, pkg, type, key), e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "getResId2()# " + String.format(INFO_FORMAT, pkg, type, key), e);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "getResId2()# " + String.format(INFO_FORMAT, pkg, type, key), e);
        }

        return resId;
    }

    private Context getPkgContext(String pkg) {
        Context context;

        if (isNativePkg(pkg)) {
            context = this;
        } else {
            context = createContext(pkg);
            if (context == null) {
                context = this;
            }
        }

        return context;
    }

    private String getValueOfResId(Context context, String type, int resId) {
        if (resId > 0) {
            if (TYPE_INTEGER.equals(type)) {
                int iValue = context.getResources().getInteger(resId);
                return String.valueOf(iValue);
            } else if (TYPE_BOOLEAN.equals(type)) {
                boolean bValue = context.getResources().getBoolean(resId);
                return String.valueOf(bValue);
            } else if (TYPE_STRING.equals(type)) {
                return context.getResources().getString(resId);
            }
        }

        return getResources().getString(R.string.err_not_found);
    }

    private List<String> getPackageList() {
        List<String> list = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for (PackageInfo info : infos) {
            list.add(info.packageName);
        }

        if (!list.contains(INTERNAL_PKG)) {
            list.add(INTERNAL_PKG);
        }
        return list;
    }

    private Context createContext(String pkg) {
        try {
            return createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Create context failed! pkg: " + pkg, e);
            return null;
        }
    }

    private boolean updateTypeValue() {
        int index = mType.getSelectedItemPosition();
        if (index != mValueTypeIndex) {
            mValueTypeIndex = index;
            mValueType = TYPES[mValueTypeIndex];
            return true;
        } else {
            return false;
        }
    }

    private boolean updatePkgValue() {
        String value = mPkg.getText().toString().trim();
        if (value.equals(mValuePkg)) {
            return false;
        } else {
            mValuePkg = value;
            return true;
        }
    }

    private boolean updateKeyValue() {
        String value = mKey.getText().toString().trim();
        if (value.equals(mValueKey)) {
            return false;
        } else {
            mValueKey = value;
            return true;
        }
    }

    private void setPkgAutoComplete() {
        List<String> mPkgList = getPackageList();
        mPkg.setAdapter(new ArrayAdapter<>(this,
                R.layout.auto_complete_item, mPkgList));
    }

    private void setKeyAutoComplete() {
        if (mValuePkg == null || mValuePkg.isEmpty() || mValueType == null
                || mValueType.isEmpty()) {
            return;
        }

        List<String> keyList = getKeyList(mValueType, mValuePkg);
        mKey.setAdapter(new ArrayAdapter<>(this,
                R.layout.auto_complete_item, keyList));
    }

    private List<String> getKeyList(String type, String pkg) {
        if (isNativePkg(pkg)) {
            return getKeyListNative(type, pkg);
        } else {
            return new ArrayList<>();
        }
    }

    private List<String> getKeyListNative(String type, String pkg) {
        List<String> keyList = new ArrayList<>();
        String clazzName = pkg + ".R$" + type;

        try {
            Class<?> clazz = Class.forName(clazzName);
            Field[] fields = clazz.getFields();

            // get key list
            if (fields != null) {
                for (Field field : fields) {
                    keyList.add(field.getName());
                }
            }
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "getKeyList_Native()# class not found. [pkg: " + pkg + ", type: " + type + "]", e);
        }

        return keyList;
    }
}

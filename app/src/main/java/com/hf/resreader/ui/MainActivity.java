package com.hf.resreader.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.hf.resreader.R;
import com.hf.resreader.ResReaderManager;

public class MainActivity extends Activity {
    private static final String TAG = "ResReader";

    private static final String EXTRA_PKG = "extra-pkg";
    private static final String EXTRA_TYPE_INDEX = "extra-type-index";
    private static final String EXTRA_KEY = "extra-key";
    private static final String EXTRA_IS_DARK = "extra-is-dark";

    private String[] mTypes = null;

    private static final String INTERNAL_PKG = "com.android.internal";
    private static final String[] NATIVE_PKGS = new String[]{
            "android",
            INTERNAL_PKG
    };

    private Spinner mType;
    private AutoCompleteTextView mPkg;
    private AutoCompleteTextView mKey;

    private int mValueTypeIndex = -1;
    private String mValueType;
    private String mValuePkg;
    private String mValueKey;

    private ResReaderManager mResReaderManager;

    private ViewGroup mContainer;

    private MenuItem mThemeBtn;
    private boolean mIsDark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mType = (Spinner) findViewById(R.id.type);
        mPkg = (AutoCompleteTextView) findViewById(R.id.pkg);
        mKey = (AutoCompleteTextView) findViewById(R.id.key);

        // create reader manager
        mContainer = (ViewGroup) findViewById(R.id.value_container);
        mResReaderManager = new ResReaderManager(this, mContainer);

        // get pkg auto complete
        setPkgAutoComplete();

        // set key auto complete
        mPkg.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // key auto complete
                    setKeyAutoComplete();
                }
            }
        });
        mType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setKeyAutoComplete();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                setKeyAutoComplete();
            }
        });

        // read last saved values
        if (savedInstanceState != null) {
            mPkg.setText(savedInstanceState.getString(EXTRA_PKG, ""));
            mType.setSelection(savedInstanceState.getInt(EXTRA_TYPE_INDEX, 0));
            mKey.setText(savedInstanceState.getString(EXTRA_KEY, ""));
            mIsDark = savedInstanceState.getBoolean(EXTRA_IS_DARK, false);

            onReadClicked();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mThemeBtn = menu.findItem(R.id.theme);
        updateThemeButton();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read:
                onReadClicked();
                return true;
            case R.id.clear:
                onClearClicked();
                return true;
            case R.id.theme:
                onThemeClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(EXTRA_PKG, mValuePkg);
        outState.putInt(EXTRA_TYPE_INDEX, mValueTypeIndex);
        outState.putString(EXTRA_KEY, mValueKey);
        outState.putBoolean(EXTRA_IS_DARK, mIsDark);
    }

    private void onReadClicked() {
        updatePkgValue();
        updateTypeValue();
        updateKeyValue();

        mResReaderManager.read(mValuePkg, mValueType, mValueKey);
    }

    private void onClearClicked() {
        mResReaderManager.clear();
    }

    private void onThemeClicked() {
        mIsDark = !mIsDark;
        updateThemeButton();
    }

    private void updateThemeButton() {
        Resources res = getResources();
        String title = mIsDark ? res.getString(R.string.btn_theme_bright) : res.getString(R.string.btn_theme_dark);
        mThemeBtn.setTitle(title);
        mThemeBtn.setTitleCondensed(title);

        mContainer.setBackgroundResource(mIsDark ? R.color.background_color_dark : R.color.background_color_light);
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

    private boolean updateTypeValue() {
        int index = mType.getSelectedItemPosition();
        if (index != mValueTypeIndex) {
            mValueTypeIndex = index;
            mValueType = getTypes()[mValueTypeIndex];
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
        boolean updatePkg = updatePkgValue();
        boolean updateType = updateTypeValue();

        if (updatePkg || updateType) {
            if (mValuePkg == null || mValuePkg.isEmpty() || mValueType == null
                    || mValueType.isEmpty()) {
                return;
            }

            List<String> keyList = getKeyList(mValueType, mValuePkg);
            mKey.setAdapter(new ArrayAdapter<>(this,
                    R.layout.auto_complete_item, keyList));
        }
    }

    private List<String> getKeyList(String type, String pkg) {
        if (isNativePkg(pkg)) {
            return getKeyListNative(type, pkg);
        } else {
            // TODO
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

    private String[] getTypes() {
        if (mTypes == null) {
            mTypes = getResources().getStringArray(R.array.types);
        }

        return mTypes;
    }
}

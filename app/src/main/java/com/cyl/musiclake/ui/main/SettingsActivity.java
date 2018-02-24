package com.cyl.musiclake.ui.main;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.base.BaseActivity;
import com.cyl.musiclake.utils.DataClearmanager;
import com.cyl.musiclake.utils.PreferencesUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.utils.UpdateUtils;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new GeneralPreferenceFragment().newInstance()).commit();
    }

    @Override
    protected void listener() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private PreferenceScreen preference_about, preference_cache, preference_update;
        public SwitchPreference wifi_mode;

        public GeneralPreferenceFragment() {
        }

        public static GeneralPreferenceFragment newInstance() {

            Bundle args = new Bundle();

            GeneralPreferenceFragment fragment = new GeneralPreferenceFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            preference_about = (PreferenceScreen) findPreference("key_about");
            preference_update = (PreferenceScreen) findPreference("key_update");
            preference_cache = (PreferenceScreen) findPreference("key_cache");

            wifi_mode = (SwitchPreference) findPreference("wifi_mode");

            new Handler().post(() -> {
                try {
                    String size = DataClearmanager.getTotalCacheSize(getActivity());
                    preference_cache.setSummary(size);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            preference_about.setOnPreferenceClickListener(this);
            preference_update.setOnPreferenceClickListener(this);
            preference_cache.setOnPreferenceClickListener(this);

            wifi_mode.setChecked(PreferencesUtils.getWifiMode());

            wifi_mode.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.e("sss", newValue.toString());
                boolean wifiMode = (boolean) newValue;
                wifi_mode.setChecked(wifiMode);
                PreferencesUtils.saveWifiMode(wifiMode);
                return false;
            });
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "key_about":
                    try {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("关于")
                                .setMessage("湖科音乐湖\n当前版本号" + UpdateUtils.getVersion())
                                .show();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "key_cache":
                    new Handler().post(() -> {
                        try {
                            //清除缓存
                            DataClearmanager.cleanApplicationData(getActivity());
                            ToastUtils.show(getActivity(), "清除成功");
                            String size = DataClearmanager.getTotalCacheSize(getActivity());
                            preference_cache.setSummary(size);
                        } catch (Exception e) {
                            //清除失败
                            ToastUtils.show(getActivity(), "清除失败");
                            e.printStackTrace();
                        }
                    });
                    break;
                case "key_update":
                    UpdateUtils.checkUpdate(getActivity());
                    break;

            }
            return false;
        }
    }

}

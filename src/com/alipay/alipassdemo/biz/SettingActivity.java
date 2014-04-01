package com.alipay.alipassdemo.biz;

import java.io.File;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.alipay.alipassdemo.R;

public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener {
    private EditTextPreference mEtpAlipassDir;
    private EditTextPreference mEtpDownload;
    //private SwitchPreference apiModeArea;

    private SharedPreferences  mSharedPreferences;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSharedPreferences = getSharedPreferences(Constant.SAVE_XML, MODE_PRIVATE);
        mEtpAlipassDir = (EditTextPreference) findPreference("alipass_dir");
        mEtpDownload = (EditTextPreference) findPreference("download_path");

        //apiModeArea = (SwitchPreference) findPreference("api_mode");

        mEtpAlipassDir.setOnPreferenceChangeListener(this);
        mEtpDownload.setOnPreferenceChangeListener(this);

        //apiModeArea.setOnPreferenceChangeListener(this);

        //apiModeArea.setChecked(true);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mEtpAlipassDir.setSummary(Environment.getExternalStorageDirectory()
                                  + File.separator
                                  + mSharedPreferences.getString(Constant.ALIPASSDEMO_DIR,
                                      Constant.ALIPASSDEMO_DIR_DEF));
        mEtpAlipassDir.setText(mSharedPreferences.getString(Constant.ALIPASSDEMO_DIR,
            Constant.ALIPASSDEMO_DIR_DEF));
        mEtpDownload.setSummary(mSharedPreferences.getString(Constant.DOWNLOAD_PATH,
            Constant.DOWNLOAD_PATH_DEF));
        mEtpDownload.setText(mSharedPreferences.getString(Constant.DOWNLOAD_PATH,
            Constant.DOWNLOAD_PATH_DEF));

        //apiModeArea.setChecked(mSharedPreferences.getBoolean(Constant.API_MODE,true));
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Editor edit = mSharedPreferences.edit();
        edit.putString(Constant.ALIPASSDEMO_DIR, mEtpAlipassDir.getText());
        edit.putString(Constant.DOWNLOAD_PATH, mEtpDownload.getText());
        //edit.putBoolean(Constant.API_MODE, apiModeArea.isChecked());
        edit.commit();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO Auto-generated method stub
        if (preference == mEtpAlipassDir) {
            mEtpAlipassDir.setSummary(Environment.getExternalStorageDirectory() + File.separator
                                      + String.valueOf(newValue));
        } else if (preference == mEtpDownload) {
            mEtpDownload.setSummary(String.valueOf(newValue));

        } /*else if (preference == apiModeArea) {
          if (!Boolean.valueOf(newValue + "")) {
          	apiModeArea.setChecked(false);
          } else {
          	apiModeArea.setChecked(true);
          }
          }*/
        return true;
    }

}

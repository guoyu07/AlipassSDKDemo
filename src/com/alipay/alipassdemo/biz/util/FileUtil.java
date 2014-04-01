package com.alipay.alipassdemo.biz.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alipay.alipass.sdk.enums.PassType;
import com.alipay.alipass.sdk.utils.FileUtils;
import com.alipay.alipassdemo.R;
import com.alipay.alipassdemo.biz.AlipassBean;
import com.alipay.alipassdemo.biz.Constant;

public class FileUtil {
    private static final List<String> passTypeStr = new ArrayList<String>();
    static {
        PassType[] passtype = PassType.values();
        for (PassType pt : passtype) {
            passTypeStr.add(pt.name());
        }
    }

    /**
     * 获取sdcard下自定义文件夹下的alipass文件
     * 
     * @param context
     * @return
     */
    public static List<AlipassBean> scanAlipass(Context context) {
        List<AlipassBean> list = new ArrayList<AlipassBean>();
        if (isSdcardOn(context)) {
            File dir = getAlipassDir(context);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    if (!file.isDirectory() && file.getName().endsWith(".alipass")) {
                        for (String passType : passTypeStr) {
                            if (file.getName().contains(passType)) {
                                list.add(new AlipassBean(file.getName(), file.getPath(), PassType
                                    .valueOf(passType)));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private static File getAlipassDir(Context context) {
        SharedPreferences sp = context
            .getSharedPreferences(Constant.SAVE_XML, Context.MODE_PRIVATE);
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator
                            + sp.getString(Constant.ALIPASSDEMO_DIR, Constant.ALIPASSDEMO_DIR_DEF));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private static boolean isSdcardOn(final Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.sdcard_off, Toast.LENGTH_LONG).show();
                }
            });
            return false;
        }
    }

    /**
     * 获取assets文件夹下的alipass文件，这些文件仅限于本地测试使用
     * 
     * @param context
     * @return
     */
    public static List<AlipassBean> getAssetsAlipass(Context context) {
        List<AlipassBean> list = new ArrayList<AlipassBean>();
        try {
            String[] names = context.getAssets().list("pass");
            for (String name : names) {
                for (String passType : passTypeStr) {
                    if (name.contains(passType)) {
                        list.add(new AlipassBean(name, "pass/" + name, PassType.valueOf(passType)));
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 保存alipass文件到sdcard
     * 
     * @param context
     * @param passContent
     */
    public static void saveAlipassToSdcard(Context context, String passContent, PassType passType) {
        if (isSdcardOn(context) && passContent != null && passContent.trim().length() > 0) {
            try {
                File savaDir = getAlipassDir(context);
                String filePath = savaDir.getPath() + File.separator + passType
                                  + System.currentTimeMillis() + ".alipass";

                Log.i("alipass file path", filePath);

                FileUtils.saveAsFile(filePath, passContent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFile(Context context, String passPath) {
        if (isSdcardOn(context) && passPath != null && passPath.trim().length() > 0) {
            File file = new File(passPath);
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

}

package com.alipay.alipassdemo.core;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alipay.alipass.sdk.enums.PassType;
import com.alipay.alipass.sdk.enums.Result;
import com.alipay.alipass.sdk.jsonmodel.BaseModel;
import com.alipay.alipass.sdk.utils.FileUtils;
import com.alipay.alipassdemo.R;
import com.alipay.alipassdemo.biz.AlipassBean;
import com.alipay.alipassdemo.biz.Constant;
import com.alipay.alipassdemo.biz.SettingActivity;
import com.alipay.alipassdemo.biz.adapter.AlipassListAdapter;
import com.alipay.alipassdemo.biz.util.FileUtil;
import com.alipay.alipassdemo.core.service.GenerateAlipassService;
import com.alipay.alipassdemo.core.service.JumpToAlipayService;

public class MainActivity extends Activity {
    private static final CharSequence[] ALIPASS_TYPE       = new CharSequence[] { "boardingPass",
            "coupon", "eventTicket"                       };
    private static final PassType[]     ALIPASS_TYPE_VALUE = new PassType[] {
            PassType.boardingPass, PassType.coupon, PassType.eventTicket };

    /**
     * .alipass文件路径列表
     */
    private List<AlipassBean>           mPassPathList      = new ArrayList<AlipassBean>();

    private ListView                    mLvAlipass;

    private Button                      mBtnScanAlipass;

    private Button                      mBtnSetting;

    private Button                      mBtnGenerateAlipass;

    private AlipassListAdapter          mAlipassListAdapter;

    private boolean                     isScanning         = false;

    private DownloadManager             manager;

    private DownloadCompleteReceiver    receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        addListeners();

        init();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        scanAlipass();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void findViews() {
        mLvAlipass = (ListView) findViewById(R.id.lv_alipass_list);
        mBtnScanAlipass = (Button) findViewById(R.id.btn_scanalipass);
        mBtnSetting = (Button) findViewById(R.id.btn_setting);
        mBtnGenerateAlipass = (Button) findViewById(R.id.btn_generate_alipass);
    }

    private void addListeners() {
        mLvAlipass.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                jumpToAlipayClient(pos, true);
            }

        });
        mLvAlipass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView,
                                           final int pos, long paramLong) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                    .setMessage(R.string.delete_alipass_message)
                    .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (FileUtil.deleteFile(MainActivity.this,
                                    mPassPathList.get(pos).getPath())) {
                                    scanAlipass();
                                }
                            }
                        }).setNegativeButton(android.R.string.cancel, null);
                builder.show();
                return true;
            }
        });
        mBtnScanAlipass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                scanAlipass();
            }
        });
        mBtnSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        mBtnGenerateAlipass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 生成alipass文件
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(ALIPASS_TYPE, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, final int paramInt) {
                        // TODO Auto-generated method stub
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                GenerateAlipassService.generateAlipass(MainActivity.this,
                                    ALIPASS_TYPE_VALUE[paramInt]);

                                jumpToAlipayClient(0, false);

                            }
                        }).start();
                    }
                });
                builder.create().show();
                //
            }
        });
    }

    private void init() {
        manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        mAlipassListAdapter = new AlipassListAdapter(this, mPassPathList);
        mLvAlipass.setAdapter(mAlipassListAdapter);
    }

    private void scanAlipass() {
        if (!isScanning) {
            isScanning = true;
            mPassPathList.clear();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    final List<AlipassBean> assetList = FileUtil
                        .getAssetsAlipass(MainActivity.this);
                    final List<AlipassBean> sdcardList = FileUtil.scanAlipass(MainActivity.this);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mPassPathList.addAll(assetList);
                            mPassPathList.addAll(sdcardList);
                            mAlipassListAdapter.notifyDataSetChanged();
                            isScanning = false;
                        }
                    });
                }
            }).start();
        }
    }

    @SuppressWarnings("deprecation")
    private void downloadApk() {
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(getSharedPreferences(
            Constant.SAVE_XML, MODE_PRIVATE).getString(Constant.DOWNLOAD_PATH,
            Constant.DOWNLOAD_PATH_DEF)));
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                                    | DownloadManager.Request.NETWORK_WIFI);
        down.setShowRunningNotification(true);
        down.setVisibleInDownloadsUi(true);
        down.setDestinationInExternalFilesDir(MainActivity.this, null, "alipay_test.apk");
        manager.enqueue(down);
    }

    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                String downUri = "";
                Cursor c = manager.query(new DownloadManager.Query());
                if (c.getCount() > 0) {
                    c.moveToLast();
                    downUri = c.getString(c.getColumnIndex((DownloadManager.COLUMN_LOCAL_URI)));
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse(downUri), "application/vnd.android.package-archive");
                startActivity(i);
            }
        }
    }

    /**
     * 跳转到支付宝钱包
     * @param pos               列表索引
     * @param isFromScanList    是否来自刷新列表
     */
    private void jumpToAlipayClient(int pos, boolean isFromScanList) {
        // 跳转到支付宝钱包
        BaseModel bm = null;
        if (isFromScanList) {
            bm = JumpToAlipayService.jumpToAlipay(MainActivity.this, mPassPathList.get(pos)
                .getPath(), mPassPathList.get(pos).getPassType());
        } else {
            final List<AlipassBean> sdcardList = FileUtil.scanAlipass(MainActivity.this);
            if (sdcardList == null || sdcardList.size() == 0) {
                return;
            }
            bm = JumpToAlipayService.jumpToAlipay(MainActivity.this,
                sdcardList.get(sdcardList.size() - 1).getPath(),
                sdcardList.get(sdcardList.size() - 1).getPassType());
        }
        if (bm.getResult() == Result.ALIPAY_APP_NEED_UPGRADE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder
                        .setMessage(R.string.download_dialog_message)
                        .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    downloadApk();
                                }
                            })
                        .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    scanAlipass();
                                }
                            });
                    builder.show();
                }
            });
        }

    }

}

package com.alipay.alipassdemo.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ReceiveIntentActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if(intent.getData()!=null){
			Toast.makeText(this, intent.getData().getQueryParameter("statusMemo"), Toast.LENGTH_SHORT).show();
		}
		finish();
	}

}

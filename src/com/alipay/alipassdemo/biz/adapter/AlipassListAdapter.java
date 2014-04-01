package com.alipay.alipassdemo.biz.adapter;

import java.util.List;

import com.alipay.alipassdemo.biz.AlipassBean;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlipassListAdapter extends BaseAdapter {
	private List<AlipassBean> mPassPath;
	private Context mContext;
	
	public AlipassListAdapter(Context context, List<AlipassBean> passPath){
		mContext = context;
		mPassPath = passPath;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPassPath.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = null;
		if(convertView == null){
			textView = new TextView(mContext);
			textView.setTextColor(Color.BLACK);
			textView.setPadding(10, 25, 10, 25);
			textView.setTextSize(15);
		}
		else{
			textView = (TextView) convertView;
		}
		textView.setText(mPassPath.get(position).getName());
		return textView;
	}

}

package com.alipay.alipassdemo.core.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

public class PassContentProvider extends ContentProvider{

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		String path = uri.getPath();
		if(path == null){
			return null;
		}
		path = path.replaceFirst("/", "");
		InputStream is = null;
		FileOutputStream fos = null;
		String pathName = null;
		int index = path.lastIndexOf("/");
		pathName = path.substring(index+1, path.length());
		
		try {
			if(path.startsWith(Environment.getExternalStorageDirectory().toString())){
				is = new FileInputStream(path);
			}
			else{
				is = getContext().getAssets().open(path);
			}
			if(is != null){
				String cachePath = getContext().getCacheDir() + File.separator + pathName;
				fos = new FileOutputStream(cachePath);
				byte[] buffer = new byte[1024];
				int length;
				while((length = is.read(buffer)) != -1)
					fos.write(buffer,0,length);
				return ParcelFileDescriptor.open(new File(cachePath), ParcelFileDescriptor.MODE_READ_ONLY);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is !=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}

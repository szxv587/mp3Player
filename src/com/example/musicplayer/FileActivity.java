package com.example.musicplayer;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FileActivity extends Activity {

	private ListView lv_fileActivity;
	private ArrayList<MyFile> myFile_list, myFile_list2;
	private ArrayList<MyFile> dir_list = new ArrayList<MyFile>();
	private FileAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_layout);
		lv_fileActivity = (ListView) findViewById(R.id.lv_fileActivity);
		File file = Environment.getExternalStorageDirectory();
		myFile_list = getMyFilelist(file);
		dir_list.add(new MyFile(false, file));
		adapter = new FileAdapter();
		adapter.setData(myFile_list);
		lv_fileActivity.setAdapter(adapter);
		lv_fileActivity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MyFile file = (MyFile) adapter.getItem(arg2);
				if (file.file.isDirectory()) {
					dir_list.add(file);
					myFile_list2 = getMyFilelist(file.file);
					adapter.setData(myFile_list2);
				} else {
					if (file.file.getAbsolutePath().toString().endsWith("mp3")) {
						file.ischecked = !file.ischecked;
						adapter.notifyDataSetChanged();
					}
				}

			}
		});

	}

	/**
	 * 将SD卡中的目录和文件添加到集合中
	 * @param 路径
	 * @return 存储了目录和文件的集合
	 */
	public ArrayList<MyFile> getMyFilelist(File file) {
		myFile_list = new ArrayList<MyFile>();
		File[] files = file.listFiles();
		for (File f : files) {
			Log.e("TAG", f.getAbsolutePath());
			myFile_list.add(new MyFile(false, f));
		}
		return myFile_list;
	}

	String paths = "";

	public void sure(View v) {
		
		Intent data = new Intent();
		for (MyFile mf : adapter.MyFile_list) {
			if (mf.ischecked) {
				if(mf.file.getAbsolutePath().toString().equals("")){
					break;
				}
				paths = paths + mf.file.getAbsolutePath() + "@";
			}
		}

		data.putExtra("paths", paths);
		setResult(2000, data);
		finish();
	}

	public void backToPreLevel(View v) {
		if(dir_list.size()==1){
			return;
		}
		ArrayList<MyFile> myFilelist = getMyFilelist(dir_list.get(dir_list.size()-2).file);
		adapter.setData(myFilelist);
		adapter.notifyDataSetChanged();
		dir_list.remove(dir_list.size()-1);
	}
	ArrayList<MyFile> a =new ArrayList<MyFile>();
	public void search(View v) {
		File file = Environment.getExternalStorageDirectory();
		dir_list.add(new MyFile(false, file));
		extracted(file);
		adapter.setData(a);
		lv_fileActivity.setAdapter(adapter);
	}

	public  void extracted(File file) {
		
		File[] files = file.listFiles();
		Log.e("TAG", "files=" + files);
		for (File f : files) {
			Log.e("TAG", f.getAbsolutePath());
			if(f.isDirectory()){
				if(f.getName().equals("MIUI")||f.getName().equals("music")||f.getName().equals("mp3")){
				  extracted(f);
				}
				
			} else {
				Log.e("路径", f.getAbsolutePath().toString());
				if (f.getAbsolutePath().toString().endsWith("mp3")) {
					a.add(new MyFile(false, f));
				}
			}
		}
		
	}

	public void chooseAll(View v) {
		int index = adapter.getCount();
		for (int i = 0; i < index; i++) {
			MyFile file = (MyFile) adapter.getItem(i);
			if (file.file.isDirectory()) {
				continue;
			} else if (file.file.getAbsolutePath().toString().endsWith("mp3")) {

				file.ischecked = true;
				adapter.notifyDataSetChanged();
			}
		}

	}

	public void cancelAll(View v) {
		int index = adapter.getCount();
		for (int i = 0; i < index; i++) {
			MyFile file = (MyFile) adapter.getItem(i);
			if (file.file.isDirectory()) {
				continue;
			} else if (file.file.getAbsolutePath().toString().endsWith("mp3")) {

				file.ischecked = false;
				adapter.notifyDataSetChanged();
			}
		}
	}

	public void fanxuan(View v) {
		int index = adapter.getCount();
		for (int i = 0; i < index; i++) {
			MyFile file = (MyFile) adapter.getItem(i);

			if (file.file.isDirectory()) {
				continue;
			} else if (file.file.getAbsolutePath().toString().endsWith("mp3")) {
				if (file.ischecked) {
					file.ischecked = false;
				} else {
					file.ischecked = true;
				}
				adapter.notifyDataSetChanged();
			}

		}
	}

}

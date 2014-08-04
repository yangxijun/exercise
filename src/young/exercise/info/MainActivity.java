package young.exercise.info;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	
	public static final String BROADCAST_CHANGE_NUMEBER= "com.exercise.info.broadcast.change_number";
	public static final String INFO_ID= "com.exercise.info.id";
	public static final String INFO_NAME= "com.exercise.info.name";
	public static final String INFO_AGE= "com.exercise.info.age";
	public static final String INFO_SEX= "com.exercise.info.sex";
	public static final String INFO_NUMBER= "com.exercise.info.number";
	private SimpleCursorAdapter mAdapter = null;
	private ContentResolver mContentResolver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = getLayoutInflater();
		final TextView footerView = (TextView) inflater.inflate(
				R.layout.footer_view, null);

		mContentResolver = getContentResolver();

		// 判断数据库里面是否存有数据
		if (dataExisted()) {
			initAdapter();
			setListAdapter(mAdapter);
			footerView.setVisibility(View.GONE);
		} else {
			getListView().addFooterView(footerView);
			getListView().setAdapter(mAdapter);
		}
		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						initData();
					}
				});
//				new initDataTask().execute();
				initAdapter();
				setListAdapter(mAdapter);
				footerView.setVisibility(View.GONE);
			}
		});

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showInfoDialog(position);
			}

		});

		// 接收广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BROADCAST_CHANGE_NUMEBER);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String newNumberId = intent.getStringExtra(PersonalDetail.NEW_NUMBER_ID);
			String newNumber = intent.getStringExtra(PersonalDetail.NEW_NUMBER);
			updateContentProvider(newNumberId, newNumber);
			mAdapter.notifyDataSetChanged();
		}
	};

	private boolean dataExisted() {
		Cursor cursor = mContentResolver.query(Profile.CONTENT_URI, null, null,
				null, null);
		
		boolean judge = false;
		try {
			if (null != cursor) {
				judge = (cursor.getCount() == 0);
			}
		} catch (Exception e) {
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
		return !judge;
	}

	private void showInfoDialog(final int pos) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.dialog_info)
				.setTitle(R.string.dialog_modify)
				.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						deleteInContentProvider(pos);

						mAdapter.notifyDataSetChanged();

						dialog.dismiss();

					}

				})
				.setNegativeButton(R.string.dialog_detail,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								setProile(pos);
								dialog.dismiss();

							}

						}).create().show();
	}

	protected void updateContentProvider(String newNumberId, String newNumber) {

		Uri uri = Uri.withAppendedPath(Profile.CONTENT_URI, newNumberId + "");

		Cursor cursor = mContentResolver.query(uri, null, Profile.ID + "="
				+ newNumberId, null, null);
		try {
			if (null != cursor && cursor.moveToFirst()) {
				ContentValues updateValues = new ContentValues();
				updateValues.put(Profile.NUMBER, newNumber);
				Uri updateUri = ContentUris.withAppendedId(Profile.CONTENT_URI,
						Long.parseLong(newNumberId));
				getContentResolver()
						.update(updateUri, updateValues, null, null);
			}
		} catch (Exception e) {
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}
	}

	private void setProile(int pos) {

		Intent setDetailIntent = new Intent(MainActivity.this,
				PersonalDetail.class);
		int id = (int) mAdapter.getItemId(pos);
		String name = null;
		String sex = null;
		String age = null;
		String number = null;
		Uri uri = Uri.withAppendedPath(Profile.CONTENT_URI, id + "");
		Cursor cursor = mContentResolver.query(uri, null, null, null, null);
		try {
			if (null != cursor && cursor.moveToFirst()) {

				name = cursor.getString(cursor.getColumnIndex(Profile.NAME));
				sex = cursor.getString(cursor.getColumnIndex(Profile.SEX));
				age = cursor.getString(cursor.getColumnIndex(Profile.AGE));
				number = cursor
						.getString(cursor.getColumnIndex(Profile.NUMBER));
			}
		} catch (Exception e) {
		} finally {
			if (null != cursor) {
				cursor.close();
			}
		}

		setDetailIntent.putExtra(INFO_ID, id);
		setDetailIntent.putExtra(INFO_NAME, name);
		setDetailIntent.putExtra(INFO_SEX, sex);
		setDetailIntent.putExtra(INFO_AGE, age);
		setDetailIntent.putExtra(INFO_NUMBER, number);

		startActivity(setDetailIntent);
	}

	private void deleteInContentProvider(int pos) {

		long delId = mAdapter.getItemId(pos);
		getContentResolver().delete(Profile.CONTENT_URI, "_id=" + delId, null);

	}

	private void initAdapter() {

		Cursor mCursor = mContentResolver.query(Profile.CONTENT_URI,
				new String[] { Profile.ID, Profile.NAME, Profile.NUMBER },
				null, null, null);

		startManagingCursor(mCursor);
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_item, mCursor,
				new String[] { Profile.ID, Profile.NAME, Profile.NUMBER },
				new int[] { R.id.profile_id, R.id.profile_name,
						R.id.profile_number });
	}

	class initDataTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected String doInBackground(Void... params) {
			initData();
			return null;
		}

	}

	private void initData() {

		ContentValues mValues = new ContentValues();
		int dataNum = 30;
		for (int i = 1; i <= dataNum; i++) {

			mValues.put(Profile.ID, 10000 + i);
			mValues.put(Profile.NAME, "NO." + i);
			mValues.put(Profile.SEX, "man");
			mValues.put(Profile.AGE, 20 + i);
			mValues.put(Profile.NUMBER, 13380010 + i);
			mValues.put(Profile.INTRODUCTION, "Hello");

			mContentResolver.insert(Profile.CONTENT_URI, mValues);
		}
	}

}

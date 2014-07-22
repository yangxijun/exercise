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
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private SimpleCursorAdapter mAdapter = null;
	private ContentResolver mContentResolver = null;
	private ContentValues mValues = new ContentValues();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = getLayoutInflater();
		final TextView footerView = (TextView) inflater.inflate(
				R.layout.footer_view, null);

		mContentResolver = getContentResolver();

		if (isExisted()) {
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
				initData();
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

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.exercise.info.change_number");
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

			String newNumber = intent.getStringExtra("new_number");
			String newNumberId = intent.getStringExtra("new_number_id");
			updateContentProvider(newNumberId, newNumber);
			mAdapter.notifyDataSetChanged();
		}
	};

	private boolean isExisted() {
		Cursor cursor = mContentResolver.query(Profile.CONTENT_URI, null, null,
				null, null);
		boolean judge = (cursor.getCount() == 0);
		cursor.close();
		return !judge;
	}

	private void showInfoDialog(final int pos) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("员工信息")
				.setTitle("修改")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						deleteInContentProvider(pos);

						mAdapter.notifyDataSetChanged();

						dialog.dismiss();

					}

				})
				.setNegativeButton("个人详情",
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

		Cursor mCursor = mContentResolver.query(uri, null, Profile.ID + "="
				+ newNumberId, null, null);
		if (mCursor.moveToFirst()) {
			ContentValues updateValues = new ContentValues();
			updateValues.put(Profile.NUMBER, newNumber);
			Uri updateUri = ContentUris.withAppendedId(Profile.CONTENT_URI,
					Long.parseLong(newNumberId));
			getContentResolver().update(updateUri, updateValues, null, null);
		}
		mCursor.close();
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
		Cursor mCursor = mContentResolver.query(uri, null, null, null, null);

		if (mCursor.moveToFirst()) {
			name = mCursor.getString(mCursor.getColumnIndex(Profile.NAME));
			sex = mCursor.getString(mCursor.getColumnIndex(Profile.SEX));
			age = mCursor.getString(mCursor.getColumnIndex(Profile.AGE));
			number = mCursor.getString(mCursor.getColumnIndex(Profile.NUMBER));
		}
		mCursor.close();

		setDetailIntent.putExtra("id", id);
		setDetailIntent.putExtra("name", name);
		setDetailIntent.putExtra("sex", sex);
		setDetailIntent.putExtra("age", age);
		setDetailIntent.putExtra("number", number);

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
						R.id.profile_number }, 1);

	}

	private void initData() {

		int dataNum = 10;
		for (int i = 1; i <= dataNum; i++) {

			mValues.put(Profile.ID, 10000 + i);
			mValues.put(Profile.NAME, "NO." + i);
			mValues.put(Profile.SEX, "man");
			mValues.put(Profile.AGE, 20 + i);
			mValues.put(Profile.NUMBER, 13380010 + i);

			mContentResolver.insert(Profile.CONTENT_URI, mValues);
		}
	}

}

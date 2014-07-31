package young.exercise.info;

import android.R.integer;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;

public class MyProvider extends ContentProvider {

	DBHelper mDbHelper = null;
	SQLiteDatabase mDb = null;

	private static final UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher
				.addURI(Profile.AUTOHORITY, Profile.TABLE_NAME, Profile.ITEM);
		sUriMatcher.addURI(Profile.AUTOHORITY, Profile.TABLE_NAME + "/#",
				Profile.ITEM_ID);
	}

	@Override
	public boolean onCreate() {

		mDbHelper = new DBHelper(this.getContext());
		try {
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Cursor cursor = null;
		switch (sUriMatcher.match(uri)) {
		case Profile.ITEM:
			cursor = mDb.query(Profile.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		case Profile.ITEM_ID:
			cursor = mDb.query(Profile.TABLE_NAME, projection, Profile.ID + "="
					+ uri.getLastPathSegment(), selectionArgs, null, null,
					sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {

		switch (sUriMatcher.match(uri)) {
		case Profile.ITEM:
			return Profile.CONTENT_TYPE;
		case Profile.ITEM_ID:
			return Profile.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknow URI" + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		long rowId;
		if (sUriMatcher.match(uri) != Profile.ITEM) {
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		rowId = mDb.insert(Profile.TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris
					.withAppendedId(Profile.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		return uri;

		// throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		try {
			mDb = mDbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		int count;
		switch (sUriMatcher.match(uri)) {
		case Profile.ITEM:
			count = mDb.delete(Profile.TABLE_NAME, selection, selectionArgs);
			break;
		case Profile.ITEM_ID:
			String profileId = uri.getPathSegments().get(1);
			count = mDb.delete(Profile.TABLE_NAME, Profile.ID
					+ "="
					+ profileId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		try {
			mDb = mDbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		int count = 0;
		switch (sUriMatcher.match(uri)) {
		case Profile.ITEM:
			count = mDb.update(Profile.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case Profile.ITEM_ID:
			String profileId = uri.getPathSegments().get(1);
			count = mDb.update(Profile.TABLE_NAME, values, Profile.ID
					+ "="
					+ profileId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			break;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}

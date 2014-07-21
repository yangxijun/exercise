package young.exercise.info;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "profile.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) throws SQLException {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Profile.TABLE_NAME + "("
				+ Profile.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ Profile.NAME + " TEXT NOT NULL," + Profile.SEX
				+ " TEXT NOT NULL," + Profile.AGE + " TEXT NOT NULL,"
				+ Profile.NUMBER + " TEXT NOT NULL," + Profile.INTRODUCTION
				+ " TEST NOT NULL);");
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
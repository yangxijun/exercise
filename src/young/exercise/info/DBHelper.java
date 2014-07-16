package young.exercise.info;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	
	public DBHelper(Context context) {
		super(context, Profile.TABLE_NAME, null, Profile.VERSION);
		// TODO Auto-generated constructor stub
	}



	private static final String DATABASE_NAME = "profile.db";
	private static final int DATABASE_VERSION = 1;
	
	@Override
	public void onCreate(SQLiteDatabase db) throws SQLException{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Profile.TABLE_NAME + "(" +
	Profile.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
	Profile.NAME + " TEXT NOT NULL," +
	Profile.SEX + " TEXT NOT NULL," +
	Profile.AGE + " TEXT NOT NULL," +
	Profile.NUMBER + " TEXT NOT NULL," +
	Profile.INTRODUCTION + " TEST NOT NULL);" );
	}
	
	public void add(String name, String sex, int age, String number, String introduction){
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Profile.NAME, name);
		values.put(Profile.SEX, sex);
		values.put(Profile.AGE, age);
		values.put(Profile.NUMBER, number);
		values.put(Profile.INTRODUCTION, introduction);
		
	}
	


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
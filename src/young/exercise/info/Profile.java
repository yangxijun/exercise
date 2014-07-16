package young.exercise.info;

import android.R.integer;
import android.net.Uri;

public class Profile {
	
	public static final String TABLE_NAME = "profile";
	public static final int VERSION = 1;
	
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String SEX = "sex";
	public static final String AGE = "age";
	public static final String NUMBER = "number";
	public static final String INTRODUCTION = "introduction";
	
	public static final String AUTOHORITY = "young.exercise.provider";
	public static final int ITEM = 1;
	public static final int ITEM_ID = 2;
	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.exercise.profile";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.exercise.profile";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY + "/profile");
}

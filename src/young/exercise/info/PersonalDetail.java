package young.exercise.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PersonalDetail extends Activity {
	
	public static final String NEW_NUMBER_ID = "new_number_id";
	public static final String NEW_NUMBER = "new_number";
	public static final String INTRO1 = "com.exercise.info.intro1";
	
	
	private TextView mIdTextView;
	private TextView mNameTextView;
	private TextView mAgeTextView;
	private TextView mSexTextView;
	private TextView mOldNumberTextView;
	private EditText mNewNumberEditText;
	private Button mSubmitButton;
	private ToggleButton mIntroduction;
	private final Intent mIntent = new Intent(
			"android.intent.action.INTRO_MUSIC");

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.detail);
		mIdTextView = (TextView) findViewById(R.id.detail_id);
		mNameTextView = (TextView) findViewById(R.id.detail_name);
		mAgeTextView = (TextView) findViewById(R.id.detail_age);
		mSexTextView = (TextView) findViewById(R.id.detail_sex);
		mOldNumberTextView = (TextView) findViewById(R.id.detail_old_number);
		mNewNumberEditText = (EditText) findViewById(R.id.detail_new_number);
		mSubmitButton = (Button) findViewById(R.id.submit);
		mIntroduction = (ToggleButton) findViewById(R.id.detail_introduction);

		final int changedId = getIntent().getIntExtra(MainActivity.INFO_ID, 0);
		String changedName = getIntent().getStringExtra(MainActivity.INFO_NAME);
		String changedSex = getIntent().getStringExtra(MainActivity.INFO_SEX);
		String changedAge = getIntent().getStringExtra(MainActivity.INFO_AGE);
		String changedNumber = getIntent().getStringExtra(MainActivity.INFO_NUMBER);

		mIdTextView.setText(changedId + "");
		mNameTextView.setText(changedName);
		mSexTextView.setText(changedSex);
		mAgeTextView.setText(changedAge);
		mOldNumberTextView.setText(changedNumber);

		mSubmitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String newNumber = mNewNumberEditText.getText().toString();
				sendNewNumber(changedId + "", newNumber);
				finish();
			}
		});

		mIntroduction.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mIntent.putExtra(INTRO1,
							R.raw.introduction);
					startService(mIntent);
					isChecked = !isChecked;
				} else {
					Intent pauseMusicIntent = new Intent(
							MusicPlayerService.BROADCAST_PAUSEMUSIC);
					sendBroadcast(pauseMusicIntent);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		stopService(mIntent);
		super.onDestroy();
	}

	private void sendNewNumber(String changedId, String newNumber) {

		Intent intent = new Intent();
		intent.setAction(MainActivity.BROADCAST_CHANGE_NUMEBER);
		intent.putExtra(NEW_NUMBER_ID, changedId);
		intent.putExtra(NEW_NUMBER, newNumber);
		sendBroadcast(intent);
		Toast.makeText(this, "Send the broadcast!", Toast.LENGTH_LONG).show();
	}

}

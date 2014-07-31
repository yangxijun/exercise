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

		final int changedId = getIntent().getIntExtra("id", 0);
		String changedName = getIntent().getStringExtra("name");
		String changedSex = getIntent().getStringExtra("sex");
		String changedAge = getIntent().getStringExtra("age");
		String changedNumber = getIntent().getStringExtra("number");

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
					mIntent.putExtra("com.exercise.info.intro1",
							R.raw.introduction);
					startService(mIntent);
					isChecked = !isChecked;
				} else {
					Intent pauseMusicIntent = new Intent(
							"com.exercise.info.broadcast.pausemusic");
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
		intent.setAction("com.exercise.info.change_number");
		intent.putExtra("new_number_id", changedId);
		intent.putExtra("new_number", newNumber);
		sendBroadcast(intent);
		Toast.makeText(this, "Send the broadcast!", Toast.LENGTH_LONG).show();
	}

}

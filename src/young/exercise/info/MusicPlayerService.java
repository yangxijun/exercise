package young.exercise.info;

import android.R.integer;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class MusicPlayerService extends Service {

	private MediaPlayer mPlayer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPlayer.release();
	}
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent("android.intent.action.INTRO_MUSIC");
		int introId = intent.getIntExtra("com.exercise.info.intro1", R.raw.introduction);
		mPlayer = MediaPlayer.create(this, introId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		mPlayer.start();
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				try {
					mp.start();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		});
		return super.onStartCommand(intent, flags, startId);
	}
	
	

}

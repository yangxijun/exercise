package young.exercise.info;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
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
		mPlayer.stop();
	}
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		mPlayer = MediaPlayer.create(this, R.raw.introduction);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		mPlayer.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	

}

package young.exercise.info;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class MusicPlayerService extends Service {

	private MediaPlayer mPlayer;
	private int mIntroId;

	@Override
	public IBinder onBind(Intent intent) {
		
		mIntroId = intent.getIntExtra("com.exercise.info.intro1", R.raw.introduction);
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
		mPlayer = MediaPlayer.create(this, mIntroId);
		registerReceiver(mPauseReceiver, new IntentFilter("com.exercise.info.broadcast.pausemusic"));
		
	}
	
	private BroadcastReceiver mPauseReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (null != mPlayer) { 
				if(mPlayer.isPlaying()){
					mPlayer.pause();
				}else {
					mPlayer.seekTo(0);
					mPlayer.start();
				}
			}
		}
	};

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

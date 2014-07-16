package young.exercise.info;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicPlayerService extends Service{

	private MediaPlayer player;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	 public void onDestroy() {
	  super.onDestroy();
	  player.stop();
	 }
	
	 @Override
	 public void onStart(Intent intent, int startId) {

	  super.onStart(intent, startId);
	  player = MediaPlayer.create(this, R.raw.introduction);
	  player.start();
	 }


}

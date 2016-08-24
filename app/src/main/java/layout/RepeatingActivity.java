package layout;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bryanjiang.bryan.focusredo.R;

public class RepeatingActivity extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("it worked", "we are here");

        mediaPlayer = MediaPlayer.create(this, R.raw.notification_ring);
        mediaPlayer.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }
}

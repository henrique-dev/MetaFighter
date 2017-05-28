package phdev.com.br.metafighter;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public final class SoundManager {

    private Context context;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    private int currentMusic;

    public SoundManager(Context context){
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        this.context = context;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    public void playMusic(int id){
        currentMusic = id;
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, id);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp != null)
                    mp.start();
            }
        });
        mediaPlayer.start();
    }

    public int getCurrentMusic() {
        return currentMusic;
    }

    public void playSound(int id){
        soundPool.play(id, 1, 1, 1, 0, 1f);
    }

    public void release(){
        mediaPlayer.release();
        soundPool.release();
    }
}

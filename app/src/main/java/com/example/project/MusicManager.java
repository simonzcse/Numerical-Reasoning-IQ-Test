package com.example.project;

import android.content.Context;
import android.media.MediaPlayer;
//Reference:https://hant-kb.kutu66.com/background/post_12283212
public class MusicManager implements MediaPlayer.OnPreparedListener {
    static MediaPlayer mPlayer;
    Context context;
    private int mySoundId;
    public MusicManager(Context ctx, int musicID) {
        context = ctx;
        mySoundId = musicID;
        mPlayer = MediaPlayer.create(context, mySoundId);
        mPlayer.setOnPreparedListener(this);
    }
    public void play() {
        mPlayer = MediaPlayer.create(context, mySoundId);
    }
    public void stop() {
        mPlayer.stop();
        mPlayer.release();
    }
    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
        mPlayer.setLooping(false);
        mPlayer.setVolume(25, 25);
    }
}

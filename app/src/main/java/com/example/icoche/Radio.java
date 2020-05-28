package com.example.icoche;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Radio extends AppCompatActivity {

    Button btn;
    String stream = "http://20073.live.streamtheworld.com/LOS40.mp3";
    Boolean prepared = false;
    Boolean started = false;
    MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        btn = findViewById(R.id.audioStreamBtn);
        btn.setEnabled(false);
        btn.setText("Cargando");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(this);

        new PlayerTask().execute(stream);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (started) {
                    started = false;
                    mediaPlayer.pause();
                    btn.setText("Reproducir");
                } else {
                    started = true;
                    mediaPlayer.start();
                    btn.setText("Pausar");

                }
            }
        });
    }






         class PlayerTask extends AsyncTask<String,Void,Boolean>{

             @Override
             protected Boolean doInBackground(String... strings) {

                 try {
                     mediaPlayer.setDataSource(strings[0]);
                     mediaPlayer.prepare();
                     prepared = true;
                 } catch (IOException e) {
                     e.printStackTrace();
                 }


                 return prepared;
             }

             @Override
             protected void onPostExecute(Boolean aBoolean) {
                 super.onPostExecute(aBoolean);

                 btn.setEnabled(true);
                 btn.setText("Reproducir");
             }
         }

    @Override
    protected void onPause() {
        super.onPause();

        if (started) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (started) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (started) {
            mediaPlayer.release();
        }
    }
}



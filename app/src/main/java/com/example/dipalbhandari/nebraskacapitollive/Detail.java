package com.example.dipalbhandari.nebraskacapitollive;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import static com.example.dipalbhandari.nebraskacapitollive.MainActivity.EXTRA_URL;

import static com.example.dipalbhandari.nebraskacapitollive.MainActivity.Video_Desc;
import static com.example.dipalbhandari.nebraskacapitollive.MainActivity.Video_Title;
import static java.lang.System.err;


public class Detail  extends AppCompatActivity{




    private ImageButton top_back;
    private TextView top_title;
    private TextView description_textview;




    private TextView error;






        JWPlayerView mPlayerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.detail);

            Intent intent = getIntent();

            String videoTitle = intent.getStringExtra(Video_Title);
            String videoURL = intent.getStringExtra(EXTRA_URL);





                String videoDesc = intent.getStringExtra(Video_Desc);

                top_back = findViewById(R.id.top_back);
                top_title = findViewById(R.id.top_title);
                description_textview = findViewById(R.id.description_textview);
                error  = (TextView) findViewById(R.id.error);

                top_title.setText(videoTitle);
                description_textview.setText(videoDesc);



                top_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent home = new Intent(Detail.this, MainActivity.class);
                        startActivity(home);

                    }
                });

           // MyTask task = new MyTask();
            //task.execute(url);


                mPlayerView = findViewById(R.id.video_player_fragment);
                PlaylistItem playlistItem = new PlaylistItem.Builder()
                        .file(videoURL)
                        .mediaId("123acb4e")
                        .build();


                mPlayerView.addOnErrorListener(new VideoPlayerEvents.OnErrorListener() {



                    @Override
                    public void onError(ErrorEvent errorEvent) {


                        error.setText("The Stream has ended");

                        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);

                        builder.setCancelable(true);
                        builder.setTitle("Alert");
                        builder.setMessage("Stream is closed");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }

                        });
                        builder.show();



                    }
                });





                List<PlaylistItem> playlist = new ArrayList<>();
                playlist.add(playlistItem);
                PlayerConfig config = new PlayerConfig.Builder()
                        .playlist(playlist)
                        .build();
                mPlayerView.setup(config);

            }


   /* private class MyTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;
            if (bResponse==true)
            {
                Toast.makeText(Detail.this, "File exists!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(Detail.this, "File does not exist!", Toast.LENGTH_SHORT).show();
            }
        }

} */






        @Override
        protected void onStart() {
            super.onStart();
            mPlayerView.onStart();
        }

        @Override
        protected void onResume() {
            super.onResume();
            mPlayerView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mPlayerView.onPause();
        }

        @Override
        protected void onStop() {
            super.onStop();
            mPlayerView.onStop();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mPlayerView.onDestroy();
        }
    }
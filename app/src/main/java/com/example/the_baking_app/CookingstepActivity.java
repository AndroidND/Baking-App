package com.example.the_baking_app;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.the_baking_app.models.CookingStep;
import com.example.the_baking_app.models.Recipe;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CookingstepActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tv_cookingstep_description) TextView tv_cookingstep_description;
    @BindView(R.id.iv_cookingstep) ImageView iv_cookingstep;
    @BindView(R.id.player_view) PlayerView playerView;
    @BindView(R.id.previous_step_button) Button button_previous;
    @BindView(R.id.next_step_button) Button button_next;

    @OnClick(R.id.next_step_button) void next() {
        if (currentStepIndex >= steps.size() - 1) {
            currentStepIndex = steps.size() - 1;
        } else {
            currentStepIndex++;
            populateStepUI();
        }
    }

    @OnClick(R.id.previous_step_button) void previous() {
        if (currentStepIndex <= 0) {
            currentStepIndex = 0;
        } else {
            currentStepIndex--;
            populateStepUI();
        }
    }


    private String video_url;
    private String image_url;

    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private CookingStep cookingStep;
    private List<CookingStep> steps;
    private int currentStepIndex;
    private String recipe;

    private Menu menu;

    private FrameLayout stepContainer;

    public static final String MIME_VIDEO = "video/";
    public static final String MIME_IMAGE = "image/";

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(params);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().hide();
            }
            button_previous.setVisibility(View.GONE);
            button_next.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            //unhide your objects here.
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            playerView.setLayoutParams(params);
            //To show the action bar
            if(getSupportActionBar()!=null) {
                getSupportActionBar().show();
            }
            button_previous.setVisibility(View.VISIBLE);
            button_next.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        this.menu = menu;
        updateMenu();
        return true;
    }

    private void updateMenu() {
        for (CookingStep step: steps) {
            menu.add(0,step.getId(),step.getId(),step.getId() + ". " + step.getShortDescription());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        currentStepIndex = item.getItemId();
        populateStepUI();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookingstep);

        ButterKnife.bind(this);


        stepContainer = findViewById(R.id.cookingsteps_container);

        Intent intent = getIntent();
        steps = intent.getParcelableArrayListExtra(MainActivity.EXTRA_STEPS);
        currentStepIndex = intent.getIntExtra(DetailActivity.EXTRA_STEP_INDEX,0);
        recipe = intent.getStringExtra(DetailActivity.EXTRA_RECIPE);

        toolbar.setTitle(recipe);
        setSupportActionBar(toolbar);

        populateStepUI();

    }

    private void populateStepUI() {
        cookingStep = steps.get(currentStepIndex);
        tv_cookingstep_description.setText(cookingStep.getDescription());
        video_url = cookingStep.getVideoURL();
        image_url = cookingStep.getThumbnailURL();


        Log.d("populateStepUI", "populateStepUI videoURL: " + video_url);
        Log.d("populateStepUI", "populateStepUI imageURL: " + image_url);
        if (video_url != "") {
            playerView.setVisibility(View.VISIBLE);
            iv_cookingstep.setVisibility(View.GONE);
            initializePlayer();
        } else if (image_url != ""){
            String mimeType = getMimeType(this, Uri.parse(image_url));
            // sometimes video url stored incorrectly in image url,
            if (mimeType.startsWith(MIME_VIDEO)){
                video_url = image_url;
                playerView.setVisibility(View.VISIBLE);
                iv_cookingstep.setVisibility(View.GONE);
                initializePlayer();
            } else {
                playerView.setVisibility(View.GONE);
                iv_cookingstep.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(image_url)
                        .into(iv_cookingstep);
            }

        }


        /**
         * Source: https://stackoverflow.com/questions/12473851/how-i-can-get-the-mime-type-of-a-file-having-its-uri
         * @param context
         * @param uri
         * @return
         */
    }

    private String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        // check for steps first?
//        url = steps.get(currentStepIndex).getVideoURL();
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Uri uri = Uri.parse(video_url);
        MediaSource mediaSource = buildMediaSource(uri);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "baking-app");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}



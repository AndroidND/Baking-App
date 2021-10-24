package com.example.the_baking_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.the_baking_app.models.CookingStep;
import com.google.android.exoplayer2.C;
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

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CookingStepDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CookingStepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CookingStepDetailFragment extends Fragment {

    public static final String EXTRA_STEP = "step_index";

    private static CookingStep mCookingStep;

    private String mVideoUrl;
    private String mThumbnailUrl;
    private TextView textView;
    private ImageView imageView;
    private PlayerView playerView;


    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;


    private String recipe;

    private Menu menu;

    public static CookingStepDetailFragment newInstance (CookingStep step) {
        mCookingStep = step;

        CookingStepDetailFragment fragment = new CookingStepDetailFragment();
        // set bundle arguments for fragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_STEP, step);
        fragment.setArguments(arguments);
        return fragment;
    }



        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cooking_step_detail, container, false);
        // get reference to Exoplayer PlayerView, TextView, and buttons next and previous
         playerView = (PlayerView) rootView.findViewById(R.id.player_view);
         imageView = (ImageView) rootView.findViewById(R.id.iv_no_video_placeholder);
         textView = (TextView) rootView.findViewById(R.id.tv_cookingstep_description);

         populateStepUI();
            // display video only
            Context context = getContext();
//             create Exoplayer instance, can also use Exoplayer.Builder
            player = new SimpleExoPlayer.Builder(context).build();

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.getApplicationInfo().loadLabel(context.getPackageManager()).toString()));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource =
                    new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(mVideoUrl));
            // Prepare the player with the source.
            player.prepare(videoSource);

            // bind player to PlayerView
            playerView.setPlayer(player);

            playerView.setVisibility(View.VISIBLE);

        return rootView;
    }



    private void populateStepUI() {
        CookingStep cookingStep = mCookingStep;
        textView.setText(cookingStep.getDescription());
        mVideoUrl = cookingStep.getVideoURL();
        mThumbnailUrl = cookingStep.getThumbnailURL();
        Log.d("populateStepUI", "populateStepUI videoURL: " + mVideoUrl);
        Log.d("populateStepUI", "populateStepUI imageURL: " + mThumbnailUrl);
        if (mVideoUrl != "") {
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            initializePlayer();
        }
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

        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);

        Uri uri = Uri.parse(mVideoUrl);
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
                new DefaultDataSourceFactory(getActivity(), "baking-app");
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

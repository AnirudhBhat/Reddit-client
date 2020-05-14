package com.abhat.reddit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_media.*
import kotlinx.android.synthetic.main.custom_player_ui_layout.*


/**
 * Created by Anirudh Uppunda on 13,May,2020
 */
class MediaActivity : AppCompatActivity(), Player.EventListener {

    private var player: SimpleExoPlayer? = null

    private lateinit var bandwidthMeter: DefaultBandwidthMeter

    private lateinit var dataSourceFactory: DefaultDataSourceFactory

    private lateinit var extractorsFactory: DefaultExtractorsFactory

    private lateinit var dashChunkSource: DefaultDashChunkSource.Factory

    private lateinit var mediaSource: ExtractorMediaSource

    private lateinit var dashMediaSource: DashMediaSource

    private lateinit var trackSelectionFactory: AdaptiveTrackSelection.Factory

    private lateinit var trackSelector: DefaultTrackSelector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        var url = intent.getStringExtra("url")
        var shouldUseGlide = intent.getBooleanExtra("shoulduseglide", false)
        if (url.contains("imgur") && !(url.endsWith(".jpg") || url.endsWith(".png"))) {
            url = url.replace("gifv", "mp4").replace("http:", "https:")
            shouldUseGlide = false
        }
        if (shouldUseGlide) {
            image.visibility = View.VISIBLE
            exoplayer.visibility = View.GONE
            Glide
                .with(this)
                .load(url)
                .into(image)
        } else {
            image.visibility = View.GONE
            exoplayer.visibility = View.VISIBLE
            url?.let { url ->
                prepareExoPlayer()
                initExoPlayerr(url, exoplayer)
            }
        }

        volume.setOnClickListener {
            if (player?.volume ?: 0f <= 0f) {
                volume.setImageResource(R.drawable.ic_volume_up)
                player?.volume = 100f
            } else {
                volume.setImageResource(R.drawable.ic_volume_off)
                player?.volume = 0f
            }
        }
    }

    private fun prepareExoPlayer() {
        bandwidthMeter = DefaultBandwidthMeter()
        trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        trackSelector = DefaultTrackSelector(trackSelectionFactory)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        // Default parameters, except allowCrossProtocolRedirects is true
        // https://github.com/google/ExoPlayer/issues/423
        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, "mediaPlayerSample"),
            null /* listener */,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true /* allowCrossProtocolRedirects */
        )
        dataSourceFactory = DefaultDataSourceFactory(this, null, httpDataSourceFactory)
        extractorsFactory = DefaultExtractorsFactory()
    }

    fun initExoPlayerr(podcastStreamLink: String, exoPlayer: PlayerView) {
        exoPlayer.visibility = View.VISIBLE
        image.visibility = View.GONE
        prepareExoPlayer()
        try {
            player?.addListener(this)
            Log.d("TAG", "AUDIO URL: $podcastStreamLink")
            val mediaSource =
                buildMediaSource(Uri.parse(podcastStreamLink))//ExtractorMediaSource(Uri.parse(podcastStreamLink), dataSourceFactory, extractorsFactory, null, null)
            player?.prepare(mediaSource)
            exoPlayer.player = player
            exoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            player?.playWhenReady = true
        } catch (e: Exception) {
            player?.release()
            player = null
            exoPlayer.player.release()
            initExoPlayerr(podcastStreamLink, exoPlayer)

        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = "exoplayer-reddit"
        when {
            uri.lastPathSegment?.contains("mp3") == true || uri.lastPathSegment?.contains("mp4") == true -> {
                return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri)
            }
            uri.lastPathSegment?.contains("m3u8") == true -> {
                return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri)
            }
            else -> {
                val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                    DefaultHttpDataSourceFactory(Util.getUserAgent(this, "exoplayer-reddit"), null)
                )
                return DashMediaSource.Factory(dashChunkSourceFactory, DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri)
            }
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_ENDED -> {
                player?.release()
                player = null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
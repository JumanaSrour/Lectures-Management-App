package com.example.schoolie.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.schoolie.R
import com.example.schoolie.models.Lecture
import com.example.schoolie.utilities.SavedPreferences
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lecture_details.*
import org.jetbrains.anko.appcompat.v7.activityChooserView

class LectureDetailsActivity : AppCompatActivity() {
    private var simpleExpoPlayer: SimpleExoPlayer?= null
    private var playerView: PlayerView?= null
    var playWhenReady = true
    var currentPosition: Long = 0
    var currentWindow = 0
    private var lectureCollectionRef = Firebase.firestore.collection("lectures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_details)
        retrieveLectureDetails()
    }

    private fun retrieveLectureDetails() {
        val uid = SavedPreferences.user_id
        if (uid != "") {
            lectureCollectionRef.get()
                .addOnCompleteListener {
                    try {
                        val data = arrayListOf<Lecture>()
                        for (lectureDetails in it.result.documents) {
                            val obj = lectureDetails.toObject<Lecture>()
                            data.add(obj!!)
                            tv_lecturer_name.text = obj.lecture_instructor
                            tv_lec_desc.text = obj.lecture_desc
                            tv_lec_title.text = obj.lecture_title
                            Glide.with(this).load(obj.lecture_image).into(iv_lec_icon)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun initPlayer() {
        lectureCollectionRef.get()
            .addOnCompleteListener {
                val video = arrayListOf<Lecture>()
                for (videos in it.result.documents) {
                    val obj = videos.toObject<Lecture>()
                    video.add(obj!!)
                    simpleExpoPlayer = SimpleExoPlayer.Builder(this).build()
                    playerView = exoplayerView
                    playerView!!.player = simpleExpoPlayer

                    val item: MediaItem = MediaItem.fromUri(obj.lecture_video)
                    simpleExpoPlayer!!.playWhenReady = playWhenReady
                    simpleExpoPlayer!!.addMediaItem(item)
                    simpleExpoPlayer!!.seekTo(currentWindow, currentPosition)
                    simpleExpoPlayer!!.prepare()
                }
            }
    }
    private fun releasePlayer() {
        if (simpleExpoPlayer != null) {
            playWhenReady = simpleExpoPlayer!!.playWhenReady
            currentWindow = simpleExpoPlayer!!.currentWindowIndex
            currentPosition = simpleExpoPlayer!!.currentPosition
            simpleExpoPlayer = null
        }
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onResume() {
        super.onResume()
        if (simpleExpoPlayer != null) {
            initPlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }
}

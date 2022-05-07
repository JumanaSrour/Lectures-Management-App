package com.example.schoolie.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import java.io.File

class LectureDetailsActivity : AppCompatActivity() {
    private var simpleExpoPlayer: SimpleExoPlayer? = null
    private var playerView: PlayerView? = null
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
                            val file = File(
                                Environment.getExternalStorageDirectory(),
                                obj.lecture_assignment
                            )
                            val path = Uri.fromFile(file)
                            val pdfOpenintent = Intent(Intent.ACTION_VIEW)
                            pdfOpenintent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            pdfOpenintent.setDataAndType(path, "application/pdf")
                            try {
                                pdfView.setOnClickListener {
                                    startActivity(pdfOpenintent)
                                }
                            } catch (e: ActivityNotFoundException) {
                            }
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
    private fun openFile(url: File) {
        try {
            val uri: Uri = Uri.fromFile(url)
            val intent = Intent(Intent.ACTION_VIEW)
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip")
            } else if (url.toString().contains(".rar")) {
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed")
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf")
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav")
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif")
            } else if (url.toString().contains(".jpg") || url.toString()
                .contains(".jpeg") || url.toString().contains(".png")
            ) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg")
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                url.toString().contains(".mpeg") || url.toString()
                    .contains(".mpe") || url.toString().contains(".mp4") || url.toString()
                    .contains(".avi")
            ) {
                // Video files
                intent.setDataAndType(uri, "video/*")
            } else {
                intent.setDataAndType(uri, "*/*")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "No application found which can open the file",
                Toast.LENGTH_SHORT
            ).show()
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

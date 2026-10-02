package com.memesound.app

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

data class MemeSound(
    val name: String,
    val resourceId: Int
)

class MainActivity : AppCompatActivity() {

    private var player: MediaPlayer? = null

    private val sounds = listOf(
        MemeSound("Test Sound", R.raw.test_sound)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val title = TextView(this).apply {
            text = "🎵 Meme Sound"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "Tap Preview to listen, or Send to WhatsApp."
            textSize = 16f
            setPadding(0, 16, 0, 24)
        }

        root.addView(title)
        root.addView(subtitle)

        sounds.forEach { sound ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, 8, 0, 8)
            }

            val name = TextView(this).apply {
                text = sound.name
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(0, 56).apply {
                    weight = 1f
                }
            }

            val preview = Button(this).apply {
                text = "Preview"
                setOnClickListener {
                    playSound(sound.resourceId)
                }
            }

            val send = Button(this).apply {
                text = "Send"
                setOnClickListener {
                    shareSound(sound)
                }
            }

            row.addView(name)
            row.addView(preview)
            row.addView(send)
            root.addView(row)
        }

        setContentView(root)
    }

    private fun playSound(resourceId: Int) {
        player?.release()
        player = MediaPlayer.create(this, resourceId)
        player?.setOnCompletionListener {
            it.release()
            player = null
        }
        player?.start()
    }

    private fun shareSound(sound: MemeSound) {
        val fileName = "${sound.name.lowercase().replace(" ", "_")}.wav"
        val shareDir = File(cacheDir, "shared_sounds")
        shareDir.mkdirs()

        val file = File(shareDir, fileName)

        resources.openRawResource(sound.resourceId).use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/wav"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Send sound"))
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}

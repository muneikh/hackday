package com.example.android.tflitecamerademo

import android.content.Context
import android.speech.tts.TextToSpeech
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import java.lang.StringBuilder
import java.util.HashMap

class TalkingHead(val context: Context) {
    private val textToSpeech = TextToSpeech(context) {
        Timber.d("TTS init: $it")
    }

    init {
    }

    fun say(text: String) {
        if (text.contains("other") || !textToSpeech.isSpeaking)
            textToSpeech.speak(createText(text), TextToSpeech.QUEUE_ADD, null, null)
    }

    private var count = 0

    private fun createText(name: String): String {
        val stringRes = templates[count++ % templates.size]
        return context.getString(stringRes, name)
    }


    val templates = arrayOf(
            R.string.speechAmerica,
            R.string.speechAustrlia,
            R.string.speechFrance,
            R.string.speechGermay,
            R.string.speechPakistan,
            R.string.speechSweden
    )

}


class FaceDetectionResult(
        val probability: Float,
        val name: String
) {
    companion object {
        fun processResults(results: HashMap<String, Float>): FaceDetectionResult? {

//            results.keys.forEach {
//                Timber.d("$it : ${results[it]}")
//            }

            return results
                    .filter {
                        it.value > 0.6
                    }
                    .map {
                        FaceDetectionResult(it.value, it.key)
                    }
                    .sortedByDescending {
                        it.probability
                    }
                    .firstOrNull()

//            return emptyArray()
        }

        fun makeString(results: HashMap<String, Float>): String {

            val sb = StringBuilder()

            results
                    .map {
                        FaceDetectionResult(it.value, it.key)
                    }
                    .sortedByDescending {
                        it.probability
                    }
                    .forEach { item ->
                        val floatString = "%.2f".format(item.probability)
                        sb.append("${item.name} : $floatString \n")
                    }

            return sb.toString()
//            return emptyArray()
        }
    }
}
package com.eecs40.homework_4;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Michael on 6/4/2015.
 */
public class SpeechInput {
    public static final int REQUEST_SPEECH_INPUT = 1002;
    private Context context;
    private MainActivity main;

    public SpeechInput(Context context, MainActivity main){
        this.context = context;
        this.main = main;
    }

    //Call dispatchspeechInputIntent() when the speech-to-text button is clicked
    void dispatchSpeechInputIntent(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(R.string.speech_prompt));
        try{
            main.startActivityForResult(intent, REQUEST_SPEECH_INPUT);
        }
        catch(ActivityNotFoundException a){
            Toast.makeText(context, "Speech-to-Text Not Detected", Toast.LENGTH_SHORT).show();
        }
    }
}

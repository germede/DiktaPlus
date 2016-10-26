package singularfactory.app.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.presenters.PresenterTexts;

public class GameFragment extends BaseFragment implements TextToSpeech.OnInitListener {
    View view;
    TextView pressTheButtonLabel;
    ImageButton playButton;
    EditText gameTextEdit;
    String textToPlay;
    TextToSpeech tts;


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");
            } else {
                dictate();
            }
        } else {
            Log.e("TTS", "Initilization failed");
        }
    }

    public void dictate () {
        String [] words = textToPlay.split(" ");
        for (int i = 0; i < words.length; i++) {
            
        }
    }

    public GameFragment() {
        // Required empty public constructor
    }

    public void play() {
        pressTheButtonLabel = (TextView)view.getRootView().findViewById(R.id.press_the_button_label);
        playButton = (ImageButton) view.getRootView().findViewById(R.id.play_button);
        gameTextEdit = (EditText) view.getRootView().findViewById(R.id.game_text_edit);

        pressTheButtonLabel.setVisibility(View.GONE);
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        gameTextEdit.setVisibility(View.VISIBLE);

        tts = new TextToSpeech(getContext(),this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void setTextToPlay (String textToPlay) {
        this.textToPlay=textToPlay;
    }

}

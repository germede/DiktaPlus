package singularfactory.app.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.models.Text;
import singularfactory.app.views.activities.MainActivity;

public class GameFragment extends BaseFragment implements TextToSpeech.OnInitListener {
    View view;
    TextView languageGameLabel;
    TextView difficultyGameLabel;
    TextView bestScoreGameLabel;
    TextView pressTheButtonLabel;
    TextView progressLabel;
    ProgressBar progressBar;
    ImageButton playButton;
    EditText gameTextEdit;

    TextToSpeech tts;
    Text textToPlay;
    String [] words;
    int wordIndex;


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale(textToPlay.getLanguage()));
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");
            } else {
                startDictation();
            }
        } else {
            Log.e("TTS", "Initilization failed");
        }
    }

    public void startDictation () {
        //wordIndex = 0;
        //words = textToPlay.getContent().split(" ");

        if (textToPlay.getDifficulty().equals("Easy")) {
            tts.setSpeechRate(0.1f);
        } else if (textToPlay.getDifficulty().equals("Medium")) {
            tts.setSpeechRate(0.25f);
        } else {
            tts.setSpeechRate(0.4f);
        }

        tts.speak(textToPlay.getContent(),TextToSpeech.QUEUE_ADD,null);

    }

    public void dictateNextWord() {
        if (wordIndex < words.length) {
            tts.speak(words[wordIndex],TextToSpeech.QUEUE_ADD,null);
            wordIndex++;
        }
    }

    public void stopDictation() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        ((MainActivity)getActivity()).changeToTextFragment();
    }

    public GameFragment() {
        // Required empty public constructor
    }

    public void play() {
        // Initialize invisible elements
        pressTheButtonLabel = (TextView)view.getRootView().findViewById(R.id.press_the_button_label);
        gameTextEdit = (EditText) view.getRootView().findViewById(R.id.game_text_edit);
        progressBar = (ProgressBar) view.getRootView().findViewById(R.id.progress_bar);
        progressLabel = (TextView) view.getRootView().findViewById(R.id.progress_label);

        // Hide all the labels
        languageGameLabel.setVisibility(View.GONE);
        difficultyGameLabel.setVisibility(View.GONE);
        bestScoreGameLabel.setVisibility(View.GONE);
        pressTheButtonLabel.setVisibility(View.GONE);

        // Change image of button and its behaviour
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDictation();
            }
        });

        // Show progress bar
        progressLabel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // Show text area to play
        gameTextEdit.setVisibility(View.VISIBLE);
        gameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().charAt(s.length()-1) == ' ') {
                    //dictateNextWord();
                }
            }
        });

        // Request focus for text area and show keyboard
        gameTextEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(gameTextEdit, InputMethodManager.SHOW_IMPLICIT);

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

        languageGameLabel = (TextView)view.getRootView().findViewById(R.id.language_game_label);
        difficultyGameLabel = (TextView)view.getRootView().findViewById(R.id.difficulty_game_label);
        bestScoreGameLabel = (TextView)view.getRootView().findViewById(R.id.best_score_game_label);

        languageGameLabel.setText(getActivity().getApplicationContext().getString(R.string.language, textToPlay.getLanguage()));
        difficultyGameLabel.setText(getActivity().getApplicationContext().getString(R.string.difficulty, textToPlay.getDifficulty()));
        bestScoreGameLabel.setText(getActivity().getApplicationContext().getString(R.string.best_score,textToPlay.getBestScore()));

        playButton = (ImageButton) view.getRootView().findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

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

    public void setTextToPlay (Text textToPlay) {
        this.textToPlay=textToPlay;
    }

}

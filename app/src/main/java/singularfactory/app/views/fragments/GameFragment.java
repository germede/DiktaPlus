package singularfactory.app.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;

import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.common.Utils;
import singularfactory.app.models.Text;
import singularfactory.app.views.activities.MainActivity;

public class GameFragment extends BaseFragment implements TextToSpeech.OnInitListener {
    View view;
    TextView languageGameLabel;
    TextView difficultyGameLabel;
    TextView bestScoreGameLabel;
    TextView pressTheButtonLabel;
    ProgressBar progressBar;
    ImageButton playButton;
    ImageButton repeatButton;
    EditText gameTextEdit;
    Chronometer chronometer;
    BaseInputConnection textFieldInputConnection;
    ScrollView sv;

    TextToSpeech tts;
    Text textToPlay;
    String [] words;
    int wordIndex;
    String[] originalText;
    int score;
    int bestScore;
    int level;

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
        words = null;
        originalText = null;
        wordIndex = 0;

        String textToSplit = textToPlay.getContent();
        // Remove punctuation
        textToSplit = textToSplit.replaceAll("\\.","");
        textToSplit = textToSplit.replaceAll("\\(","");
        textToSplit = textToSplit.replaceAll("\\)","");
        textToSplit = textToSplit.replaceAll("\\,","");

        originalText = textToPlay.getContent().split(" ");
        words = textToSplit.split(" ");
        switch(textToPlay.getDifficulty()) {
            case "Easy": tts.setSpeechRate(0.05f); break;
            case "Medium": tts.setSpeechRate(0.55f); break;
            case "Hard": tts.setSpeechRate(2f); break;
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        dictateNextWord();
    }

    public void dictateNextWord() {
        if (wordIndex > 0) {
            pressTheButtonLabel.append(originalText[wordIndex-1]+" ");
            sv.scrollTo(0,sv.getBottom());
        }
        if (wordIndex < words.length) speakActualWord();
        else gameOver();
        progressBar.setProgress((int)(((float)wordIndex/(float)words.length)*100));
        gameTextEdit.setText("");
    }

    private void speakActualWord() {
        tts.speak(words[wordIndex],TextToSpeech.QUEUE_ADD,null);
    }

    public void gameOver() {
        stopDictation();

        score = (int) (10000000/(SystemClock.elapsedRealtime()-chronometer.getBase()));
        switch(textToPlay.getDifficulty()) {
            case "Easy": score = score / 4 ; break;
            case "Medium": score = score / 2 ; break;
        }

        if (score > textToPlay.getBestScore()) {
            textToPlay.setBestScore(score);
            bestScore = score;
        }
        int[] params = {appCommon.getUser().getId(),
                textToPlay.getId(),
                score};
        try {
            appCommon.getPresenterGame().postGame(
                    this,
                    "Post game",
                    Request.Method.POST,
                    appCommon.getBaseURL() + "games/add",
                    "Posting game...",
                    params);
        } catch (JSONException e) {
            Log.e(TAG,"JSON error");
        }

    }

    public void showGameOverDialog(int levelup) {
        if (levelup > 0) level = levelup;
        else level = 0;
        if (levelup > appCommon.getUser().getLevel()) {
            appCommon.getUser().setLevel(levelup);
            ((MainActivity)getActivity()).setUsernameLabelAndLevelLabel();
        }

        GameOverDialog gameOverDialog = new GameOverDialog(getActivity());
        gameOverDialog.show();
    }

    public void stopDictation() {
        chronometer.stop();
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public GameFragment() {
        // Required empty public constructor
    }

    public void play() {

        // Change image of button and its behaviour
        pressTheButtonLabel.setText("");
        pressTheButtonLabel.setVisibility(View.VISIBLE);
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeToTextFragment(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        // Show text area to play
        gameTextEdit.setVisibility(View.VISIBLE);
        gameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String t = s.toString();
                if (t.length() > 1 && t.charAt(t.length()-1) == ' ') {
                    t = t.trim();
                    Log.e(TAG,wordIndex+""+words.length);
                    if (wordIndex >= words.length) gameOver();
                    else if (t.equals(words[wordIndex])) {
                        wordIndex++;
                        dictateNextWord();
                    } else {
                        showToast(getString(R.string.typing_error));
                        textFieldInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                        speakActualWord();
                    }
                }
            }
        });

        // Request focus for text area and show keyboard
        gameTextEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(gameTextEdit, InputMethodManager.SHOW_IMPLICIT);

        tts = new TextToSpeech(getContext(),this);

        repeatButton = (ImageButton) view.getRootView().findViewById(R.id.repeat_button);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { speakActualWord(); }
        });
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

        languageGameLabel.setText(toProperCase(new Locale (textToPlay.getLanguage()).getDisplayLanguage()));
        switch(textToPlay.getDifficulty()) {
            case "Easy": difficultyGameLabel.setText(getResources().getStringArray(R.array.difficulties)[0]); break;
            case "Medium": difficultyGameLabel.setText(getResources().getStringArray(R.array.difficulties)[1]); break;
            case "Hard": difficultyGameLabel.setText(getResources().getStringArray(R.array.difficulties)[2]); break;
        }
        bestScoreGameLabel.setText(getActivity().getResources().getString(R.string.best_score,textToPlay.getBestScore()));

        playButton = (ImageButton) view.getRootView().findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        chronometer = (Chronometer) view.getRootView().findViewById(R.id.chronometer);

        // Initialize elements
        pressTheButtonLabel = (TextView)view.getRootView().findViewById(R.id.press_the_button_label);
        gameTextEdit = (EditText) view.getRootView().findViewById(R.id.game_text_edit);
        progressBar = (ProgressBar) view.getRootView().findViewById(R.id.progress_bar);
        textFieldInputConnection = new BaseInputConnection(gameTextEdit, true);
        sv = (ScrollView) view.getRootView().findViewById(R.id.scroll);



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

    class GameOverDialog extends Dialog implements
            android.view.View.OnClickListener {
        TextView levelupLabel, scoreLabel, bestScoreLabel;
        Button ok, repeat;

        GameOverDialog(Activity a) {super(a);}
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_game_over);

            levelupLabel = (TextView)findViewById(R.id.game_over_levelup_label);
            scoreLabel = (TextView)findViewById(R.id.game_over_score_label);
            bestScoreLabel = (TextView)findViewById(R.id.game_over_best_score_label);

            if (level > 0) {
                levelupLabel.setText(getActivity().getString(R.string.level_up,level));
                levelupLabel.setVisibility(View.VISIBLE);
            }
            scoreLabel.setText(getActivity().getString(R.string.score, score));
            bestScoreLabel.setText(getActivity().getString(R.string.best_score, bestScore));


            ok = (Button) findViewById(R.id.btn_gameover_ok);
            ok.setOnClickListener(this);
            repeat = (Button) findViewById(R.id.btn_gameover_repeat);
            repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    play();
                }
            });

            setTitle(getString(R.string.game_over));
            setCancelable(false);
        }
        @Override
        public void onClick(View v) {
            dismiss();
            ((MainActivity)getActivity()).changeToTextFragment(R.anim.slide_in_left,R.anim.slide_out_right);
        }
    }

}

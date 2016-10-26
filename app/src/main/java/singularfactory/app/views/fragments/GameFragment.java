package singularfactory.app.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

import singularfactory.app.R;
import singularfactory.app.presenters.PresenterTexts;

public class GameFragment extends BaseFragment {
    View view;
    TextView pressTheButtonLabel;
    ImageButton playButton;
    EditText gameTextEdit;

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

    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game, container, false);
        return view;
    }


}

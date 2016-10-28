package singularfactory.app.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.models.Text;

public class TextFragment extends BaseFragment {
    View view;
    ExpandableListView textsList;
    ExpandableListAdapter textsListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    JSONArray receivedList;
    Text selectedText;

    ImageButton languageLeft, languageRight, difficultyLeft, difficultyRight;

    final String[] languages = {"EN","ES","DE","FR","IT"};
    final int[] flags = {R.drawable.en,
            R.drawable.es,
            R.drawable.de,
            R.drawable.fr,
            R.drawable.it,
    };
    Locale[] languagesLocales;
    int selectedLanguage;
    TextView languageLabel;
    ImageView languageFlag;

    final String[] difficulties = {"Easy", "Medium", "Hard"};
    int selectedDifficulty;
    TextView difficultyLabel;

    public TextFragment() {
        // Required empty public constructor
    }

    public Text getSelectedText() {
        return selectedText;
    }

    private void getTextList() {
        appCommon.getPresenterText().getTexts(
                this,
                "Get texts",
                Request.Method.GET,
                appCommon.getBaseURL()+"texts/"+languages[selectedLanguage]+"/"+difficulties[selectedDifficulty],
                "Loading texts list...");
    }


    public void setTextsList(JSONArray texts) throws JSONException{
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        receivedList = texts;
        List<String> listItemInfo;
        for (int i = 0; i < texts.length(); i++) {
            listItemInfo = new ArrayList<String>();
            listDataHeader.add(texts.getJSONObject(i).getString("title"));
            listItemInfo.add(Integer.toString(111));             //TODO: hacer funcionar el bestscore
            listDataChild.put(listDataHeader.get(i), listItemInfo);
        }

        textsListAdapter = new ExpandableListAdapter(this.getContext(),this, listDataHeader, listDataChild);
        textsList.setAdapter(textsListAdapter);
    }

    public void onErrorGetTexts (String message) {
        textsList.setAdapter((BaseExpandableListAdapter)null);
        showToast(message);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_text, container, false);

        textsList = (ExpandableListView)view.findViewById(R.id.textsList);
        getTextList();

        // Default difficulty: "Medium"
        difficultyLabel = (TextView)view.findViewById(R.id.difficulty_label);
        selectedDifficulty = 1;
        updateDifficultyLabel();

        // Default language: "English"
        languageLabel = (TextView)view.findViewById(R.id.language_label);
        languageFlag = (ImageView)view.findViewById(R.id.language_flag);
        languagesLocales = new Locale[languages.length];
        for (int i = 0; i < languages.length; i++) {languagesLocales[i] = new Locale(languages[i]);}
        selectedLanguage = 0;
        updateLanguageLabel();

        // Switch language and difficulty buttons
        languageLeft = (ImageButton)view.findViewById(R.id.language_left);
        languageRight = (ImageButton)view.findViewById(R.id.language_right);
        difficultyLeft = (ImageButton)view.findViewById(R.id.difficulty_left);
        difficultyRight = (ImageButton)view.findViewById(R.id.difficulty_right);

        // Click listeners for the buttons
        languageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedLanguage == 0) selectedLanguage = languages.length-1;
                else selectedLanguage--;
                updateLanguageLabel();
                getTextList();
            }
        });
        languageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedLanguage == languages.length-1) selectedLanguage = 0;
                else selectedLanguage++;
                languageLabel.setText(toProperCase(languagesLocales[selectedLanguage].getDisplayName()));
                updateLanguageLabel();
                getTextList();
            }
        });
        difficultyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDifficulty == 0) selectedDifficulty = difficulties.length-1;
                else selectedDifficulty--;
                updateDifficultyLabel();
                getTextList();
            }
        });
        difficultyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDifficulty == difficulties.length-1) selectedDifficulty = 0;
                else selectedDifficulty++;
                updateDifficultyLabel();
                getTextList();
            }
        });

        return view;
    }

    private void updateLanguageLabel() {
        languageLabel.setText(toProperCase(languagesLocales[selectedLanguage].getDisplayName()));
        languageFlag.setImageResource(flags[selectedLanguage]);
    }

    private void updateDifficultyLabel() {
        difficultyLabel.setText(difficulties[selectedDifficulty]);
        switch (selectedDifficulty) {
            case 0: difficultyLabel.setTextColor(Color.GREEN);
                break;
            case 1: difficultyLabel.setTextColor(Color.YELLOW);
                break;
            case 2: difficultyLabel.setTextColor(Color.RED);
                break;
            default:break;
        }
    }
}

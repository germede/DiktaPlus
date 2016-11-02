package singularfactory.app.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.models.Text;
import singularfactory.app.views.activities.MainActivity;

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
    Locale[] languagesLocales;
    int selectedLanguage;
    TextSwitcher languageLabel;

    final String[] difficulties = {"Easy", "Medium", "Hard"};
    int selectedDifficulty;
    TextSwitcher difficultyLabel;

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
            listItemInfo = new ArrayList<>();
            listDataHeader.add(texts.getJSONObject(i).getString("title"));
            listItemInfo.add(getString(R.string.best_score,0));
            listDataChild.put(listDataHeader.get(i), listItemInfo);
        }

        textsListAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);
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

        // Default difficulty: "Medium"
        difficultyLabel = (TextSwitcher) view.findViewById(R.id.difficulty_label);
        difficultyLabel.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(getActivity());
                myText.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
                myText.setTextColor(getResources().getColor(R.color.white));
                myText.setTypeface(null, Typeface.BOLD);
                myText.setTextSize(20);
                return myText;
            }
        });
        selectedDifficulty = 1;
        updateDifficultyLabel();

        // Default language: "English"
        languageLabel = (TextSwitcher)view.findViewById(R.id.language_label);
        languageLabel.setInAnimation(getContext(),R.anim.slide_in_left);
        languageLabel.setOutAnimation(getContext(),R.anim.slide_out_right);
        languageLabel.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(getActivity());
                myText.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
                myText.setTextColor(getResources().getColor(R.color.white));
                myText.setTypeface(null, Typeface.BOLD);
                myText.setTextSize(20);
                return myText;
            }
        });
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
                languageLabel.setInAnimation(getContext(),R.anim.slide_in_left);
                languageLabel.setOutAnimation(getContext(),R.anim.slide_out_right);
                if (selectedLanguage == 0) selectedLanguage = languages.length-1;
                else selectedLanguage--;
                updateLanguageLabel();
                getTextList();
            }
        });
        languageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageLabel.setInAnimation(getContext(),R.anim.slide_in_right);
                languageLabel.setOutAnimation(getContext(),R.anim.slide_out_left);
                if (selectedLanguage == languages.length-1) selectedLanguage = 0;
                else selectedLanguage++;
                updateLanguageLabel();
                getTextList();
            }
        });
        difficultyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultyLabel.setInAnimation(getContext(),R.anim.slide_in_left);
                difficultyLabel.setOutAnimation(getContext(),R.anim.slide_out_right);
                if (selectedDifficulty == 0) selectedDifficulty = difficulties.length-1;
                else selectedDifficulty--;
                updateDifficultyLabel();
                getTextList();
            }
        });
        difficultyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultyLabel.setInAnimation(getContext(),R.anim.slide_in_right);
                difficultyLabel.setOutAnimation(getContext(),R.anim.slide_out_left);
                if (selectedDifficulty == difficulties.length-1) selectedDifficulty = 0;
                else selectedDifficulty++;
                updateDifficultyLabel();
                getTextList();
            }
        });

        ((MainActivity)getActivity()).setUsernameLabelAndLevelLabel();
        getTextList();

        return view;
    }

    private void updateLanguageLabel() {
        languageLabel.setText(toProperCase(languagesLocales[selectedLanguage].getDisplayName()));
    }

    private void updateDifficultyLabel() {
        difficultyLabel.setText(getResources().getStringArray(R.array.difficulties)[selectedDifficulty]);
    }

    public void getBestScore() throws JSONException {
        appCommon.getPresenterGame().getBestScore(
                this,
                "Get best score",
                Request.Method.GET,
                appCommon.getBaseURL()+"games/"+appCommon.getUser().getId()+"/"+selectedText.getId(),
                "Loading best score..");
    }

    public void setBestScore(int bestScore) {
        selectedText.setBestScore(bestScore);
        TextView bestScoreLabel = (TextView)view.findViewById(R.id.best_score_label);
        bestScoreLabel.setText(getActivity().getApplicationContext().getString(R.string.best_score, bestScore));
    }

    public void getTextContent(int groupPosition) throws JSONException {
        appCommon.getPresenterText().getTextContent(
                this,
                "Get text content",
                Request.Method.GET,
                appCommon.getBaseURL()+"texts/"+receivedList.getJSONObject(groupPosition).getInt("id"),
                "Loading text content..");
    }

    public void setTextContent(JSONObject text) {
        try {
            selectedText = new Text(text.getInt("id"),
                    text.getString("title"),
                    text.getString("content"),
                    text.getString("language"),
                    text.getString("difficulty"));
            getBestScore();
        } catch (JSONException e) {
            Log.e(TAG,"Error parsing received JSON");
        }
    }

    class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> listDataHeader;
        private HashMap<String, List<String>> listDataChild;
        private int previousGroup;

        ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
            previousGroup = -1;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            try {
                getTextContent(groupPosition);

            } catch (JSONException e) {
                Log.e(TAG,"JSON error");
            }
            if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                textsList.collapseGroup(previousGroup);
            }
            previousGroup = groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.fragment_text_child,null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.best_score_label);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.fragment_text_parent, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.text_title_label);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}

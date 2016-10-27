package singularfactory.app.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.presenters.PresenterTexts;

public class TextsFragment extends BaseFragment {
    View view;
    ExpandableListView textsList;
    ExpandableListAdapter textsListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    JSONArray receivedList;
    DiktaplusText selectedText;

    final String[] languages = {"en","es","de","fr","it"};
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

    public TextsFragment() {
        // Required empty public constructor
    }

    public DiktaplusText getSelectedText() {
        return selectedText;
    }

    private void updateTextList() {
        appCommon.getPresenterTexts().getTextsList(
                this,
                "GET Texts",
                Request.Method.GET,
                "http://192.168.1.15:8000/api/texts/"
                        +languages[selectedLanguage]
                        +"/"+difficulties[selectedDifficulty],
                "Loading texts list...");
    }


    public void setTextsList(JSONArray texts) throws JSONException{
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        receivedList = texts;
        List<String> listItemInfo;
        for (int i = 0; i < texts.length(); i++) {
            listItemInfo = new ArrayList<String>();
            listDataHeader.add(texts.getJSONObject(i).getString("title"));
            listItemInfo.add(Integer.toString(111));             //TODO: hacer funcionar el bestscore
            listDataChild.put(listDataHeader.get(i), listItemInfo);
        }

        textsListAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);
        textsList.setAdapter(textsListAdapter);
    }

    public void onResponseError (String message) {
        textsList.setAdapter((BaseExpandableListAdapter)null);
        showErrorToast(message);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_texts, container, false);

        textsList = (ExpandableListView)view.findViewById(R.id.textsList);
        updateTextList();

        // Default difficulty: "Medium"
        difficultyLabel = (TextView)view.findViewById(R.id.difficulty_label);
        difficultyLabel.setTypeface(appCommon.getFont());
        selectedDifficulty = 1;
        updateDifficultyLabel();

        // Default language: "English"
        languageLabel = (TextView)view.findViewById(R.id.language_label);
        languageLabel.setTypeface(appCommon.getFont());
        languageFlag = (ImageView)view.findViewById(R.id.language_flag);
        languagesLocales = new Locale[languages.length];
        for (int i = 0; i < languages.length; i++) {languagesLocales[i] = new Locale(languages[i]);}
        selectedLanguage = 0;
        updateLanguageLabel();

        return view;
    }

    public void switchLanguageLeft() {
        if (selectedLanguage == 0) selectedLanguage = languages.length-1;
        else selectedLanguage--;
        updateLanguageLabel();
        updateTextList();
    }

    public void switchLanguageRight() {
        if (selectedLanguage == languages.length-1) selectedLanguage = 0;
        else selectedLanguage++;
        languageLabel.setText(toProperCase(languagesLocales[selectedLanguage].getDisplayName()));
        updateLanguageLabel();
        updateTextList();
    }

    private void updateLanguageLabel() {
        languageLabel.setText(toProperCase(languagesLocales[selectedLanguage].getDisplayName()));
        languageFlag.setImageResource(flags[selectedLanguage]);
    }

    public void switchDifficultyLeft() {
        if (selectedDifficulty == 0) selectedDifficulty = difficulties.length-1;
        else selectedDifficulty--;
        updateDifficultyLabel();
        updateTextList();
    }

    public void switchDifficultyRight() {
        if (selectedDifficulty == difficulties.length-1) selectedDifficulty = 0;
        else selectedDifficulty++;
        updateDifficultyLabel();
        updateTextList();
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

    class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader;
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            JSONObject text;
            try {
                text = receivedList.getJSONObject(groupPosition);
                selectedText = new DiktaplusText(text.getString("title"),
                        text.getString("content"),
                        text.getString("language"),
                        text.getString("difficulty"));
            } catch (JSONException e) {
                Log.e(TAG,"Error parsing received JSON");
            }
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
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.fragment_texts_child, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.best_score_label);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
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
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.fragment_texts_head, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.textsListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
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

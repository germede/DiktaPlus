package singularfactory.app.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
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
    ExpandableListView textsList;
    ExpandableListAdapter textsListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    PresenterTexts presenterTexts;

    public GameFragment() {
        // Required empty public constructor
    }
    private void prepareTextsList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        appCommon.getPresenterTexts().getTextsList(this,"GET Texts", Request.Method.GET,"http://192.168.1.106:8000/api/texts/ES/Easy","Cargando lista de textos...");
    }

    public void setTextsList(JSONArray texts) throws JSONException{
        List<String> listItemInfo;
        for (int i = 0; i < texts.length(); i++) {
            listItemInfo = new ArrayList<String>();
            listDataHeader.add(texts.getJSONObject(i).getString("content"));
            listItemInfo.add(Integer.toString(111));             //TODO: hacer funcionar el bestscore
            listDataChild.put(listDataHeader.get(i), listItemInfo);
        }

        textsList = (ExpandableListView)view.findViewById(R.id.textsList);
        textsListAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);
        textsList.setAdapter(textsListAdapter);
    }

    public void showErrorToast(String errorMessage) {
        Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_texts, container, false);
        prepareTextsList();
        return view;
    }

    class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
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
                convertView = infalInflater.inflate(R.layout.fragment_texts_listitem, null);
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
                convertView = infalInflater.inflate(R.layout.fragment_texts_listgroup, null);
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

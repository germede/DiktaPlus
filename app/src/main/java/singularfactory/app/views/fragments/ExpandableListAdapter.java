package singularfactory.app.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import singularfactory.app.R;
import singularfactory.app.models.Text;

import static singularfactory.app.views.fragments.BaseFragment.TAG;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private TextFragment textFragment;
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    public ExpandableListAdapter(Context context, TextFragment textFragment, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.textFragment = textFragment;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        JSONObject text;
        try {
            text = textFragment.receivedList.getJSONObject(groupPosition);
            textFragment.selectedText = new Text(text.getInt("id"),
                    text.getString("title"),
                    text.getString("content"),
                    text.getString("language"),
                    text.getString("difficulty"));
            textFragment.getBestScore();
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
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_text_child, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.best_score_label);

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
            convertView = infalInflater.inflate(R.layout.fragment_text_head, null);
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


package singularfactory.app.views.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import singularfactory.app.R;

public class FriendFragment extends BaseFragment {
    View view;
    SearchView searchBox;
    ImageView addFriendIcon;
    ListView friends;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranking, container, false);

        searchBox = (SearchView) view.findViewById(R.id.friends_searchbox);
        searchBox.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showToast("ADSFD");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                showToast("SFD");
                return false;
            }
        });
        addFriendIcon = (ImageView) view.findViewById(R.id.add_friend_icon);
        friends = (ListView) view.findViewById(R.id.friends);

        return view;
    }

    public void getUsersByUsername() {
        appCommon.getPresenterUser().getRanking(
                this,
                "Get users by username",
                Request.Method.GET,
                appCommon.getBaseURL()+"users/like/"+searchBox.getQuery(),
                "Getting users list...");
    }

    public void setUsersByUsername(JSONArray users) throws JSONException {
        List<String> usersList = new ArrayList<String>();;
        for (int i = 0; i < users.length(); i++) {
            usersList.add((i+1)+". "+users.getJSONObject(i).getString("username"));
        }
        friends.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.fragment_friend_item,usersList));
    }
}

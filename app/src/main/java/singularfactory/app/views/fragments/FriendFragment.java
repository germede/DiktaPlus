package singularfactory.app.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
    ListView friends;

    ImageButton unconfirmedFriendship;

    ArrayList<String> friendsList;

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
        view = inflater.inflate(R.layout.fragment_friend, container, false);

        searchBox = (SearchView) view.findViewById(R.id.friends_searchbox);
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 0) getUsersByUsername();
                else getFriends();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) getFriends();
                return false;
            }
        });
        friends = (ListView) view.findViewById(R.id.friends);
        getFriends();

        return view;
    }

    public void getFriends() {
        appCommon.getPresenterUser().getFriends(
                this,
                "Get friends",
                Request.Method.GET,
                appCommon.getBaseURL()+"users/friends/"+appCommon.getUser().getId(),
                "Getting friends...");
    }

    public void setFriends(JSONArray users) throws JSONException {
        friendsList = new ArrayList<String>();
        for (int i = 0; i < users.length(); i++) friendsList.add(users.getJSONObject(i).getString("username"));
        if (friendsList.contains(appCommon.getUser().getUsername())) friendsList.remove(appCommon.getUser().getUsername());
        friends.setAdapter(new UsersAdapter(getContext(),friendsList));
    }

    public void makeFriends(String friendUsername) {
        appCommon.getPresenterUser().makeFriends(
                this,
                "Make friends",
                Request.Method.PUT,
                appCommon.getBaseURL()+"users/friends/"+appCommon.getUser().getId()+"/"+friendUsername,
                "Making friends...");
    }

    public void deleteFriends(String friendUsername) {
        appCommon.getPresenterUser().deleteFriends(
                this,
                "Delete friends",
                Request.Method.DELETE,
                appCommon.getBaseURL()+"users/friends/"+appCommon.getUser().getId()+"/"+friendUsername,
                "Deleting friends...");
    }

    public void getUsersByUsername() {
        appCommon.getPresenterUser().getUsersByUsername(
                this,
                "Get users by username",
                Request.Method.GET,
                appCommon.getBaseURL()+"users/like/"+searchBox.getQuery(),
                "Getting users list...");
    }

    public void setUsersByUsername(JSONArray users) throws JSONException {
        ArrayList<String> usersList = new ArrayList<String>();;
        for (int i = 0; i < users.length(); i++) {
            usersList.add(users.getJSONObject(i).getString("username"));
        }
        if (usersList.contains(appCommon.getUser().getUsername())) usersList.remove(appCommon.getUser().getUsername());

        friends.setAdapter(new UsersAdapter(getContext(),usersList));
    }

    class UsersAdapter extends ArrayAdapter<String> {
        public UsersAdapter(Context context, ArrayList<String> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String string = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_friend_item, parent, false);
            }
            TextView username = (TextView) convertView.findViewById(R.id.friend_label);
            ImageButton addButton = (ImageButton) convertView.findViewById(R.id.add_friend_button);
            ImageButton infoButton = (ImageButton) convertView.findViewById(R.id.info_friend_button);
            ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_friend_button);


            if (friendsList.contains((string))) {
                addButton.setVisibility(View.GONE);
                infoButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFriends(string);
                    }
                });
            } else {
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeFriends(string);
                    }
                });
                infoButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
            username.setText(string);

            return convertView;
        }
    }
}

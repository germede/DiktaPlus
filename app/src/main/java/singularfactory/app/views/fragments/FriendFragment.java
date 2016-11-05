package singularfactory.app.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.models.User;

public class FriendFragment extends BaseFragment {
    View view;
    SearchView searchBox;
    ListView friends;
    JSONArray receivedList;
    ArrayList<String> friendsList;

    User selectedFriend;

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
        receivedList = users;
        friendsList = new ArrayList<>();
        for (int i = 0; i < users.length(); i++) friendsList.add(users.getJSONObject(i).getString("username"));
        if (friendsList.contains(appCommon.getUser().getUsername())) friendsList.remove(appCommon.getUser().getUsername());
        friends.setAdapter(new FriendsAdapter(getContext(),friendsList));
        searchBox.setQuery("",false);
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
        ArrayList<String> usersList = new ArrayList<>();
        for (int i = 0; i < users.length(); i++) {
            usersList.add(users.getJSONObject(i).getString("username"));
        }
        if (usersList.contains(appCommon.getUser().getUsername())) usersList.remove(appCommon.getUser().getUsername());
        friends.setAdapter(new FriendsAdapter(getContext(),usersList));
    }

    private void getFriendInfo(int id) {
        appCommon.getPresenterUser().getFriendInfo(
                this,
                "Get friend info",
                Request.Method.GET,
                appCommon.getBaseURL()+"users/"+id,
                "Getting friend info...");
    }

    public void setFriendInfo(JSONObject userJson) {
        try {
            selectedFriend = new User(userJson.getInt("id"),
                    userJson.getString("email"),
                    userJson.getString("username"),
                    userJson.getString("country"),
                    userJson.getInt("total_score"),
                    userJson.getInt("level"));
            FriendInfoDialog dialog =new FriendInfoDialog(getActivity());
            dialog.show();

        } catch (JSONException e) {
            Log.e(TAG,"Error parsing received JSON");
        }
    }

    class FriendsAdapter extends ArrayAdapter<String> {
        FriendsAdapter(Context context, ArrayList<String> users) {
            super(context, 0, users);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final String string = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_friend_item, parent, false);
            }
            TextView username = (TextView) convertView.findViewById(R.id.friend_label);
            ImageButton addButton = (ImageButton) convertView.findViewById(R.id.add_friend_button);
            ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_friend_button);

            if (friendsList.contains((string))) {
                addButton.setVisibility(View.GONE);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            getFriendInfo(receivedList.getJSONObject(position).getInt("id"));
                        } catch (JSONException e) {
                            Log.e(TAG,"JSON error");
                        }
                    }
                });
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
                deleteButton.setVisibility(View.GONE);
            }
            username.setText(string);

            return convertView;
        }
    }

    class FriendInfoDialog extends Dialog implements
            android.view.View.OnClickListener {
        private Button exit;

        FriendInfoDialog(Activity a) {super(a);}
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_friend_info);
            TextView country = (TextView)findViewById(R.id.friend_country_label);
            TextView level = (TextView)findViewById(R.id.friend_level_label);
            TextView totalScore = (TextView)findViewById(R.id.friend_total_score_label);
            ImageView flag = (ImageView)findViewById(R.id.friend_flag);

            setTitle(selectedFriend.getUsername());
            country.setText(new Locale("",selectedFriend.getCountry()).getDisplayCountry());
            level.setText(String.valueOf(selectedFriend.getLevel()));
            totalScore.setText(String.valueOf(selectedFriend.getTotalScore()));
            int drawableId = getResources()
                    .getIdentifier("flag_"+selectedFriend.getCountry().toLowerCase(), "drawable", getActivity().getPackageName());

            flag.setImageResource(drawableId);


            exit = (Button) findViewById(R.id.btn_exit);
            exit.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {dismiss();}
    }

}

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
    EditText cnt;
    EditText country;
    ImageView flag;
    ListView ranking;
    String selectedCountry;

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

        selectedCountry = "GB";
        ranking = (ListView) view.findViewById(R.id.ranking);
        flag = (ImageView) view.findViewById(R.id.ranking_flag);
        cnt = (EditText) view.findViewById(R.id.cnt_ranking_input);
        cnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s!= null && s.toString().length()>0) getRanking();
            }
        });
        country = (EditText) view.findViewById(R.id.country_ranking_input);
        country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    CountryPicker picker = CountryPicker.getInstance("Select Country", new CountryPickerListener() {
                        @Override
                        public void onSelectCountry(String name, String code) {
                            country.setText(name);
                            DialogFragment dialogFragment =
                                    (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker");
                            dialogFragment.dismiss();
                            int drawableId = getResources()
                                    .getIdentifier("flag_"+code.toLowerCase(), "drawable", getActivity().getPackageName());

                            flag.setImageResource(drawableId);
                            selectedCountry = code;
                            getRanking();
                        }
                    });
                    picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
                }
            }
        });
        getRanking();
        return view;
    }

    public void getRanking() {
        appCommon.getPresenterUser().getRanking(
                this,
                "Get ranking",
                Request.Method.GET,
                appCommon.getBaseURL()+"users/ranking/"+selectedCountry+"/"+cnt.getText(),
                "Getting ranking...");
    }

    public void setRanking(JSONArray users) throws JSONException {
        List<String> usersList = new ArrayList<String>();;
        for (int i = 0; i < users.length(); i++) {
            usersList.add((i+1)+". "+users.getJSONObject(i).getString("username"));
        }
        ranking.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,usersList));
    }
}

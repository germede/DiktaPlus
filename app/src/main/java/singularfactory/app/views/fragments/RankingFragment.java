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

public class RankingFragment extends BaseFragment {
    View view;
    EditText cnt;
    EditText country;
    ImageView flag;
    ListView ranking;
    String selectedCountry;

    public RankingFragment() {
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

        selectedCountry = "";
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
                if(s!= null && s.toString().length()>0 && !selectedCountry.equals("")) getRanking();
            }
        });
        country = (EditText) view.findViewById(R.id.country_ranking_input);
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCountryList();
            }
        });
        country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showCountryList();
            }
        });
        return view;
    }



    public void getRanking() {
        ranking.setAdapter(null);
        appCommon.getPresenterUser().getRanking(
                this,
                "Get ranking",
                Request.Method.GET,
                appCommon.getBaseURL()+"users/ranking/"+selectedCountry+"/"+cnt.getText(),
                "Getting ranking...");
    }

    public void setRanking(JSONArray users) throws JSONException {
        List<String> usersList = new ArrayList<>();
        for (int i = 0; i < users.length(); i++) {
            usersList.add((i+1)+". "+users.getJSONObject(i).getString("username")+" - "
                    +users.getJSONObject(i).getString("total_score"));
        }
        ranking.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,usersList));
    }

    private void showCountryList() {
        CountryPicker picker = CountryPicker.getInstance(getString(R.string.select_country), new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code) {
                country.setText(name);
                ((DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker")).dismiss();
                flag.setImageResource(getResources()
                        .getIdentifier("flag_"+code.toLowerCase(), "drawable", getActivity().getPackageName()));
                selectedCountry = code;
                getRanking();
            }
        });
        picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
    }
}

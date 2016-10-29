package singularfactory.app.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import singularfactory.app.R;
import singularfactory.app.models.Text;

public class RankingFragment extends BaseFragment {
    View view;
    EditText country;


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

        country = (EditText) view.findViewById(R.id.country_ranking_input);
        country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    CountryPicker picker = CountryPicker.getInstance("Select Country", new CountryPickerListener() {
                        @Override
                        public void onSelectCountry(String name, String code) {
                            country.setText(code);
                            DialogFragment dialogFragment =
                                    (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker");
                            dialogFragment.dismiss();

                        }
                    });
                    picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
                }
            }
        });
        return view;
    }
}

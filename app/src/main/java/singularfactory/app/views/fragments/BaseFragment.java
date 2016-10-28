package singularfactory.app.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import singularfactory.app.common.AppCommon;

public class BaseFragment extends Fragment {

    public final static String TAG = BaseFragment.class.getSimpleName();
    AppCommon appCommon;

    public void showErrorToast(String errorMessage) {
        Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    public String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCommon       = AppCommon.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    /********************/
    /** PUBLIC METHODS **/
    /********************/

}

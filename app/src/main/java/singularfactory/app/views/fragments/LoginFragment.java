package singularfactory.app.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import singularfactory.app.AppCommon;
import singularfactory.app.R;
import singularfactory.app.views.fragments.initializations.InitBaseExampleFragment;

/**
 * Created by Óscar Adae Rodríguez on 18/08/2016.
 */
public class LoginFragment extends BaseFragment implements InitBaseExampleFragment.InitBaseExampleListener {

    private final static String TAG = BaseExampleFragment.class.getSimpleName();

    private AppCompatActivity mActivity;
    private AppCommon appCommon;
    private FragmentManager fragmentManager;
    private View containerLayout;

    public InitBaseExampleFragment itemView;


    public static BaseExampleFragment newInstance() {

        BaseExampleFragment fragment = new BaseExampleFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {

        if (bundle != null) {
            //TODO Get values from bundle...
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        readBundle(getArguments());
        initialize(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //TODO Get container layout from activity
//        containerLayout = mActivity.findViewById(R.id.containerLayout);
    }

    private void initialize(View view) {
        appCommon       = AppCommon.getInstance();
        fragmentManager = mActivity.getSupportFragmentManager();

        itemView = new InitBaseExampleFragment(this);
        itemView.initialize(view);
        itemView.initializeActions();
        itemView.initializeCustomFonts();
        itemView.setInitBaseExampleListener(this);
    }

    /** PUBLIC METHODS **/

    /** END **/

    /** API CALLS **/

    /** END **/

    /** InitBaseExampleFragment.InitBaseExampleListener **/

    /*** END **/
}

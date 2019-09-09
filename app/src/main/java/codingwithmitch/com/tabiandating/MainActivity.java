package codingwithmitch.com.tabiandating;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import codingwithmitch.com.tabiandating.models.FragmentTag;
import codingwithmitch.com.tabiandating.models.Message;
import codingwithmitch.com.tabiandating.models.User;
import codingwithmitch.com.tabiandating.settings.SettingsFragment;
import codingwithmitch.com.tabiandating.util.PreferenceKeys;

public class MainActivity extends AppCompatActivity
        implements IMainActivity
        , BottomNavigationViewEx.OnNavigationItemSelectedListener
        , NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    //widgets
    private BottomNavigationViewEx mBottomNavigationViewEx;
    private ImageView mHeaderImage;
    private DrawerLayout mDrawerLayout;


    //Fragments
    private SettingsFragment mSettingsFragment;
    private AgreementFragment mAgreementFragment;
    private HomeFragment mHomeFragment;
    private SavedConnectionsFragment mSavedConnectionsFragment;
    private MessagesFragment mMessagesFragment;
    private ViewProfileFragment mViewProfileFragment;
    private ChatFragment mChatFragment;

    //vars
    private ArrayList<String> mFragmentTags = new ArrayList<>();
    private ArrayList<FragmentTag> mFragments = new ArrayList<>();
    private int mExitCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationViewEx = findViewById(R.id.bottom_nav_view);
        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(this);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        mHeaderImage = headerView.findViewById(R.id.header_image);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        initBottomNavigationView();
        isFirstLogin();
        setNavigationViewListener();
        setHeaderImage();
        init();

    }

    private void initBottomNavigationView() {
        Log.d(TAG, "initBottomNavigationView: initializing bottom navigation view");
        //mBottomNavigationViewEx.enableAnimation(false);


    }

    private void init() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content_frame, mHomeFragment, getString(R.string.tag_fragment_home));
            transaction.commit();
            mFragmentTags.add(getString(R.string.tag_fragment_home));
            mFragments.add(new FragmentTag(mHomeFragment, getString(R.string.tag_fragment_home)));
        } else {
            mFragmentTags.remove(getString(R.string.tag_fragment_home));
            mFragmentTags.add(getString(R.string.tag_fragment_home));
        }

        setFragmentVisibilities(getString(R.string.tag_fragment_home));

    }

    private void setFragmentVisibilities(String tagname){
        for(int i = 0; i < mFragments.size();i++){
            if(tagname.equals(mFragments.get(i).getTag())){
                //show
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show(mFragments.get(i).getFragment());
                transaction.commit();
            }else{
                //dont show
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(mFragments.get(i).getFragment());
                transaction.commit();
            }
        }
    }

    private void setNavigationViewListener() {
        Log.d(TAG, "setNavigationViewListener: initializing navigation drawer listener");
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setHeaderImage() {
        Log.d(TAG, "setHeaderImage: Setting header image for navigation drawer");
        Glide.with(this).load(R.drawable.couple).into(mHeaderImage);
    }

    public void isFirstLogin() {
        Log.d(TAG, "isFirstLogin: checking if this is the first login");
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstLogin = preferences.getBoolean(PreferenceKeys.FIRST_TIME_LOGIN, true);

        if (isFirstLogin) {
            Log.d(TAG, "isFirstLogin: Launching ALert Dialog");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.first_time_user_message));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: closing dialog");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(PreferenceKeys.FIRST_TIME_LOGIN, false);
                    editor.apply();
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.setIcon(R.drawable.tabian_dating);
            alertDialogBuilder.setTitle(" ");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void inflateViewProfileFragment(User user) {
        if (mViewProfileFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mViewProfileFragment).commitAllowingStateLoss();
        }
        mViewProfileFragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_user), user);
        mViewProfileFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mViewProfileFragment, getString(R.string.tag_fragment_view_profile));
        transaction.commit();
        mFragmentTags.add(getString(R.string.tag_fragment_view_profile));
        mFragments.add(new FragmentTag(mViewProfileFragment, getString(R.string.tag_fragment_view_profile)));

        setFragmentVisibilities(getString(R.string.tag_fragment_view_profile));
    }


    @Override
    public void onMessageSelected(Message message) {
        if(mChatFragment != null){
            getSupportFragmentManager().beginTransaction().remove(mChatFragment).commitAllowingStateLoss();

        }
        mChatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.intent_message), message);
        mChatFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content_frame, mChatFragment, getString(R.string.tag_fragment_chat));
        transaction.commit();
        mFragmentTags.add(getString(R.string.tag_fragment_chat));
        mFragments.add(new FragmentTag(mChatFragment, getString(R.string.tag_fragment_chat)));
        setFragmentVisibilities(getString(R.string.tag_fragment_chat));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                mFragments.clear();
                mFragmentTags = new ArrayList<>();
                init();
                break;
            case R.id.settings:
                Log.d(TAG, "onNavigationItemSelected: settings");
                if (mSettingsFragment == null) {
                    mSettingsFragment = new SettingsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mSettingsFragment, getString(R.string.tag_fragment_settings));
                    transaction.commit();
                    mFragmentTags.add(getString(R.string.tag_fragment_settings));
                    mFragments.add(new FragmentTag(mSettingsFragment, getString(R.string.tag_fragment_settings)));
                } else {
                    mFragmentTags.remove(getString(R.string.tag_fragment_settings));
                    mFragmentTags.add(getString(R.string.tag_fragment_settings));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_settings));

                break;
            case R.id.agreement:
                Log.d(TAG, "onNavigationItemSelected: agreement");
                if (mAgreementFragment == null) {
                    mAgreementFragment = new AgreementFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mAgreementFragment, getString(R.string.tag_fragment_agreement));
                    transaction.commit();
                    mFragmentTags.add(getString(R.string.tag_fragment_agreement));
                    mFragments.add(new FragmentTag(mAgreementFragment, getString(R.string.tag_fragment_agreement)));
                } else {
                    mFragmentTags.remove(getString(R.string.tag_fragment_agreement));
                    mFragmentTags.add(getString(R.string.tag_fragment_agreement));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_agreement));

                break;

            case R.id.bottom_nav_home:
                Log.d(TAG, "onNavigationItemSelected: HomeFragment");
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mHomeFragment, getString(R.string.tag_fragment_home));
                    transaction.commit();
                    menuItem.setChecked(true);
                    mFragmentTags.add(getString(R.string.tag_fragment_home));
                    mFragments.add(new FragmentTag(mHomeFragment, getString(R.string.tag_fragment_home)));
                } else {
                    mFragmentTags.remove(getString(R.string.tag_fragment_home));
                    mFragmentTags.add(getString(R.string.tag_fragment_home));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_home));
                break;
            case R.id.bottom_nav_connections:
                Log.d(TAG, "onNavigationItemSelected: ConnectionsFragment");
                if (mSavedConnectionsFragment == null) {
                    mSavedConnectionsFragment = new SavedConnectionsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mSavedConnectionsFragment, getString(R.string.tag_fragment_saved_connections));
                    transaction.commit();
                    menuItem.setChecked(true);
                    mFragmentTags.add(getString(R.string.tag_fragment_saved_connections));
                    mFragments.add(new FragmentTag(mSavedConnectionsFragment, getString(R.string.tag_fragment_saved_connections)));
                } else {
                    mFragmentTags.remove(getString(R.string.tag_fragment_saved_connections));
                    mFragmentTags.add(getString(R.string.tag_fragment_saved_connections));
                }

                setFragmentVisibilities(getString(R.string.tag_fragment_saved_connections));

                break;
            case R.id.bottom_nav_messages:
                menuItem.setChecked(true);
                Log.d(TAG, "onNavigationItemSelected: MessagesFragment");
                if (mMessagesFragment == null) {
                    mMessagesFragment = new MessagesFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.main_content_frame, mMessagesFragment, getString(R.string.tag_fragment_messages));
                    transaction.commit();
                    menuItem.setChecked(true);
                    mFragmentTags.add(getString(R.string.tag_fragment_messages));
                    mFragments.add(new FragmentTag(mMessagesFragment, getString(R.string.tag_fragment_messages)));
                } else {
                    mFragmentTags.remove(getString(R.string.tag_fragment_messages));
                    mFragmentTags.add(getString(R.string.tag_fragment_messages));
                }
                setFragmentVisibilities(getString(R.string.tag_fragment_messages));
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}

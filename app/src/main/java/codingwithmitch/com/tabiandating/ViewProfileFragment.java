package codingwithmitch.com.tabiandating;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.HashSet;
import java.util.Set;

import codingwithmitch.com.tabiandating.models.User;
import codingwithmitch.com.tabiandating.util.PreferenceKeys;
import codingwithmitch.com.tabiandating.util.Resources;
import de.hdodenhof.circleimageview.CircleImageView;


public class ViewProfileFragment extends Fragment implements OnLikeListener,View.OnClickListener{

    private static final String TAG = "ViewProfileFragment";

    //widgets
    private TextView mFragmentHeading, mName, mGender, mInterestedIn, mStatus;
    private LikeButton mLikeButton;
    private RelativeLayout mBackArrow;
    private CircleImageView mProfileImage;


    //vars
    private User mUser;
    private IMainActivity mInterface;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            mUser = bundle.getParcelable(getString(R.string.intent_user));
            assert mUser != null;
            Log.d(TAG, "onCreate: got incoming bundle: " + mUser.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        Log.d(TAG, "onCreateView: started.");
        mBackArrow = view.findViewById(R.id.back_arrow);
        mFragmentHeading = view.findViewById(R.id.fragment_heading);
        mProfileImage = view.findViewById(R.id.profile_image);
        mLikeButton = view.findViewById(R.id.heart_button);
        mName = view.findViewById(R.id.name);
        mGender = view.findViewById(R.id.gender);
        mInterestedIn = view.findViewById(R.id.interested_in);
        mStatus = view.findViewById(R.id.status);
        mLikeButton.setOnLikeListener(this);
        checkIfConnected();
        setBackgroundImage(view);
        init();
        return view;
    }

    private  void init(){
        Log.d(TAG, "init: initializing ViewProfileFragment.");
        if(mUser != null){
            Glide.with(getActivity()).load(mUser.getProfile_image()).into(mProfileImage);
            mName.setText(mUser.getName());
            mGender.setText(mUser.getGender());
            mInterestedIn.setText(mUser.getInterested_in());
            mStatus.setText(mUser.getStatus());
        }
    }

    private void setBackgroundImage(View view){
        ImageView backgroundImage = view.findViewById(R.id.background);
        Glide.with(getActivity()).load(Resources.BACKGROUND_HEARTS).into(backgroundImage);


    }
    private void checkIfConnected(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> savedNames = preferences.getStringSet(PreferenceKeys.SAVED_CONNECTIONS,new HashSet<String>());

        savedNames.add(mUser.getName());
        if(savedNames.contains(mUser.getName())){
            mLikeButton.setLiked(true);
        }else{
            mLikeButton.setLiked(false);
        }
    }


    @Override
    public void liked(LikeButton likeButton) {
        Log.d(TAG, "liked: liked");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> savedNames = preferences.getStringSet(PreferenceKeys.SAVED_CONNECTIONS,new HashSet<String>());

        savedNames.add(mUser.getName());
        editor.putString(PreferenceKeys.SAVED_CONNECTIONS, String.valueOf(savedNames));
        editor.apply();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        Log.d(TAG, "unLiked: unliked");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> savedNames = preferences.getStringSet(PreferenceKeys.SAVED_CONNECTIONS,new HashSet<String>());

        editor.remove(PreferenceKeys.SAVED_CONNECTIONS);
        editor.apply();
        editor.putString(PreferenceKeys.SAVED_CONNECTIONS, String.valueOf(savedNames));
        editor.apply();

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: clicked.");

        if(view.getId() == R.id.back_arrow){
            Log.d(TAG, "onClick: navigating back.");
            mInterface.onBackPressed();


        }
    }


}

















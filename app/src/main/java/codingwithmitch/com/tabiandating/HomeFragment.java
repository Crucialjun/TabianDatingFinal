package codingwithmitch.com.tabiandating;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import codingwithmitch.com.tabiandating.models.User;
import codingwithmitch.com.tabiandating.util.Users;

import static java.util.Collections.*;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    //constants
    private static final int NUM_COLUMNS = 2;

    //widgets
    private RecyclerView mRecyclerView;

    //vars
    private ArrayList<User> mMatches = new ArrayList<>();
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private MainRecyclerViewAdapter mMainRecyclerViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d(TAG, "onCreateView: Started");
        mRecyclerView = view.findViewById(R.id.recycler_view);
        findMatches();
        return view;
    }

    private void findMatches() {
        Users users = new Users();
        if(mMatches != null){
            mMatches.clear();
        }

        addAll(mMatches, users.USERS);

        initRecyclerView();

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        mStaggeredGridLayoutManager =
                new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mMainRecyclerViewAdapter = new MainRecyclerViewAdapter(getActivity(),mMatches);
        mRecyclerView.setAdapter(mMainRecyclerViewAdapter);

    }
}

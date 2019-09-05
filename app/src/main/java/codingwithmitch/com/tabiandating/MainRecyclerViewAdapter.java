package codingwithmitch.com.tabiandating;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import codingwithmitch.com.tabiandating.models.User;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MainRecyclerViewAdapter";

    //vars
    private ArrayList<User> mUsers = new ArrayList<>();
    private Context mContext;
    private IMainActivity mInterface;

    public MainRecyclerViewAdapter(Context context,ArrayList<User> users) {
        mUsers = users;
        mContext = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.layout_main_feed, viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: Called");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(mUsers.get(i).getProfile_image())
                .apply(requestOptions).into(viewHolder.image);

        viewHolder.name.setText(mUsers.get(i).getName());
        viewHolder.interested_in.setText(mUsers.get(i).getInterested_in());
        viewHolder.status.setText(mUsers.get(i).getStatus());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mUsers.get(i).getName());
                mInterface.inflateViewProfileFragment(mUsers.get(i));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInterface = (IMainActivity) mContext;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView interested_in;
        TextView status;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            interested_in = itemView.findViewById(R.id.interested_in);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

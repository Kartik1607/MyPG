package stfo.com.mypg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import stfo.com.mypg.Adapters.PG_Overview;
import stfo.com.mypg.pojo.PG;

/**
 * Created by Kartik Sharma on 20/02/17.
 */
public class Fragement_PG_Overview extends Fragment {

    private Context context;
    RecyclerView recyclerView;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<PG,PG_Overview> mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pg_overview, container, false);
        init(v);
        return v;
    }

    private void init(View v){
        context = getContext();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child(Constants.CHILD_PG);
        mAdapter = new FirebaseRecyclerAdapter<PG, PG_Overview>(PG.class,R.layout.layout_pg_overview,PG_Overview.class,mRef) {
            @Override
            protected void populateViewHolder(PG_Overview viewHolder, PG model, int position) {
                viewHolder.setPrice(context, model.getPrice());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setImage(context, model.getImage());
            }

            @Override
            public PG_Overview onCreateViewHolder(ViewGroup parent, int viewType) {
                PG_Overview viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new PG_Overview.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PG item = getItem(position);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.KEY_PG_LOCATION, item.getLocation());
                        bundle.putString(Constants.KEY_PG_FACILITIES, item.getFacilities());
                        bundle.putLong(Constants.KEY_PG_PRICE, item.getPrice());
                        bundle.putString(Constants.KEY_PG_PHONE, item.getPhone());
                        bundle.putString(Constants.KEY_PG_IMAGE, item.getImage());
                        bundle.putDouble(Constants.KEY_PG_LATITUDE, item.getLatitude());
                        bundle.putDouble(Constants.KEY_PG_LONGITIUDE, item.getLongitude());
                        bundle.putString(Constants.KEY_PG_ADDRESS, item.getAddress());
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });
                return viewHolder;
            }
        };
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
    }
}

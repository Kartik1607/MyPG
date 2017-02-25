package stfo.com.mypg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import stfo.com.mypg.Adapters.ComplaintAdapter;
import stfo.com.mypg.Adapters.PaymentAdapter;
import stfo.com.mypg.pojo.Complaint;
import stfo.com.mypg.pojo.Payment;

/**
 * Created by Kartik Sharma on 25/02/17.
 */
public class Fragment_Complaints extends Fragment {

    private RecyclerView recyclerView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pg_overview, container,false);
        init(v);
        return v;
    }

    private void init(View v) {
        context = getContext();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child(Constants.CHILD_COMPLAINTS).child(
                FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")
        );

        FirebaseRecyclerAdapter<Complaint,ComplaintAdapter> mAdapter = new FirebaseRecyclerAdapter<Complaint,ComplaintAdapter>(Complaint.class,R.layout.layout_complaint, ComplaintAdapter.class,mRef) {


            @Override
            protected void populateViewHolder(ComplaintAdapter viewHolder, Complaint model, int position) {
                viewHolder.setData(model.getDate(), model.getLastMessage(), model.getStatus());
            }

            @Override
            public ComplaintAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                ComplaintAdapter holder =  super.onCreateViewHolder(parent, viewType);
                holder.setOnClickListener(new ComplaintAdapter.ClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Complaint item = getItem(position);
                        /* Start Chat Activity Here */
                    }
                });
                return holder;
            }
        };
        recyclerView.setAdapter(mAdapter);

    }
}

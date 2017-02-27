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
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import stfo.com.mypg.Adapters.Complaint_Admin_Adapter;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class Fragment_admin_Complaint extends Fragment {

    private RecyclerView recyclerView;
    private Button button_complaint;
    Complaint_Admin_Adapter adapter;
    Context context;
    DatabaseReference mRef;
    ArrayList<String> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_complaint, container, false);
        init(v);
        return v;
    }

    private void init(View v){
        data = new ArrayList<>();
        adapter = new Complaint_Admin_Adapter(getActivity(), data);
        context = getContext();
        button_complaint = (Button) v.findViewById(R.id.button_new_complaint);
        button_complaint.setVisibility(View.GONE);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child(Constants.CHILD_COMPLAINTS);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                for(DataSnapshot c : dataSnapshot.getChildren()){
                    data.add(c.getKey());
                }
                adapter.setData(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

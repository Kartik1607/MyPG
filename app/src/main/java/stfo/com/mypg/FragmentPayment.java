package stfo.com.mypg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import stfo.com.mypg.Adapters.PaymentAdapter;
import stfo.com.mypg.pojo.Payment;

/**
 * Created by Kartik Sharma on 23/02/17.
 */
public class FragmentPayment extends Fragment {

    RecyclerView recyclerView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pg_overview, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        context = getContext();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child(Constants.CHILD_PAYMENTS).child(
                FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",")
        );

        FirebaseRecyclerAdapter<Payment, PaymentAdapter> mAdapter = new FirebaseRecyclerAdapter<Payment, PaymentAdapter>(Payment.class, R.layout.layout_pg_payment, PaymentAdapter.class, mRef) {


            @Override
            protected void populateViewHolder(PaymentAdapter viewHolder, Payment model, int position) {
                viewHolder.setData(model.getAmount(), model.getStatus().intValue(), model.getDate(), context);
            }
        };
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
    }

    private void showSnackbar(int id) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), id, Snackbar.LENGTH_LONG).show();
    }
}

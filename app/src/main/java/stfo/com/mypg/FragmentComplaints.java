package stfo.com.mypg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import stfo.com.mypg.Adapters.ComplaintAdapter;
import stfo.com.mypg.pojo.ChatMessage;
import stfo.com.mypg.pojo.Complaint;

/**
 * Created by Kartik Sharma on 25/02/17.
 */
public class FragmentComplaints extends Fragment {

    private RecyclerView recyclerView;
    private Button button_complaint;
    Context context;
    DatabaseReference mRef;
    FirebaseRecyclerAdapter<Complaint, ComplaintAdapter> mAdapter;
    String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        email = getArguments().getString(Constants.KEY_EMAIL);
        View v = inflater.inflate(R.layout.fragment_complaint, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        context = getContext();
        button_complaint = (Button) v.findViewById(R.id.button_new_complaint);
        if (!Constants.isNormalUser)
            button_complaint.setVisibility(View.GONE);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mRef = database.getReference().child(Constants.CHILD_COMPLAINTS).child(
                email
        );
        mAdapter = new FirebaseRecyclerAdapter<Complaint, ComplaintAdapter>(Complaint.class, R.layout.layout_complaint, ComplaintAdapter.class, mRef) {


            @Override
            protected void populateViewHolder(ComplaintAdapter viewHolder, Complaint model, int position) {
                viewHolder.setData(model.getDate(), model.getMessage());
            }

            @Override
            public ComplaintAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                ComplaintAdapter holder = super.onCreateViewHolder(parent, viewType);
                holder.setOnClickListener(new ComplaintAdapter.ClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Complaint item = getItem(position);
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra(Constants.KEY_CHAT, item.getComplaintID());
                        startActivity(intent);
                    }
                });
                return holder;
            }
        };

        recyclerView.setAdapter(mAdapter);


        button_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.new_complaint_title);
                builder.setMessage(R.string.new_complaint_message);
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().length() == 0)
                            return;

                        String message = input.getText().toString();

                        DatabaseReference ref = mRef.push();
                        ref.setValue(
                                new Complaint(
                                        ref.getKey(),
                                        getDate(),
                                        message
                                )
                        );

                        DatabaseReference refChat = FirebaseDatabase.getInstance().getReference()
                                .child(Constants.CHILD_CHATS).child(ref.getKey());

                        refChat.push().setValue(new ChatMessage(
                                getString(R.string.new_complaint_default, message),
                                Constants.isNormalUser
                        ));


                    }
                });
                builder.setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String format = simpleDateFormat.format(new Date());
        return format;
    }
}

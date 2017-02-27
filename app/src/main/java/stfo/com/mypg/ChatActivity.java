package stfo.com.mypg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import stfo.com.mypg.Adapters.ChatAdapter;
import stfo.com.mypg.pojo.ChatMessage;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private Button button_resolved;
    private ImageButton button_send;
    private Context context;
    private EditText editText;

    private FirebaseRecyclerAdapter<ChatMessage, ChatAdapter> recyclerAdapter;
    private FirebaseDatabase database;
    private DatabaseReference ref, refComplaint;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private void init() {

        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        button_resolved = (Button) findViewById(R.id.button_complaint_resolved);
        button_send = (ImageButton) findViewById(R.id.button_send);

        editText = (EditText) findViewById(R.id.editText_message);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child(Constants.CHILD_CHATS).child(getIntent().getStringExtra(Constants.KEY_CHAT));

        recyclerAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatAdapter>(ChatMessage.class, R.layout.layout_complaint_chat, ChatAdapter.class, ref) {
            @Override
            protected void populateViewHolder(ChatAdapter viewHolder, ChatMessage model, int position) {
                viewHolder.bindData(context, model.getMessage(), model.getUser());
                recyclerView.smoothScrollToPosition(recyclerAdapter.getItemCount());
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        button_send.setOnClickListener(this);
        button_resolved.setOnClickListener(this);


        if(!Constants.isNormalUser)
            button_resolved.setVisibility(View.VISIBLE);

        recyclerView.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send :

                if(editText.getText().length() != 0) {
                    ref.push().setValue(new ChatMessage(
                            editText.getText().toString(), Constants.isNormalUser
                    ));
                    recyclerView.scrollToPosition(recyclerAdapter.getItemCount() );
                }
                editText.setText("");

                break;
            case R.id.button_complaint_resolved :
                break;

        }
    }
}

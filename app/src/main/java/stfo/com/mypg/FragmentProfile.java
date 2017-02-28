package stfo.com.mypg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import stfo.com.mypg.pojo.User;

/**
 * Created by Kartik Sharma on 22/02/17.
 */
public class FragmentProfile extends Fragment {

    public interface LogoutListener {
        void OnUserLoggedOut();
    }

    private FirebaseUser fUser;
    private User user;
    private ImageView imageView;
    private FloatingActionButton editButton;
    private Button button_save, button_logout;
    private EditText editText_Name, editText_Company, editText_PG, editText_Email;
    private static String NAME, COMPANY;
    private static boolean EDITABLE;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EDITABLE = button_save.getVisibility() == View.VISIBLE;
        NAME = editText_Name.getText().toString();
        COMPANY = editText_Company.getText().toString();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEditable(false);
        if (EDITABLE) {
            button_save.setVisibility(View.VISIBLE);
            editText_Name.requestFocus();
            setEditable(true);
            editText_Name.setText(NAME);
            editText_Company.setText(COMPANY);
        } else {
            loadUserDetails();
            setEditable(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        init(v);
        user = new User();
        return v;
    }

    private void init(View v) {
        editText_Name = (EditText) v.findViewById(R.id.editText_Name);
        editText_Company = (EditText) v.findViewById(R.id.editText_Company);
        editText_PG = (EditText) v.findViewById(R.id.editText_CurrentPG);
        editText_Email = (EditText) v.findViewById(R.id.editText_EMail);

        button_save = (Button) v.findViewById(R.id.button_save);
        button_logout = (Button) v.findViewById(R.id.button_log_out);
        editButton = (FloatingActionButton) v.findViewById(R.id.fab);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button_save.getVisibility() == View.VISIBLE) {
                    setEditable(false);
                    loadUserDetails();
                    button_save.setVisibility(View.GONE);
                    ;
                } else {
                    button_save.setVisibility(View.VISIBLE);
                    editText_Name.requestFocus();
                    setEditable(true);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveUserDetails();
                button_save.setVisibility(View.GONE);
                setEditable(false);
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        editText_PG.setKeyListener(null); //Not editable by user.
        editText_Email.setKeyListener(null); //Not editable by user.


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        imageView = (ImageView) v.findViewById(R.id.imageView_profile);
        Glide.with(getContext()).load(R.drawable.backdrop).centerCrop().into(imageView);
    }

    private void setEditable(boolean value) {
        if (value) {
            editText_Name.setKeyListener((KeyListener) editText_Name.getTag());
            editText_Company.setKeyListener((KeyListener) editText_Company.getTag());
        } else {
            if (editText_Name.getKeyListener() == null)
                return;
            editText_Name.setTag(editText_Name.getKeyListener());
            editText_Name.setKeyListener(null);

            editText_Company.setTag(editText_Company.getKeyListener());
            editText_Company.setKeyListener(null);
        }

    }

    private void loadUserDetails() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child(Constants.CHILD_USERS).child(
                fUser.getEmail().replace(".", ",")
        );

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                editText_Name.setText(user.getName());
                editText_Company.setText(user.getCompany());
                editText_PG.setText(user.getCurrentPG());
                editText_Email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addListenerForSingleValueEvent(
                listener
        );
    }

    private void saveUserDetails() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child(Constants.CHILD_USERS);
        reference.child(
                FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",")
        ).setValue(new User(editText_Name.getText().toString(),
                editText_Email.getText().toString(),
                editText_Company.getText().toString(),
                editText_PG.getText().toString()));

    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (getActivity() instanceof LogoutListener) {
                            ((LogoutListener) getActivity()).OnUserLoggedOut();
                        }
                    }
                });
    }
}

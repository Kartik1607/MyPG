package stfo.com.mypg;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Arrays;

import stfo.com.mypg.Adapters.Complaint_Admin_Adapter;
import stfo.com.mypg.Util.Utils;
import stfo.com.mypg.pojo.User;

public class MainActivity extends AppCompatActivity implements Fragment_profile.LogoutListener,
        Complaint_Admin_Adapter.OnAdminComplaintSelected
{

    //TODO : Strings in strimgs.xml.
    //TODO : Save when screen rotates.
    //TODO : Content Description
    //TODO : Activity Animations

    private static final int RC_SIGN_IN = 123;

    private final String TAG_PROFILE = "TP", TAG_PAYMENT = "TPAY", TAG_COMPLAINT = "TC", TAG_OVERVIEW = "TO";
    FragmentManager fragmentManager;
    Fragment pg_overview_fragment, profile_fragment, payment_fragment, complaint_fragment, complaint_admin;
    FirebaseAuth auth;
    View v_activity;
    BottomBar barAnonymous, barLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            init();
            fragmentManager.beginTransaction()
                    .add(R.id.frame, pg_overview_fragment)
                    .commit();
            setUpListener();
        }else{
            init();
            pg_overview_fragment = fragmentManager.findFragmentByTag(TAG_OVERVIEW);
            profile_fragment = fragmentManager.findFragmentByTag(TAG_PROFILE);
            payment_fragment = fragmentManager.findFragmentByTag(TAG_PAYMENT);
            complaint_fragment = fragmentManager.findFragmentByTag(TAG_COMPLAINT);
            setUpListener();
        }

        if(auth.getCurrentUser() != null){
            Constants.IS_SIGNED_IN = true;
            loadPG();
            barAnonymous.setVisibility(View.GONE);
            barLoggedIn.setVisibility(View.VISIBLE);
        }else{
            barLoggedIn.setVisibility(View.GONE);
            barAnonymous.setVisibility(View.VISIBLE);
        }

    }

    private void setUpListener() {
        final BottomBar barA =  barAnonymous;
        final BottomBar barL =  barLoggedIn;
        barA.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if(tabId == R.id.tab_login){
                    startActivityForResult(
                            AuthUI.getInstance().createSignInIntentBuilder()
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                    )).build(),
                            RC_SIGN_IN);
                }
            }
        });
        barL.setDefaultTab(R.id.tab_home);
        barL.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if(tabId == R.id.tab_home ){
                    if(pg_overview_fragment == null){
                        pg_overview_fragment = new Fragement_PG_Overview();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, pg_overview_fragment, TAG_OVERVIEW).commit();
                }else if(tabId == R.id.tab_profile){
                    if(profile_fragment == null){
                        profile_fragment = new Fragment_profile();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, profile_fragment, TAG_PROFILE).commit();
                }else if(tabId == R.id.tab_payments ){
                    if( Constants.NO_PG.equals(Constants.CURRENT_PG)){
                        showSnackbar(R.string.no_pg);
                        return;
                    }
                    if(payment_fragment == null){
                        payment_fragment = new Fragment_Payment();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, payment_fragment, TAG_PAYMENT).commit();
                }else if(tabId == R.id.tab_complaints ){
                    if(!Constants.isNormalUser){
                        if(complaint_admin == null){
                            complaint_admin = new Fragment_admin_Complaint();
                        }
                        fragmentManager.beginTransaction().replace(R.id.frame,complaint_admin)
                                .commit();
                        return;
                    }
                    if( Constants.NO_PG.equals(Constants.CURRENT_PG)){
                        showSnackbar(R.string.no_pg);
                        return;
                    }
                    if(complaint_fragment == null){
                        complaint_fragment = new Fragment_Complaints();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEY_EMAIL, Utils.CURRENT_EMAIL);
                    complaint_fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.frame, complaint_fragment, TAG_COMPLAINT).commit();
                }
            }
        });
    }

    private void init(){
        v_activity = findViewById(R.id.view_main_activity);
        barAnonymous = (BottomBar) findViewById(R.id.bottomBarAnonymous);
        barLoggedIn = (BottomBar) findViewById(R.id.bottomBarLoggedIn);
        pg_overview_fragment = new Fragement_PG_Overview();
        fragmentManager = getSupportFragmentManager();
        auth = FirebaseAuth.getInstance();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                Constants.IS_SIGNED_IN = true;
                barAnonymous.setVisibility(View.GONE);
                barLoggedIn.setVisibility(View.VISIBLE);
                ((BottomBar)barLoggedIn).setDefaultTab(R.id.tab_home);
                loadPG();
                showSnackbar(R.string.sign_in_successful);
                return;
            } else {
                if (response == null) {
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_error);
        }
    }

    private void loadPG(){
        startService(new Intent(this, NotificationService.class));
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHILD_USERS);
        final DatabaseReference refAdmin = FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHILD_ADMIN);
        if(auth.getCurrentUser() != null){
            final FirebaseUser u = auth.getCurrentUser();
            Utils.CURRENT_EMAIL = u.getEmail().replace(".",",");

            refAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(u.getEmail().replace(".",","))){
                        Constants.isNormalUser = false;
                    }else{
                        Constants.isNormalUser = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(u.getEmail().replace(".",","))){
                        User user = dataSnapshot.child(u.getEmail().replace(".",",")).getValue(User.class);
                        Constants.CURRENT_PG = user.getCurrentPG();
                    }else{
                        ref.child(u.getEmail().replace(".",",")).setValue(
                                new User(u.getDisplayName(),u.getEmail(),"None","None")
                        );
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    private void showSnackbar(int id){
        Snackbar.make(v_activity,id,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void OnUserLoggedOut() {
        barLoggedIn.setVisibility(View.GONE);
        barAnonymous.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, pg_overview_fragment,TAG_OVERVIEW).commit();

        payment_fragment = null;
        profile_fragment = null;
        Constants.IS_SIGNED_IN = false;
        Constants.CURRENT_PG = Constants.NO_PG;
        stopService(new Intent(this, NotificationService.class));
        showSnackbar(R.string.sign_out_successful);
    }

    @Override
    public void ComplaintSelectedForUser(String userEmail) {
        if(complaint_fragment == null)
            complaint_fragment = new Fragment_Complaints();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_EMAIL, userEmail);
        complaint_fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame, complaint_fragment,TAG_COMPLAINT).commit();
    }
}

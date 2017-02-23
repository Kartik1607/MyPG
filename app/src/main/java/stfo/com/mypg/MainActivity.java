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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Arrays;

import stfo.com.mypg.pojo.User;

public class MainActivity extends AppCompatActivity implements Fragment_profile.LogoutListener{

    //TODO : Strings in strimgs.xml.
    //TODO : Save when screen rotates.
    //TODO : Content Description
    //TODO : Activity Animations

    private static final int RC_SIGN_IN = 123;

    FragmentManager fragmentManager;
    Fragment pg_overview_fragment, profile_fragment;
    FirebaseAuth auth;
    View barAnonymous, barLoggedIn, v_activity;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpListener();
    }

    private void setUpListener() {
        BottomBar barA = (BottomBar) barAnonymous;
        BottomBar barL = (BottomBar) barLoggedIn;
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
                if(tabId == R.id.tab_home && currentFragment!=pg_overview_fragment){
                    if(pg_overview_fragment == null){
                        pg_overview_fragment = new Fragement_PG_Overview();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, pg_overview_fragment).commit();
                    currentFragment = pg_overview_fragment;
                }else if(tabId == R.id.tab_profile && currentFragment!=profile_fragment){
                    if(profile_fragment == null){
                        profile_fragment = new Fragment_profile();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, profile_fragment).commit();
                    currentFragment = profile_fragment;
                }
            }
        });
    }

    private void init(){
        v_activity = findViewById(R.id.view_main_activity);
        barAnonymous = findViewById(R.id.bottomBarAnonymous);
        barLoggedIn = findViewById(R.id.bottomBarLoggedIn);
        pg_overview_fragment = new Fragement_PG_Overview();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame, pg_overview_fragment)
                .commit();
        currentFragment = pg_overview_fragment;
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            Constants.IS_SIGNED_IN = true;
            barAnonymous.setVisibility(View.GONE);
            barLoggedIn.setVisibility(View.VISIBLE);
        }else{
            barLoggedIn.setVisibility(View.GONE);
            barAnonymous.setVisibility(View.VISIBLE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                barAnonymous.setVisibility(View.GONE);
                barLoggedIn.setVisibility(View.VISIBLE);
                ((BottomBar)barLoggedIn).setDefaultTab(R.id.tab_home);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child(Constants.CHILD_USERS).child(
                                response.getEmail().replace('.',',')
                        );
                if(auth.getCurrentUser() != null){
                    FirebaseUser u = auth.getCurrentUser();
                    ref.setValue(new User(u.getDisplayName(),u.getEmail()," ", " "));
                }
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


    private void showSnackbar(int id){
        Snackbar.make(v_activity,id,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void OnUserLoggedOut() {
        barLoggedIn.setVisibility(View.GONE);
        barAnonymous.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, pg_overview_fragment).commit();

        Constants.IS_SIGNED_IN = false;
        showSnackbar(R.string.sign_out_successful);
    }
}

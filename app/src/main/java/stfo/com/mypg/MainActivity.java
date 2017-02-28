package stfo.com.mypg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

import stfo.com.mypg.Adapters.ComplaintAdminAdapter;
import stfo.com.mypg.Util.Utils;
import stfo.com.mypg.pojo.User;

public class MainActivity extends AppCompatActivity implements FragmentProfile.LogoutListener,
        ComplaintAdminAdapter.OnAdminComplaintSelected {
    private static final int RC_SIGN_IN = 123;

    private final String TAG_PROFILE = "TP", TAG_PAYMENT = "TPAY", TAG_COMPLAINT = "TC", TAG_OVERVIEW = "TO";
    FragmentManager fragmentManager;
    Fragment pgOverviewFragment, profileFragment, paymentFragment, complaintFragment, complaintAdmin;
    FirebaseAuth auth;
    View v_activity;
    BottomBar barAnonymous, barLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.xml.activity_open_translate, R.xml.activity_close_scale);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            init();
            fragmentManager.beginTransaction()
                    .add(R.id.frame, pgOverviewFragment)
                    .commit();
            setUpListener();
        } else {
            init();
            pgOverviewFragment = fragmentManager.findFragmentByTag(TAG_OVERVIEW);
            profileFragment = fragmentManager.findFragmentByTag(TAG_PROFILE);
            paymentFragment = fragmentManager.findFragmentByTag(TAG_PAYMENT);
            complaintFragment = fragmentManager.findFragmentByTag(TAG_COMPLAINT);
            setUpListener();
        }

        if (auth.getCurrentUser() != null) {
            Constants.IS_SIGNED_IN = true;
            loadPG();
            barAnonymous.setVisibility(View.GONE);
            barLoggedIn.setVisibility(View.VISIBLE);
        } else {
            barLoggedIn.setVisibility(View.GONE);
            barAnonymous.setVisibility(View.VISIBLE);
        }

    }

    private void setUpListener() {
        final BottomBar barA = barAnonymous;
        final BottomBar barL = barLoggedIn;
        barA.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_login) {
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
                if (tabId == R.id.tab_home) {
                    if (pgOverviewFragment == null) {
                        pgOverviewFragment = new FragementPGOverview();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, pgOverviewFragment, TAG_OVERVIEW).commit();
                } else if (tabId == R.id.tab_profile) {
                    if (profileFragment == null) {
                        profileFragment = new FragmentProfile();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, profileFragment, TAG_PROFILE).commit();
                } else if (tabId == R.id.tab_payments) {
                    if (Constants.NO_PG.equals(Constants.CURRENT_PG)) {
                        showSnackbar(R.string.no_pg);
                        return;
                    }
                    if (paymentFragment == null) {
                        paymentFragment = new FragmentPayment();
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame, paymentFragment, TAG_PAYMENT).commit();
                } else if (tabId == R.id.tab_complaints) {
                    if (!Constants.isNormalUser) {
                        if (complaintAdmin == null) {
                            complaintAdmin = new FragmentAdminComplaint();
                        }
                        fragmentManager.beginTransaction().replace(R.id.frame, complaintAdmin)
                                .commit();
                        return;
                    }
                    if (Constants.NO_PG.equals(Constants.CURRENT_PG)) {
                        showSnackbar(R.string.no_pg);
                        return;
                    }
                    if (complaintFragment == null) {
                        complaintFragment = new FragmentComplaints();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEY_EMAIL, Utils.CURRENT_EMAIL);
                    complaintFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.frame, complaintFragment, TAG_COMPLAINT).commit();
                }
            }
        });
    }

    private void init() {
        v_activity = findViewById(R.id.view_main_activity);
        barAnonymous = (BottomBar) findViewById(R.id.bottomBarAnonymous);
        barLoggedIn = (BottomBar) findViewById(R.id.bottomBarLoggedIn);
        pgOverviewFragment = new FragementPGOverview();
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
                ((BottomBar) barLoggedIn).setDefaultTab(R.id.tab_home);
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

    private void loadPG() {
        startService(new Intent(this, NotificationService.class));
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHILD_USERS);
        final DatabaseReference refAdmin = FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHILD_ADMIN);
        if (auth.getCurrentUser() != null) {
            final FirebaseUser u = auth.getCurrentUser();
            Utils.CURRENT_EMAIL = u.getEmail().replace(".", ",");

            refAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(u.getEmail().replace(".", ","))) {
                        Constants.isNormalUser = false;
                    } else {
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
                    if (dataSnapshot.hasChild(u.getEmail().replace(".", ","))) {
                        User user = dataSnapshot.child(u.getEmail().replace(".", ",")).getValue(User.class);
                        Constants.CURRENT_PG = user.getCurrentPG();
                    } else {
                        String displayName = u.getDisplayName();

                        //BUG IN FIREBASE, displayname returns null.

                        ref.child(u.getEmail().replace(".", ",")).setValue(
                                new User(displayName, u.getEmail(), "None", "None")
                        );
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    private void showSnackbar(int id) {
        Snackbar.make(v_activity, id, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void OnUserLoggedOut() {
        barLoggedIn.setVisibility(View.GONE);
        barAnonymous.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, pgOverviewFragment, TAG_OVERVIEW).commit();

        paymentFragment = null;
        profileFragment = null;
        Constants.IS_SIGNED_IN = false;
        Constants.CURRENT_PG = Constants.NO_PG;
        stopService(new Intent(this, NotificationService.class));
        showSnackbar(R.string.sign_out_successful);
    }

    @Override
    public void ComplaintSelectedForUser(String userEmail) {
        if (complaintFragment == null)
            complaintFragment = new FragmentComplaints();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_EMAIL, userEmail);
        complaintFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame, complaintFragment, TAG_COMPLAINT).commit();
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.xml.activity_open_scale, R.xml.activity_close_translate);
    }

}

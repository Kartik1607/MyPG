package stfo.com.mypg;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import stfo.com.mypg.Util.Utils;
import stfo.com.mypg.pojo.ChatMessage;
import stfo.com.mypg.pojo.Payment;

/**
 * Created by Kartik Sharma on 28/02/17.
 */
public class NotificationService extends Service {
    FirebaseDatabase database;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference mRef;
        mRef = database.getReference().child(Constants.CHILD_PAYMENTS).child(Utils.CURRENT_EMAIL.replace(".",","));


        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Payment payment = dataSnapshot.getValue(Payment.class);
                if(payment.getStatus() == 1){ //PAYMENT IS DUE
                    showUserNotification(payment);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showUserNotification(Payment payment){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_payment);
        mBuilder.setContentTitle(getString(R.string.payment_due));
        mBuilder.setContentText(getString(R.string.payment_amount, payment.getAmount(), payment.getDate()));
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }


    @Override
    public void onDestroy() {

    }
}


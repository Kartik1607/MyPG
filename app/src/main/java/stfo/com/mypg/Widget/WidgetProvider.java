package stfo.com.mypg.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import stfo.com.mypg.Constants;
import stfo.com.mypg.R;
import stfo.com.mypg.Util.Utils;
import stfo.com.mypg.pojo.Payment;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class WidgetProvider extends AppWidgetProvider {

    private Context context;
    private String email;
    int[] appWidgetIds;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (Utils.paymentArrayList == null) {
            email = Utils.CURRENT_EMAIL;
            this.context = context;
            this.appWidgetIds = appWidgetIds;
            new LoadDataFromFirebase().execute();
        } else {
            for (int i : appWidgetIds) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                Intent listIntent = new Intent(context, WidgetService.class);
                views.setRemoteAdapter(R.id.listView_widget, listIntent);
                appWidgetManager.updateAppWidget(i, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(i, R.id.listView_widget);
            }
        }
    }

    private void loadData() {
        Utils.paymentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_PAYMENTS)
                .child(email.replace(".", ","));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Payment newData = data.getValue(Payment.class);
                    Utils.paymentArrayList.add(newData);
                    AppWidgetManager appWidgetManager = AppWidgetManager
                            .getInstance(context);
                    onUpdate(context, appWidgetManager, appWidgetIds);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class LoadDataFromFirebase extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }
    }
}
package stfo.com.mypg.Widget;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import stfo.com.mypg.R;
import stfo.com.mypg.Util.Utils;
import stfo.com.mypg.pojo.Payment;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<Payment> payments;
    Context context;
    String email;

    public WidgetFactory(Context context, String email) {
        this.context = context;
        this.email = email;
        payments = Utils.paymentArrayList;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }



    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return payments == null ? 0 : payments.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        if (payments!= null) {
            Payment data = payments.get(position);
            view.setTextViewText(R.id.textView_payment_status,
                    Utils.getStatus( data.getStatus().intValue()));
            view.setTextColor(R.id.textView_payment_status, Utils.getStatusColor(data.getStatus().intValue()));
            view.setTextViewText(R.id.textView_payment_date,
                    data.getDate());
            view.setTextViewText(R.id.textView_payment_amount,
                    context.getString(R.string.price,data.getAmount()));
            Log.d("MY_APP",data.getDate());
        }
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

package stfo.com.mypg.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import stfo.com.mypg.R;
import stfo.com.mypg.Util.Utils;

/**
 * Created by Kartik Sharma on 23/02/17.
 */
public class PaymentAdapter extends RecyclerView.ViewHolder {

    private TextView tv_amount, tv_status, tv_date;

    public PaymentAdapter(View itemView) {
        super(itemView);
        tv_amount = (TextView) itemView.findViewById(R.id.textView_payment_amount);
        tv_status = (TextView) itemView.findViewById(R.id.textView_payment_status);
        tv_date = (TextView) itemView.findViewById(R.id.textView_payment_date);
    }

    public void setData(Long amount, int status, String date, Context context){
        tv_amount.setText(context.getString(R.string.price,amount));
        tv_status.setTextColor(ContextCompat.getColor(context,Utils.getStatusColor(status)));
        tv_status.setText(Utils.getStatus(status));
        tv_date.setText(date);
    }
}


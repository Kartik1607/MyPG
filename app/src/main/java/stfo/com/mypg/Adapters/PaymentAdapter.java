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

    private TextView tvAmount, tvStatus, tvDate;

    public PaymentAdapter(View itemView) {
        super(itemView);
        tvAmount = (TextView) itemView.findViewById(R.id.textView_payment_amount);
        tvStatus = (TextView) itemView.findViewById(R.id.textView_payment_status);
        tvDate = (TextView) itemView.findViewById(R.id.textView_payment_date);
    }

    public void setData(Long amount, int status, String date, Context context) {
        tvAmount.setText(context.getString(R.string.price, amount));
        tvStatus.setTextColor(ContextCompat.getColor(context, Utils.getStatusColor(status)));
        tvStatus.setText(Utils.getStatus(status));
        tvDate.setText(date);
    }
}


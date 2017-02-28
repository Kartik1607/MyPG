package stfo.com.mypg.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import stfo.com.mypg.R;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class ChatAdapter extends RecyclerView.ViewHolder {

    TextView textView;
    LinearLayout itemView;

    public ChatAdapter(View itemView) {
        super(itemView);
        this.itemView = (LinearLayout) itemView;
        textView = (TextView) itemView.findViewById(R.id.textView_complaint_message);

    }

    public void bindData(Context context, String text, boolean isUser) {
        if (isUser) {
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            itemView.setGravity(Gravity.START);
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
            itemView.setGravity(Gravity.END);
        }

        textView.setText(text);

    }
}

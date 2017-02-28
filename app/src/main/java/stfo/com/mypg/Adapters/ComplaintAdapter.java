package stfo.com.mypg.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import stfo.com.mypg.R;

/**
 * Created by Kartik Sharma on 25/02/17.
 */
public class ComplaintAdapter extends RecyclerView.ViewHolder {

    private TextView tv_date, tv_message;
    private ClickListener mClickListener;

    public ComplaintAdapter(View itemView) {
        super(itemView);
        tv_date = (TextView) itemView.findViewById(R.id.textView_complaint_date);
        tv_message = (TextView) itemView.findViewById(R.id.textView_complaint_message);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    public void setData(String date, String message) {
        tv_date.setText(date);
        tv_message.setText(message);
    }

    public void setOnClickListener(ComplaintAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}

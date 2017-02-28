package stfo.com.mypg.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import stfo.com.mypg.R;

/**
 * Created by Kartik Sharma on 20/02/17.
 */
public class PGOverview extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView price, location;
    private PGOverview.ClickListener mClickListener;


    public PGOverview(View itemView) {
        super(itemView);
        price = (TextView) itemView.findViewById(R.id.textView_pg_price);
        location = (TextView) itemView.findViewById(R.id.textView_pg_location);
        imageView = (ImageView) itemView.findViewById(R.id.imageView_pg);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
    }

    public void setPrice(Context context, long p) {
        price.setText(context.getString(R.string.price, p));
    }

    public void setLocation(String l) {
        location.setText(l);
    }

    public void setImage(Context context, String url) {
        Glide.with(context).load(url).centerCrop().into(imageView);
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnClickListener(PGOverview.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}

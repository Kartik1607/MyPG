package stfo.com.mypg.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import stfo.com.mypg.R;

/**
 * Created by Kartik Sharma on 20/02/17.
 */
public class PG_Overview extends RecyclerView.ViewHolder{

    private ImageView imageView;
    private TextView price, location;
    private RecyclerView facilities;


    public PG_Overview(View itemView) {
        super(itemView);
        price = (TextView) itemView.findViewById(R.id.textView_pg_price);
        location = (TextView) itemView.findViewById(R.id.textView_pg_location);
        imageView = (ImageView) itemView.findViewById(R.id.imageView_pg);
        //facilities = (RecyclerView) itemView.findViewById(R.id.recyclerView_pg_overview_facilities);
    }

    public void setPrice(Context context, long p){
        price.setText(context.getString(R.string.price,p));
    }

    public void setLocation(String l){
        location.setText(l);
    }

    public void setImage(Context context, String url){
        Glide.with(context).load(url).centerCrop().into(imageView);
    }

}

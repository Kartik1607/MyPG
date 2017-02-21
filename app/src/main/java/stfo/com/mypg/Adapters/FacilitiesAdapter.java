package stfo.com.mypg.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import stfo.com.mypg.R;
import stfo.com.mypg.pojo.Facility;

/**
 * Created by Kartik Sharma on 21/02/17.
 */
public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesAdapter.FacilityViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Facility> data;

    public FacilitiesAdapter(Context context, ArrayList<Facility> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FacilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FacilityViewHolder(inflater.inflate(R.layout.layout_facility, parent, false));
    }

    @Override
    public void onBindViewHolder(FacilityViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;


        public FacilityViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_Facility);
            textView = (TextView) itemView.findViewById(R.id.textView_Facility_Name);
        }

        void bind(int position){
            Facility current = data.get(position);
            textView.setText(current.getName());
            Glide.with(context).load(current.getImage()).fitCenter().into(imageView);
        }

    }
}

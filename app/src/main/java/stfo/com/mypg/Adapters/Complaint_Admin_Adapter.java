package stfo.com.mypg.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import stfo.com.mypg.R;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class Complaint_Admin_Adapter extends RecyclerView.Adapter<Complaint_Admin_Adapter.ViewHolder> {

    public interface OnAdminComplaintSelected{
        void ComplaintSelectedForUser(String userEmail);
    }

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> data;

    public Complaint_Admin_Adapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<String> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_complaint,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_User;

        public void bind(int position){
            tv_User.setText(data.get(position));
        }

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof OnAdminComplaintSelected){
                        ((OnAdminComplaintSelected) context).ComplaintSelectedForUser(data.get(getAdapterPosition()));
                    }
                }
            });
            tv_User = (TextView) itemView.findViewById(R.id.textView_complaint_message);
        }
    }
}

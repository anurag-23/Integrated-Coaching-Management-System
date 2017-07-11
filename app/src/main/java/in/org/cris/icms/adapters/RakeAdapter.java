package in.org.cris.icms.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.activities.ReceiveRakeActivity;
import in.org.cris.icms.activities.SendRakeActivity;
import in.org.cris.icms.models.Rake;

/**
 * Created by anurag on 23/6/17.
 */
public class RakeAdapter extends RecyclerView.Adapter<RakeAdapter.RakeViewHolder> {
    private List<Rake> rakeList;
    private Activity activity;
    private static final int SEND = 0;
    private static final int RECEIVE = 1;
    private int option;

    public RakeAdapter(List<Rake> rakeList, Activity activity, int option) {
        this.rakeList = rakeList;
        this.activity = activity;
        this.option = option;
    }

    @Override
    public RakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RakeViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_rake, parent, false));
    }

    @Override
    public void onBindViewHolder(RakeViewHolder holder, int position) {
        Rake rake = rakeList.get(position);
        holder.trainNo.setText(rake.getTrainNo()+"");
        holder.trainName.setText(rake.getTrainName());
        holder.startDate.setText(rake.getStartDate());
        holder.status.setText(rake.getStatus());
        holder.lastEventTime.setText(rake.getLastEventTime());
    }

    @Override
    public int getItemCount() {
        return rakeList.size();
    }

    class RakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trainNo;
        TextView trainName;
        TextView startDate;
        TextView status;
        TextView lastEventTime;

        public RakeViewHolder(View itemView) {
            super(itemView);
            trainNo = (TextView)itemView.findViewById(R.id.rake_train_no_text_view);
            trainName = (TextView)itemView.findViewById(R.id.rake_train_name_text_view);
            startDate = (TextView)itemView.findViewById(R.id.rake_date_text_view);
            status = (TextView)itemView.findViewById(R.id.rake_status_text_view);
            lastEventTime = (TextView)itemView.findViewById(R.id.rake_last_event_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            if (option == SEND){
                intent = new Intent(activity, SendRakeActivity.class);
            }
            else{
                intent = new Intent(activity, ReceiveRakeActivity.class);
            }
            intent.putExtra("trainNo", rakeList.get(getLayoutPosition()).getTrainNo());
            intent.putExtra("trainName", rakeList.get(getLayoutPosition()).getTrainName());
            intent.putExtra("startDate", rakeList.get(getLayoutPosition()).getStartDate());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_diagonal);
        }
    }
}

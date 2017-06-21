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
import in.org.cris.icms.activities.ConsistActivity;
import in.org.cris.icms.models.Train;

/**
 * Created by anurag on 20/6/17.
 */
public class TrainsAdapter extends RecyclerView.Adapter<TrainsAdapter.TrainsViewHolder> {

    private List<Train> trainsList;
    private Activity activity;

    public TrainsAdapter(List<Train> trainsList, Activity activity) {
        this.trainsList = trainsList;
        this.activity = activity;
    }

    @Override
    public TrainsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrainsViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_train, parent, false));
    }

    @Override
    public void onBindViewHolder(TrainsViewHolder holder, int position) {
        Train train = trainsList.get(position);
        holder.trainNo.setText(train.getTrainNo()+"");
        holder.trainName.setText(train.getName());
        holder.date.setText(train.getDate());
        holder.source.setText(train.getSource());
        holder.destination.setText(train.getDestination());
        holder.arrTime.setText(train.getArrTime());
        holder.depTime.setText(train.getDepTime());
    }

    @Override
    public int getItemCount() {
        return trainsList.size();
    }

    class TrainsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trainNo;
        TextView trainName;
        TextView date;
        TextView source;
        TextView destination;
        TextView arrTime;
        TextView depTime;

        public TrainsViewHolder(View itemView) {
            super(itemView);

            trainNo = (TextView)itemView.findViewById(R.id.train_no_text_view);
            trainName = (TextView)itemView.findViewById(R.id.train_name_text_view);
            date = (TextView)itemView.findViewById(R.id.date_text_view);
            source = (TextView)itemView.findViewById(R.id.source_text_view);
            destination = (TextView)itemView.findViewById(R.id.destination_text_view);
            arrTime = (TextView)itemView.findViewById(R.id.arrival_time_text_view);
            depTime = (TextView)itemView.findViewById(R.id.departure_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, ConsistActivity.class);
            Train train = trainsList.get(getLayoutPosition());
            intent.putExtra("trainNo", train.getTrainNo());
            intent.putExtra("trainName", train.getName());
            intent.putExtra("date", train.getDate());
            intent.putExtra("source", train.getSource());
            intent.putExtra("destination", train.getDestination());
            intent.putExtra("arrTime", train.getArrTime());
            intent.putExtra("depTime", train.getDepTime());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}

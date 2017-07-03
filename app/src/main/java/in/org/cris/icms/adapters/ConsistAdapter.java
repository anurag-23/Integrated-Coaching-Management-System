package in.org.cris.icms.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.models.Coach;

/**
 * Created by anurag on 20/6/17.
 */
public class ConsistAdapter extends RecyclerView.Adapter<ConsistAdapter.ConsistViewHolder> {

    private List<Coach> coachList;
    private Activity activity;

    public ConsistAdapter(List<Coach> coachList, Activity activity) {
        this.coachList = coachList;
        this.activity = activity;
    }

    @Override
    public ConsistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConsistViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_coach, parent, false));
    }

    @Override
    public void onBindViewHolder(ConsistViewHolder holder, int position) {
        Coach coach = coachList.get(position);
        holder.serialNo.setText(coach.getSerialNo()+"");
        holder.coachInfo.setText(coach.getOwnRly()+" - "+coach.getCoachType()+" - "+coach.getCoachNo());
        holder.from.setText(coach.getFrom());
        holder.to.setText(coach.getTo());
        holder.remark.setText(coach.getRemark());
        holder.prsID.setText(coach.getPrsID());
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    class ConsistViewHolder extends RecyclerView.ViewHolder {
        TextView serialNo;
        TextView coachInfo;
        TextView from;
        TextView to;
        TextView prsID;
        TextView remark;
        CheckBox dispute;

        public ConsistViewHolder(View itemView) {
            super(itemView);

            serialNo = (TextView)itemView.findViewById(R.id.serial_no_text_view);
            coachInfo = (TextView)itemView.findViewById(R.id.coach_info_text_view);
            from = (TextView)itemView.findViewById(R.id.from_text_view);
            to = (TextView)itemView.findViewById(R.id.to_text_view);
            prsID = (TextView)itemView.findViewById(R.id.prs_id_text_view);
            remark = (TextView)itemView.findViewById(R.id.remark_text_view);
            dispute = (CheckBox)itemView.findViewById(R.id.dispute_checkbox);
        }
    }
}

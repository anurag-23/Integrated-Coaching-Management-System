package in.org.cris.icms.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.models.consistverification.Coach;

/**
 * Created by anurag on 23/6/17.
 */
public class RakeConsistAdapter extends RecyclerView.Adapter<RakeConsistAdapter.RakeConsistViewHolder> {
    private List<Coach> coachList;
    private Context context;

    public RakeConsistAdapter(List<Coach> coachList, Context context) {
        this.coachList = coachList;
        this.context = context;
    }

    @Override
    public RakeConsistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RakeConsistViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rake_consist, parent, false));
    }

    @Override
    public void onBindViewHolder(RakeConsistViewHolder holder, int position) {
        Coach coach = coachList.get(position);
        holder.serialNo.setText(coach.getSerialNo()+"");
        holder.coachInfo.setText(coach.getOwnRly()+ " - " +coach.getCoachType()+" - "+coach.getCoachNo());
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    class RakeConsistViewHolder extends RecyclerView.ViewHolder {
        private TextView serialNo;
        private TextView coachInfo;


        public RakeConsistViewHolder(View itemView) {
            super(itemView);
            serialNo = (TextView)itemView.findViewById(R.id.rake_coach_serial_no_text_view);
            coachInfo = (TextView)itemView.findViewById(R.id.rake_coach_info_text_view);
        }
    }
}

package in.org.cris.icms.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.org.cris.icms.R;
import in.org.cris.icms.fragments.ShopMarkingDialogFragment;
import in.org.cris.icms.models.Coach;

/**
 * Created by anurag on 27/6/17.
 */
public class ShopMarkCoachAdapter extends RecyclerView.Adapter<ShopMarkCoachAdapter.ShopMarkCoachViewHolder> {

    private List<Coach> coachList;
    private Context context;
    private FragmentManager fragmentManager;

    public ShopMarkCoachAdapter(List<Coach> coachList, Context context, FragmentManager fragmentManager) {
        this.coachList = coachList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ShopMarkCoachViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopMarkCoachViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shop_mark_coach, parent, false));
    }

    @Override
    public void onBindViewHolder(ShopMarkCoachViewHolder holder, int position) {
        Coach coach = coachList.get(position);
        holder.coachInfo.setText(coach.getOwnRly()+" - "+coach.getCoachType()+" - "+coach.getCoachNo());
        holder.baseDepot.setText(coach.getBaseDepot());
        holder.builtYear.setText(coach.getBuiltYear());
        holder.pohYear.setText(coach.getPohYear());
        holder.pohMonth.setText(coach.getPohMonth());
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    class ShopMarkCoachViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView coachInfo;
        TextView baseDepot;
        TextView builtYear;
        TextView pohYear;
        TextView pohMonth;

        public ShopMarkCoachViewHolder(View itemView) {
            super(itemView);
            coachInfo = (TextView)itemView.findViewById(R.id.shop_coach_info_text_view);
            baseDepot = (TextView)itemView.findViewById(R.id.shop_base_depot_text_view);
            builtYear = (TextView)itemView.findViewById(R.id.shop_built_year_text_view);
            pohYear = (TextView)itemView.findViewById(R.id.shop_poh_year_text_view);
            pohMonth = (TextView)itemView.findViewById(R.id.shop_poh_month_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ShopMarkingDialogFragment fragment = ShopMarkingDialogFragment.createInstance();
            fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            Bundle bundle = new Bundle();
            Coach coach = coachList.get(getLayoutPosition());
            bundle.putString("coachInfo", coach.getOwnRly()+" - "+coach.getCoachType()+" - "+coach.getCoachNo());
            fragment.setArguments(bundle);
            fragment.show(fragmentManager, context.getString(R.string.shop_marking));
        }
    }
}

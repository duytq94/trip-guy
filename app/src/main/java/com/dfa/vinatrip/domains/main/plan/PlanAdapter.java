package com.dfa.vinatrip.domains.main.plan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.plan.detail_plan.DetailPlanActivity_;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planList;
    private LayoutInflater layoutInflater;
    private Context context;
    private UserProfile currentUser;
    private int lastItemPosition;
    private OnUpdateOrRemoveClick onUpdateOrRemoveClick;

    public PlanAdapter(Context context, List<Plan> planList, UserProfile currentUser) {
        this.planList = planList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanAdapter.PlanViewHolder holder, final int position) {
        final Plan plan = planList.get(position);
        lastItemPosition = planList.size() - 1;

        if (position == lastItemPosition && planList.size() > 2) {
            holder.llFooter.setVisibility(View.VISIBLE);
        } else {
            holder.llFooter.setVisibility(View.GONE);
        }

        holder.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPlanActivity_.class);

                // Send Plan to DetailPlanActivity
                intent.putExtra("Plan", planList.get(position));
                context.startActivity(intent);
            }
        });

        holder.tvName.setText(plan.getName());
        holder.tvDestination.setText(plan.getDestination());
        holder.tvDate.setText(plan.getDateGo() + " " + Html.fromHtml("&#10132;") + " " + plan.getDateBack());

        if (plan.getUserMakePlan().getUid().equals(currentUser.getUid())) {
            holder.tvUserName.setText("Tôi");
            holder.tvUpdate.setVisibility(View.VISIBLE);

            holder.tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUpdateOrRemoveClick.onUpdate(position);
                }
            });

            holder.tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUpdateOrRemoveClick.onRemove(position);
                }
            });
        } else {
            holder.ivIsMyPlan.setImageResource(0);
            holder.tvUpdate.setVisibility(View.GONE);
            holder.tvUpdate.setTextColor(android.R.color.darker_gray);
            holder.tvUserName.setText(plan.getUserMakePlan().getNickname());
        }


        Picasso.with(context).load(plan.getUserMakePlan().getAvatar())
               .placeholder(R.drawable.ic_loading)
               .error(R.drawable.photo_not_available)
               .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDestination, tvDate, tvUserName, tvUpdate, tvRemove;
        private ImageView ivAvatar, ivIsMyPlan;
        private LinearLayout llDetail, llFooter;

        public PlanViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_plan_tv_name);
            tvDestination = (TextView) itemView.findViewById(R.id.item_plan_tv_destination);
            tvDate = (TextView) itemView.findViewById(R.id.item_plan_tv_date);
            tvUserName = (TextView) itemView.findViewById(R.id.item_plan_tv_user_name);
            tvUpdate = (TextView) itemView.findViewById(R.id.item_plan_tv_update);
            tvRemove = (TextView) itemView.findViewById(R.id.item_plan_tv_remove);
            ivAvatar = (ImageView) itemView.findViewById(R.id.item_plan_iv_avatar);
            ivIsMyPlan = (ImageView) itemView.findViewById(R.id.item_plan_iv_is_my_plan);
            llDetail = (LinearLayout) itemView.findViewById(R.id.item_plan_ll_detail);
            llFooter = (LinearLayout) itemView.findViewById(R.id.item_plan_ll_footer);
        }
    }

    public void setOnUpdateOrRemoveClick(OnUpdateOrRemoveClick onUpdateOrRemoveClick) {
        this.onUpdateOrRemoveClick = onUpdateOrRemoveClick;
    }

    public interface OnUpdateOrRemoveClick {
        void onUpdate(int position);

        void onRemove(int position);
    }
}
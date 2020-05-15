package com.noplugins.keepfit.userplatform.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.bean.TimeWeekBean;

import java.util.ArrayList;
import java.util.List;

public class ChangguanDetailTimeAdapter extends RecyclerView.Adapter<ChangguanDetailTimeAdapter.ViewHolder> {
    private List<Boolean> isClicks;
    private LayoutInflater mInflater;
    private List<TimeWeekBean> mDatas;

    public ChangguanDetailTimeAdapter(Context context, @Nullable ArrayList<TimeWeekBean> data) {

        mInflater = LayoutInflater.from(context);
        mDatas = data;
        isClicks = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                isClicks.add(true);
            } else {
                isClicks.add(false);
            }

        }
    }


    private OnItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_find_changguan_time, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTime = (TextView) view.findViewById(R.id.tv_time);
        viewHolder.tvWeek = (TextView) view.findViewById(R.id.tv_week);
        viewHolder.ll_time = view.findViewById(R.id.ll_time);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if (position == 0) {

            holder.tvTime.setText("今");
        } else {
            holder.tvTime.setText(mDatas.get(position).getTime());
        }

        if (isClicks.get(position)) {
            holder.ll_time.setBackgroundResource(R.drawable.xuanze_date);
            holder.tvTime.setTextColor(Color.parseColor("#ffffff"));
            holder.tvWeek.setTextColor(Color.parseColor("#ffffff"));
        } else {
            //6D7278
            if (position == 0) {
                holder.tvTime.setTextColor(Color.parseColor("#76CEE1"));
                holder.tvWeek.setTextColor(Color.parseColor("#76CEE1"));
            } else {
                holder.tvTime.setTextColor(Color.parseColor("#6D7278"));
                holder.tvWeek.setTextColor(Color.parseColor("#6D7278"));
            }

            holder.ll_time.setBackgroundResource(R.color.white);
        }

        holder.tvWeek.setText(mDatas.get(position).getWeek());

        if (mOnItemClickListener != null) {
            holder.ll_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < isClicks.size(); i++) {
                        isClicks.set(i, false);
                    }
                    isClicks.set(position, true);
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.ll_time, mDatas.get(position).getNowDate(), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ;
        TextView tvTime;
        TextView tvWeek;
        LinearLayout ll_time;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Object object, int position);
    }
}
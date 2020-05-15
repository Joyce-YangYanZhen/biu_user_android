package com.noplugins.keepfit.userplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.entity.LabelEntity;

import java.util.List;

public class TagAdapter extends BaseAdapter {
    private List<LabelEntity> strings;
    private LayoutInflater layoutInflater;
    private Context context;

    public TagAdapter(Context mcontext, List<LabelEntity> mstrings) {
        this.strings = mstrings;
        context = mcontext;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.tag_item, null);
            holder = new ViewHolder();
            holder.tag_value = (TextView) convertView.findViewById(R.id.tag_value);
            holder.tag_layout = (LinearLayout) convertView.findViewById(R.id.tag_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tag_value.setText(strings.get(position).getName());
        // TODO：多选
        if (strings.get(position).isCheck()) {
            holder.tag_layout.setBackgroundResource(R.drawable.tag_select_bg);
            holder.tag_value.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.tag_layout.setBackgroundResource(R.drawable.tag_bg);
            holder.tag_value.setTextColor(context.getResources().getColor(R.color.color_4A4A4A));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tag_value;
        LinearLayout tag_layout;
    }

}

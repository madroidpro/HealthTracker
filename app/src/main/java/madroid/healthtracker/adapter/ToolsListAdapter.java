package madroid.healthtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import madroid.healthtracker.R;
import madroid.healthtracker.activity.BMIActivity;
import madroid.healthtracker.activity.ToolsActivity;
import madroid.healthtracker.activity.WeightTrackerActivity;

/**
 * Created by madroid on 11-09-2016.
 */
public class ToolsListAdapter extends RecyclerView.Adapter<ToolsListAdapter.TitleViewHolder> {
    List<ToolsActivity.ListTitles> TitleList;
    Context context;

    public ToolsListAdapter(List<ToolsActivity.ListTitles> titleList,Context context) {
        this.TitleList = titleList;
        this.context = context;
    }

    @Override
    public TitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tools_item,parent,false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TitleViewHolder holder, int position) {
        final ToolsActivity.ListTitles ListTitle = TitleList.get(position);
        holder.title.setText(ListTitle.getTitle());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int titleId = ListTitle.getTitleId();
                switch (titleId){
                    case 1:{
                        Intent intent = new Intent(context, BMIActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(context, WeightTrackerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return TitleList.size();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public View view;

        public TitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.itemTitle);
            view=itemView;
        }
    }
}

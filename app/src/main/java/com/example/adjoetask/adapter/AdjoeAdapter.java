package com.example.adjoetask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adjoetask.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AdjoeAdapter extends RecyclerView.Adapter<AdjoeAdapter.ViewHolder> {
    private Context context;
    JSONArray jsonArray;

    public AdjoeAdapter(Context context, JSONArray jsonObjectArrayList) {
        this.context = context;
        this.jsonArray = jsonObjectArrayList;
    }

    @NonNull
    @Override
    public AdjoeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_each_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdjoeAdapter.ViewHolder viewHolder, int i) {
        JSONObject article = null;
        try {
            article = jsonArray.getJSONObject(i);
            viewHolder.tvTitle.setText(article.getString("title"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}

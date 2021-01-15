package com.example.roomdemo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private List<PersonData> personDataList;
    private Context mContext;

    public PersonAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.results_data_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonData personData = personDataList.get(position);
        final String name = personData.getName();
        final String number = personData.getNumber();
        final int id = personData.getId();

        holder.tvName.setText(name);
        holder.tvPhone.setText(number);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name",name);
                intent.putExtra("number",number);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(personDataList == null){
            return 0;
        }
        return personDataList.size();
    }

    public void setData(List<PersonData> data) {
        personDataList = data;
        notifyDataSetChanged();
    }

    public List<PersonData> getData() {
        return personDataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvPhone = itemView.findViewById(R.id.phoneNumber);
            view = itemView;
        }
    }
}

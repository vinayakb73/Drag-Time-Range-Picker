package com.vinayak.dragtimerangepicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<String> strings;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

        }
    }


    public RecyclerAdapter() {
        strings=new ArrayList<>();
        String[] times=new String[]{"12:00","1:00","2:00","3:00","4:00"
                ,"5:00"
                ,"6:00"
                ,"7:00"
                ,"8:00"
                ,"9:00"
                ,"10:00"
                ,"11:00"
                ,"12:00"
                ,"13:00"
                ,"14:00"
                ,"15:00"
                ,"16:00","17:00"
                ,"18:00","19:00"


        };
        strings= Arrays.asList(times);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(strings.get(position));

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }



}

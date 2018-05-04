package com.artisanglory.autocompletelayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo  5/2/2018.
 */

public class CustomAdapter extends ArrayAdapter<Exercise> {

    private Context mContext;
    private ArrayList<Exercise> mExerciseList;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
        super(context, resource, objects);
        mContext = context;
        this.mExerciseList = objects ;

    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View view = LayoutInflater.from(mContext).inflate(R.layout.layout_custom,parent,false);
        TextView name = view.findViewById(R.id.name);
        name.setText(mExerciseList.get(position).getName());
        return view;
    }

    @Override
    public int getCount() {
        return mExerciseList.size();
    }
}

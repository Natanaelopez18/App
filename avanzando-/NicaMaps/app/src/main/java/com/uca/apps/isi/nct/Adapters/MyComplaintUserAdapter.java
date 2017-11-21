package com.uca.apps.isi.nct.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uca.apps.isi.nct.R;
import com.uca.apps.isi.nct.models.Complaint;

import java.util.List;




public class MyComplaintUserAdapter extends RecyclerView.Adapter<MyComplaintUserAdapter.ViewHolder> {

    private List<Complaint> complaints;


    @Override
    public MyComplaintUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_complaint, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view);
        return vh;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Complaint complaint = complaints.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(complaint.getTitle());
        holder.description.setText(complaint.getDescription());
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView description;

        //Mapea las vistas

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);

        }
    }


    public MyComplaintUserAdapter(List<Complaint> complaints) {
        this.complaints = complaints;
    }











}

package com.uca.apps.isi.nct.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uca.apps.isi.nct.R;
import com.uca.apps.isi.nct.models.Picture;

import java.util.List;

/**
 * Created by Natanael López on 22/11/2017.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private List<Picture> picture;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public SimpleDraweeView image;
        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.name);
            image =  view.findViewById(R.id.image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PictureAdapter(List<Picture> pictures) {
        this.picture = pictures;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_complaint_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture pictures =   picture.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(pictures.getTitle());
        holder.image.setImageURI(pictures.getUrl());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return picture.size();
    }



}

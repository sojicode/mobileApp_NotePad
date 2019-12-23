package com.example.notepad;

import android.widget.TextView;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView titleText;
    TextView saveTimeText;
    TextView notesText;

    NoteViewHolder(View view){
        super(view);

        titleText = view.findViewById(R.id.titleText);
        saveTimeText = view.findViewById(R.id.saveTimeText);
        notesText = view.findViewById(R.id.noteText);

    }
}

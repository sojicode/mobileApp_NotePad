package com.example.notepad;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private static final String TAG = "NotesAdapterTag";

    private ArrayList<Note> notesList;
    private MainActivity mActivity;

    NotesAdapter(ArrayList<Note> noteList, MainActivity mActivity){
        this.notesList = noteList;
        this.mActivity = mActivity;

    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: CREATING NEW ");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_item, parent, false);

        itemView.setOnClickListener(mActivity);
        itemView.setOnLongClickListener(mActivity);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: SETTING DATA");

        Note selectedNote = notesList.get(position);

        String contents = selectedNote.getContent();
        if(contents.length() > 80){
            contents = selectedNote.getContent().substring(0, 79) + "...";
        }

        holder.titleText.setText(selectedNote.getTitle());
        holder.notesText.setText(contents);

        Date getTime = new Date(selectedNote.getTimestamp());
        SimpleDateFormat timeformat = new SimpleDateFormat("EEE MMM dd, hh:mm aa");
        String finalTime = timeformat.format(getTime);
        holder.saveTimeText.setText(finalTime);

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}

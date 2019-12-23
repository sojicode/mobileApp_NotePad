

package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivityTag";
    
    private ArrayList<Note> noteList = new ArrayList<>();

    private static final int CODE_FOR_EDIT_ACTIVITY = 111;

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(noteList, this);

        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        loadFile();
        setTitle("Multi Notes(" + noteList.size() + ")");
    }

    public void openAboutActivity(View v){

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void openEditActivity(View v){

        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, CODE_FOR_EDIT_ACTIVITY);

    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Note selection = noteList.get(pos);

        Intent noteData = new Intent(this, EditActivity.class);
        noteData.putExtra("NOTE_TITLE", selection.getTitle());
        noteData.putExtra("NOTE_CONTENTS", selection.getContent());

        setResult(RESULT_OK, noteData);
        startActivityForResult(noteData, CODE_FOR_EDIT_ACTIVITY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        View v = findViewById(R.id.infoItem);

        switch(item.getItemId()) {
            case R.id.infoItem:
                openAboutActivity(v);
                break;
            case R.id.addItem:
                openEditActivity(v);
                break;
            default:
                Toast.makeText(this, "Unknown ", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    @Override
    public boolean onLongClick(View v) {

        int pos = recyclerView.getChildAdapterPosition(v);

        final Note selection = noteList.get(pos);
        final String title = selection.getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "onClick: delete the this note " + title);
                deleteNote(title);
                notesAdapter.notifyDataSetChanged();
                setTitle("Multi Notes(" + noteList.size() + ")");
                onPause();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setMessage("Delete Note '"+ title +"'?");

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    private void deleteNote(String title) {

        for(int i = 0; i < noteList.size(); i++){
            if(title.equals(noteList.get(i).getTitle())){
                Log.d(TAG, "deleteNote: "+noteList.get(i));
                noteList.remove(i);
            }
        }
        Log.d(TAG, "deleteNote: updated a new note list!!" );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

//        Log.d(TAG, "onActivityResult: >>>>> ");

        if(requestCode == CODE_FOR_EDIT_ACTIVITY){
            if(resultCode == RESULT_OK){
                String textTitle = data.getStringExtra("USER_INPUT1");
                String textContents = data.getStringExtra("USER_INPUT2");
//                Log.d(TAG, "onActivityResult: title >>> "+textTitle);
//                Log.d(TAG, "onActivityResult: contents "+textContents);
                long time = System.currentTimeMillis();

                Note newNote = new Note(textTitle, textContents, time);

                for(int i = 0; i < noteList.size(); i++) {
                    if (textTitle.equals(noteList.get(i).getTitle())) {
                        noteList.remove(i);
                    }
                }
                noteList.add(0, newNote);

                if(textTitle.equals("")){

                    noteList.remove(newNote);
                    Toast.makeText(this,"Un-titled activity was not saved!", Toast.LENGTH_LONG).show();
                }

                notesAdapter.notifyDataSetChanged();
                setTitle("Multi Notes(" + noteList.size() + ")");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {

        super.onPause();
        try {
            saveNote();
//            Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException | JSONException e) {
//            Toast.makeText(this, "Note NOT Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveNote() throws IOException, JSONException {

//        Log.d(TAG, "saveNote: Saving JSON file >>>>" );

        FileOutputStream fos = getApplicationContext().openFileOutput("Note.json", Context.MODE_PRIVATE);

        JSONArray jsonArray = new JSONArray();

        for(Note note : noteList){

            try {
                JSONObject noteJson = new JSONObject();
                noteJson.put("titleText", note.getTitle());
//                Log.d(TAG, "saveNote: titleText "+ note.getTitle());
                noteJson.put("contentText", note.getContent());
                noteJson.put("timeStamp", note.getTimestamp());
                jsonArray.put(noteJson);
//                Log.d(TAG, "saveNote: Json Object : "+noteJson);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String jsonText = jsonArray.toString();
//        Log.d(TAG, "saveNote: jsonText >>> "+ jsonText);

        fos.write(jsonText.getBytes());
        fos.close();
    }

    private void loadFile() {

//        Log.d(TAG, "loadFile: Loading JSON File >>>>> " );

        try{
            InputStream is = getApplicationContext().openFileInput("Note.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null ){
                sb.append(line);
            }
            reader.close();

//            Log.d(TAG, "loadFile: JSON " + sb.toString());

            JSONArray jsonArray = new JSONArray(sb.toString());

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                String title = jsonObject.getString("titleText" );
//                Log.d(TAG, "loadFile: json object to string : "+title );
                String contents = jsonObject.getString("contentText");
//                Log.d(TAG, "loadFile: json object to string : " +contents);
                long time = jsonObject.getLong("timeStamp");
                Note n = new Note(title, contents, time);
                noteList.add(n);
                notesAdapter.notifyDataSetChanged();
//                Log.d(TAG, "loadFile: noteList>>> "+noteList.size());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

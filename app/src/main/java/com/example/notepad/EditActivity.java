package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    private EditText editTitle;
    private EditText editContents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setTitle("Multi Notes");

        editTitle = findViewById(R.id.editTitle);
        editContents = findViewById(R.id.editContents);

        Intent intent = getIntent();
        if(intent.hasExtra("NOTE_TITLE") && intent.hasExtra("NOTE_CONTENTS")){

            String title = intent.getStringExtra("NOTE_TITLE");
            editTitle.setText(title);
            String contents = intent.getStringExtra("NOTE_CONTENTS");
            editContents.setText(contents);
        } else {
            editTitle.setText("");
            editContents.setText("");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        saveItemClicked();

        return true;
    }

    @Override
    public void onBackPressed() {

        //Log.d(TAG, "onBackPressed: dialog shows " );

        final String title = editTitle.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                saveItemClicked();

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });

        builder.setMessage("Save note '"+ title +"'?");
        builder.setTitle("Your note is not saved!");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveItemClicked() {

//        Log.d(TAG, "saveItemClicked: start sending >>>>" );

        Intent data = new Intent();

        data.putExtra("USER_INPUT1", editTitle.getText().toString());
        data.putExtra("USER_INPUT2", editContents.getText().toString());
        setResult(RESULT_OK, data);
        finish();

    }

}

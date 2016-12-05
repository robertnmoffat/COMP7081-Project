package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class LoadGameActivity extends AppCompatActivity {
    String selectedFile;
    LinearLayout currentHighlightedRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        setupViews();
    }

    public void setupViews(){
        String[] saveNames = findSaves();

        LinearLayout ll = (LinearLayout)findViewById(R.id.filenamesLinearLayout);
        ll.removeAllViews();

        int idCount = 0;

        //add save names to the activity
        for(final String curName: saveNames){
            final LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            //TextView saveText = new TextView(this);
            //saveText.setText(curName);

            //row.addView(saveText);

            final Button button = new Button(this);
            button.setText(curName);
            button.setBackground(null);
            button.setId(999+idCount++);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFile = curName.replace(".sav","");
                    if (currentHighlightedRow != null) {
                        currentHighlightedRow.setBackgroundColor(Color.WHITE);
                    }
                    row.setBackgroundColor(Color.BLUE);
                    currentHighlightedRow = row;
                }
            });

            row.addView(button);

            ll.addView(row);
        }
    }

    public String[] findSaves(){
        GenericExtFilter filter = new GenericExtFilter("sav");
        File dir = new File(this.getFilesDir().getPath());
        String[] list = dir.list(filter);
        return list;
    }

    public class GenericExtFilter implements FilenameFilter {

        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }

    public void loadAndStart(View view){
        if(!GameFunctions.load(this, selectedFile))return;

        Intent intent = new Intent(this, LoadOutMenu.class);
        startActivity(intent);
    }

    public void deleteSave(View view){
        GameFunctions.delete(this,selectedFile);

        setupViews();
    }

    public void cancelToMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}

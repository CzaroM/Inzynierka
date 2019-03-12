package com.example.czaro.inzynierka;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewCategoryActivity extends AppCompatActivity {

    private EditText titleCategory;
    private Button saveCategoryButton;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        this.titleCategory=(EditText) findViewById(R.id.titleCategory);
        this.saveCategoryButton=(Button) findViewById(R.id.saveCategoryButton);
        this.databaseHelper=new DatabaseHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.databaseHelper.getCategories();
        this.saveCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ischeckForm()) {
                    addCategory();
                    finish();
                }
                else{
                    Toast.makeText(NewCategoryActivity.this, " nie wypełniono wszystkich pol w formularzu", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addCategory(){
        Category category = new Category(this.titleCategory.getText().toString());
        this.databaseHelper.addCategory(category);
        this.databaseHelper.getCategories();
    }

    private boolean ischeckForm(){
        if (titleCategory.getText().toString().equals("")){
            return false;
        }
        return true;
    }
}

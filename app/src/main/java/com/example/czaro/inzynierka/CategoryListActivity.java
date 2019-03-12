package com.example.czaro.inzynierka;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    private ListView listViewCategoryList;
    private FloatingActionButton addCategoryButton;
    private List<Category>categories;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        this.addCategoryButton = (FloatingActionButton) findViewById(R.id.addCategory);
        this.listViewCategoryList = (ListView) findViewById(R.id.categoryList);
        this.categories = new ArrayList<>();
        this.databaseHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListActivity.this,NewCategoryActivity.class);
                startActivity(intent);
            }
        });
        displayCategories();
        listViewCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = categories.get(position);
                deleteDialog(category);
            }
        });
    }

    

    private void displayCategories(){
        this.categories=this.databaseHelper.getCategories();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories);
        this.listViewCategoryList.setAdapter(adapter);
    }

    private void deleteDialog(Category category){
        final Category categoryToDelete = category;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final  View dialogView = inflater.inflate(R.layout.category_delete_dialog,null);
        dialogBuilder.setView(dialogView);
        final Button buttonConfirmDeleteCategory = (Button) dialogView.findViewById(R.id.buttonConfirmDeleteCategory);
        final Button buttonDeclineDeleteCategory = (Button) dialogView.findViewById(R.id.buttonDeclineDeleteCategory);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        buttonConfirmDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteCategory(categoryToDelete);
                alertDialog.dismiss();
            }
        });
        buttonDeclineDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}
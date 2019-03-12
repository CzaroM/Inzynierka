package com.example.czaro.inzynierka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OperationsFromCategoryActivity extends AppCompatActivity {

    private List<Operation>operations;
    private ListView listViewOperationFromCategory;
    private DatabaseHelper databaseHelper;
    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations_from_category);
        this.operations=new ArrayList<>();
        listViewOperationFromCategory = (ListView) findViewById(R.id.listViewOperationFromCategory);
        this.databaseHelper = new DatabaseHelper(this);
        this.categoryName =getIntent().getExtras().getString("categoryName");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOperationsFromDatabase();
        displayOperations();
    }

    private List<Operation> getOperationsFromDatabase(){
        this.operations=this.databaseHelper.getOperations("category",categoryName);
        return operations;
    }

    private void displayOperations(){
        OperationAdapter adapter = new OperationAdapter(this, this.operations);
        // ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories);
        this.listViewOperationFromCategory.setAdapter(adapter);
    }

}

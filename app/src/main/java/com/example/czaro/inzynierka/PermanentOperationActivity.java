package com.example.czaro.inzynierka;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class PermanentOperationActivity extends AppCompatActivity {

    private ListView permamentOperationList;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton addPermamentOperation;
    private List<PermamentOperation> permamentOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permanent_operation);
        this.permamentOperationList = (ListView) findViewById(R.id.permamentOperationList);
        this.databaseHelper = new DatabaseHelper(this);
        this.permamentOperations=new ArrayList<>();
        addPermamentOperation = (FloatingActionButton) findViewById(R.id.addPermamentOperation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addPermamentOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PermanentOperationActivity.this, NewPermamentOperationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        permamentOperations = databaseHelper.getPermamentOperations();
        displayOperations();
        editOperation();
    }

    private void displayOperations() {
        PermamentOperationAdapter adapter = new PermamentOperationAdapter(this, permamentOperations);
        permamentOperationList.setAdapter(adapter);
    }

    private void editOperation(){
        permamentOperationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PermamentOperation operation = permamentOperations.get(position);
                Intent intent = new Intent(PermanentOperationActivity.this, EditPermamentOperationActivity.class);
                intent.putExtra("operationID",operation.getId());
                startActivity(intent);
            }
        });
    }
}
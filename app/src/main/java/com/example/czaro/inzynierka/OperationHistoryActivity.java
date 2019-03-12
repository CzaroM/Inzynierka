package com.example.czaro.inzynierka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OperationHistoryActivity extends AppCompatActivity {

    private ListView operationHistoryList;
    private DatabaseHelper databaseHelper;
    private List<Operation>operations;
    private final static String displayType="History";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_history);
        this.operationHistoryList = (ListView) findViewById(R.id.operationHistoryList);
        this.databaseHelper= new DatabaseHelper(this);
        this.operations=new ArrayList<>();
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
        operations=databaseHelper.getOperations();
        return operations;
    }

    private void displayOperations(){
        OperationAdapter adapter = new OperationAdapter(this, this.operations,displayType);
        this.operationHistoryList.setAdapter(adapter);
    }
}

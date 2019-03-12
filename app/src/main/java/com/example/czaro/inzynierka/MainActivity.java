package com.example.czaro.inzynierka;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int mode = 0;
    private TextView mTextMessage;
    private FloatingActionButton addButton;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    mode=0;
                   // getOperationsFromDataBase();
                    getOperationsFromDataBase();
                    operations = operationFromThisMonth();
                    displayOperations();
                    editOperation();
                    addOperation();
                    return true;
                case R.id.navigation_dashboard:
                   // mTextMessage.setText(R.string.title_dashboard);
                    mode=1;
                    getCategoriesFromDataBase();
                    displayCategories();
                    countCategoryBalance();
                    clickCategory();
                    addCategory();
                    return true;
            }
            return false;
        }
    };
    public ListView listViewOperationList;
    private DatabaseHelper databaseHelper;
    private List<Operation> operations;
    private List<Category> categories;
    private ArrayAdapter adapterr;
    private static final String minusBalance="Wydatek";
    private TextView balance;
    private TextView monthlyBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        this.addButton = (FloatingActionButton) findViewById(R.id.addOperation);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listViewOperationList=(ListView)findViewById(R.id.operationList);
        this.databaseHelper = new DatabaseHelper(this);
        this.operations=new ArrayList<>();
        this.categories=new ArrayList<>();
        this.balance = (TextView) findViewById(R.id.Balance);
        this.monthlyBalance = (TextView) findViewById(R.id.monthlyBalance);

        editOperation();
        addOperation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.category:
                Intent intent = new Intent(this, CategoryListActivity.class);
                startActivity(intent);
                return true;
            case R.id.history:
                Intent intent2 = new Intent(this, OperationHistoryActivity.class);
                startActivity(intent2);
                return true;
            case R.id.permamentOperation:
                Intent intent3 = new Intent(this, PermanentOperationActivity.class);
                startActivity(intent3);
                return true;
            case R.id.filter:
                Intent intent4 = new Intent(this, FiltersActivity.class);
                startActivity(intent4);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode==0){
            getOperationsFromDataBase();
            double balance=countBalance();
            if (balance < (double) 0){
                this.balance.setTextColor(Color.RED);
            }
            else{
                this.balance.setTextColor(Color.GREEN);
            }
            String allBalance="Ogólne saldo wynosi: "+balance;
            this.balance.setText(allBalance);
            operations = operationFromThisMonth();
            displayOperations();
            balance=countBalance();
            if (balance < 0){
                this.monthlyBalance.setTextColor(Color.RED);
            }
            else{
                this.monthlyBalance.setTextColor(Color.rgb(115,139,40 ));
            }
            this.monthlyBalance.setText("Saldo w tym miesiącu: "+balance);
        }

    }

    public void addOperation() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewOperationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addCategory(){
       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, NewCategoryActivity.class);
               startActivity(intent);
           }
       });
    }

    private void getOperationsFromDataBase(){
        this.operations=databaseHelper.getOperations();

    }

    private void displayOperations(){
        OperationAdapter adapter = new OperationAdapter(this, operations);
        listViewOperationList.setAdapter(adapter);
    }

    private List<Category> getCategoriesFromDataBase(){
        this.categories=this.databaseHelper.getCategories();
        return categories;
    }

    private void displayCategories(){
        List<Double>categoryBalanceList=countCategoryBalance();
        CategoryAdapter adapter = new CategoryAdapter(this, this.categories, categoryBalanceList);
        // ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories);
        this.listViewOperationList.setAdapter(adapter);
    }

    private List<Double> countCategoryBalance(){
        List<Double> categoryBalanceList = new ArrayList<>();
        for( int i = 0; i < categories.size(); i++){
            double counterBalance= 0;
            for (int j = 0; j < operations.size(); j++){
                if (categories.get(i).getName().equals(operations.get(j).getCategory())){
                    if(operations.get(j).getBalance().equals(minusBalance)){
                        counterBalance-=operations.get(j).getPrice();
                    }
                    else {
                        counterBalance += operations.get(j).getPrice();
                    }
                }
            }
            categoryBalanceList.add(counterBalance);
        }
        return categoryBalanceList;
    }

    private void editOperation(){
        listViewOperationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Operation operation = operations.get(position);
                Intent intent = new Intent(MainActivity.this, EditOperationActivity.class);
                intent.putExtra("operationID",operation.getId());
                startActivity(intent);
            }
        });
    }

    private void clickCategory(){
        listViewOperationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = categories.get(position);
                Intent intent = new Intent(MainActivity.this, OperationsFromCategoryActivity.class);
                intent.putExtra("categoryName", category.getName());
                startActivity(intent);
            }
        });
    }

    private List<Operation> operationFromThisMonth(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        List<Operation> operations = new ArrayList<>();
       // databaseHelper.deleteOperation(this.operations.get(25));
        for(int i=0; i < this.operations.size(); i++){
            String monthInObject = this.operations.get(i).getDate().substring(3,5);
            int operationMonth = Integer.parseInt(monthInObject);
            if(operationMonth==month){
                operations.add(this.operations.get(i));
            }
        }
        return operations;
    }

    private double countBalance(){
        double balance = 0;
        for(int i=0; i<operations.size() ;i++){
            if (operations.get(i).getBalance().toString().equals(minusBalance)){
                balance-=operations.get(i).getPrice();
            }
            else{
                balance+=operations.get(i).getPrice();
            }
        }
        return balance;
    }
}

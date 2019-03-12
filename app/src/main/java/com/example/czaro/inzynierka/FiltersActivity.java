package com.example.czaro.inzynierka;

import android.app.DatePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FiltersActivity extends AppCompatActivity {

    private ListView filterOperationList;
    private DatabaseHelper databaseHelper;
    private List<Operation> operations;
    private List<Operation> operationListToEdit;
    private List<Category> categoryList;
    private EditText filterStartDate;
    private EditText filterEndDate;
    private Button minusBalance;
    private Button plusBalance;
    private Button timeInterval;
    private Button monthButton;
    private Button filterCategoryButton;
    private final static String displayType="History";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        this.filterOperationList = (ListView) findViewById(R.id.filterOperationList);
        this.databaseHelper= new DatabaseHelper(this);
        this.operations=new ArrayList<>();
        this.timeInterval = (Button) findViewById(R.id.timeInterval);
        this.minusBalance = (Button) findViewById(R.id.minusBalance);
        this.plusBalance = (Button) findViewById(R.id.plusBalance);
        this.monthButton = (Button) findViewById(R.id.monthButton);
        this.filterCategoryButton = (Button) findViewById(R.id.filterCategoryButton);
        this.operationListToEdit = new ArrayList<>();
        this.categoryList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getOperationsFromDatabase();
        displayOperations();
        operationListToEdit = operations;
        timeInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationListToEdit = timeIntervalDialog();
            }
        });
        minusBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 minusBalanceButton(operationListToEdit);
            }
        });
        plusBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusBalanceButton(operationListToEdit);
            }
        });
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthsDialog();
            }
        });
        filterCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category(operationListToEdit);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private List<Operation> timeIntervalDialog(){
        final List<Operation>operationListFromDate = new ArrayList<>();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.time_interval_dialog,null);
        dialogBuilder.setView(dialogView);
        final Button startDateCalendar = (Button) dialogView.findViewById(R.id.filterStartDateCalendarButton);
        final Button endDateCalendar = (Button) dialogView.findViewById(R.id.filterEndDateCalendarButton);
        final Button search = (Button) dialogView.findViewById(R.id.search);
        filterStartDate = (EditText) dialogView.findViewById(R.id.filterStartDate);
        filterEndDate = (EditText) dialogView.findViewById(R.id.filterEndDate);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        startDateCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog(filterStartDate);
            }
        });
        endDateCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog(filterEndDate);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayInStartDate = filterStartDate.getText().toString().substring(0,2);
                int startDay = Integer.parseInt(dayInStartDate);
                String monthInStartDate = filterStartDate.getText().toString().substring(3,5);
                int startMonth = Integer.parseInt(monthInStartDate);
                String yearInStartDate =  filterStartDate.getText().toString().substring(6,10);
                int startYear = Integer.parseInt(yearInStartDate);

                String dayInEndDate = filterEndDate.getText().toString().substring(0,2);
                int endDay = Integer.parseInt(dayInEndDate);
                String monthInEndDate = filterEndDate.getText().toString().substring(3,5);
                int endMonth = Integer.parseInt(monthInEndDate);
                String yearInEndDate = filterEndDate.getText().toString().substring(6,10);
                int endYear = Integer.parseInt(yearInEndDate);

                for(int i=0; i < operations.size(); i++){
                    String dayInObject = operations.get(i).getDate().substring(0,2);
                    int operationDay = Integer.parseInt(dayInObject);
                    String monthInObject = operations.get(i).getDate().substring(3,5);
                    int operationMonth = Integer.parseInt(monthInObject);
                    String yearInObject = operations.get(i).getDate().substring(6,10);
                    int operationYear = Integer.parseInt(yearInObject);

                    if(operationYear == startYear){
                        if (startMonth == operationMonth && operationDay >= startDay){
                            if(startMonth==endMonth && operationDay <= endDay){
                                operationListFromDate.add(operations.get(i));
                                continue;
                            }
                            else if(startMonth==endMonth){
                                continue;
                            }
                            operationListFromDate.add(operations.get(i));
                            continue;
                        }
                        else if(startMonth < operationMonth){
                            if(endYear == startYear){
                                if(operationMonth > endMonth){
                                    continue;
                                }
                            }
                            operationListFromDate.add(operations.get(i));
                            continue;
                        }
                    }
                    else if(operationYear < startYear){
                        if (startMonth == operationMonth && operationDay >= startDay){
                            operationListFromDate.add(operations.get(i));
                            continue;
                        }
                        else if(startMonth > operationMonth){
                            operationListFromDate.add(operations.get(i));
                            continue;
                        }
                        //   operationListFromDate.add(operations.get(i));
                    }
                    else if(operationYear <= endYear){
                        if (endMonth == operationMonth && operationDay <= endDay){
                            operationListFromDate.add(operations.get(i));
                            continue;
                        }
                        else if(endMonth >= operationMonth && operationDay <= endDay){
                            operationListFromDate.add(operations.get(i));
                            continue;
                        }
                      //  operationListFromDate.add(operations.get(i));
                    }
                }
                alertDialog.dismiss();
                displayOperations(operationListFromDate);
            }
        });
        return operationListFromDate;
    }

    private void calendarDialog(final EditText editText){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FiltersActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                if(mDayOfMonth<10 && mMonth+1<10) {
                    editText.setText(("0" + mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                }
                else if(mDayOfMonth<10){
                    editText.setText(("0" + mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                }
                else if(mMonth+1<10){
                    editText.setText((mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                }
                else {
                    editText.setText((mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private List<Operation> getOperationsFromDatabase(){
        operations=databaseHelper.getOperations();
        return operations;
    }

    private void displayOperations(){
        OperationAdapter adapter = new OperationAdapter(this, this.operations,displayType);
        this.filterOperationList.setAdapter(adapter);
    }
    private void displayOperations(List<Operation>operations){
        OperationAdapter adapter = new OperationAdapter(this, operations,displayType);
        this.filterOperationList.setAdapter(adapter);
    }

    private void minusBalanceButton(List<Operation>operations){
        List<Operation>operationList = new ArrayList<>();
        for(int i=0; i < operations.size(); i++){
            if(operations.get(i).getBalance().toString().equals("Wydatek")){
                operationList.add(operations.get(i));
            }
        }
        displayOperations(operationList);
    }

    private void plusBalanceButton(List<Operation>operations){
        List<Operation>operationList = new ArrayList<>();
        for(int i=0; i < operations.size(); i++){
            if(operations.get(i).getBalance().toString().equals("Wpływ")){
                operationList.add(operations.get(i));
            }
        }
        displayOperations(operationList);
    }

    private void monthsDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final  View dialogView = inflater.inflate(R.layout.months_dialog,null);
        dialogBuilder.setView(dialogView);
        final Spinner monthsSpinner = (Spinner) dialogView.findViewById(R.id.categorySpinner);
        final Button monthButton = (Button) dialogView.findViewById(R.id.monthButton);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        String[] month = new String[] { "Styczen",
                "Luty", "Marzec", "Kwiecien", "Maj",
                "Czerwiec", "Lipiec", "Sierpien",
                "Wrzesien", "Pazdziernik", "Listopad", "Grudzien" };
        ArrayList<String> months = new ArrayList<String>();
        months.addAll(Arrays.asList(month));
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, month);
        monthsSpinner.setAdapter(adapter);
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = 0;
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                switch (monthsSpinner.getSelectedItem().toString()){
                    case "Styczen":
                        month = 1;
                        break;
                    case "Luty":
                        month = 2;
                        break;
                    case "Marzec":
                        month = 3;
                        break;
                    case "Kwiecien":
                        month = 4;
                        break;
                    case "Maj":
                        month = 5;
                        break;
                    case "Czerwiec":
                        month = 6;
                        break;
                    case "Lipiec":
                        month = 7;
                        break;
                    case "Sierpien":
                        month = 8;
                        break;
                    case "Wrzesien":
                        month = 9;
                        break;
                    case "Pazdziernik":
                        month = 10;
                        break;
                    case "Listopad":
                        month = 11;
                        break;
                    case "Grudzien":
                        month = 12;
                        break;
                }
                List<Operation>operationList = new ArrayList<>();
                for(int i=0; i < operations.size(); i++){
                    String monthInObject = operations.get(i).getDate().substring(3,5);
                    int operationMonth = Integer.parseInt(monthInObject);
                    String yearInObject = operations.get(i).getDate().substring(6,10);
                    int operationYear = Integer.parseInt(yearInObject);

                    if(operationYear == year){
                        if (operationMonth == month){
                            operationList.add(operations.get(i));
                            continue;
                        }
                    }
                }
                alertDialog.dismiss();
                displayOperations(operationList);
            }
        });
    }

    private void category(final List<Operation>operations){
        categoryList=this.databaseHelper.getCategories();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final  View dialogView = inflater.inflate(R.layout.months_dialog,null);
        dialogBuilder.setView(dialogView);
        final Spinner categorySpinner = (Spinner) dialogView.findViewById(R.id.categorySpinner);
        final Button categorySaveButton = (Button) dialogView.findViewById(R.id.monthButton);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList);
        categorySpinner.setAdapter(adapter);
        categorySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Operation>operationList = new ArrayList<>();
                for(int i=0; i < operations.size(); i++){
                    if(operations.get(i).getCategory().toString().equals(categorySpinner.getSelectedItem().toString())){
                        operationList.add(operations.get(i));
                    }
                }
                alertDialog.dismiss();
                displayOperations(operationList);
            }
        });
    }
}

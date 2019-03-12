package com.example.czaro.inzynierka;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class EditPermamentOperationActivity extends AppCompatActivity {

    private EditText price;
    private Spinner categoryList;
    private Spinner frequency;
    private EditText title;
    private EditText date;
    private EditText endDate;
    private EditText note;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Button button;
    private Button endDateCalendarButton;
    private Button saveOperationButton;
    private Button deletePermamentOperation;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private List<Operation> operationList;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter adapter;
    private ArrayAdapter adapter2;
    private int operationID;
    private static final String minusBalance="Wydatek";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_permament_operation);
        databaseHelper = new DatabaseHelper(this);
        price = (EditText) findViewById(R.id.price);
        //  price.setText("0.00");
        categoryList=(Spinner) findViewById(R.id.categoryList);
        frequency=(Spinner) findViewById(R.id.frequencyList);
        title = (EditText) findViewById(R.id.title);
        note = (EditText) findViewById(R.id.note);
        date= (EditText) findViewById(R.id.filterStartDate);
        endDate= (EditText) findViewById(R.id.filterEndDate);
        button = (Button) findViewById(R.id.filterStartDateCalendarButton);
        endDateCalendarButton = (Button) findViewById(R.id.endDateCalendarButton);
        saveOperationButton = (Button) findViewById(R.id.saveOperationButton);
        deletePermamentOperation = (Button) findViewById(R.id.deletePermamentOperation);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup2);
        this.operationID =getIntent().getExtras().getInt("operationID");
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(EditPermamentOperationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        if(mDayOfMonth<10 && mMonth+1<10) {
                            date.setText(("0" + mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                        }
                        else if(mDayOfMonth<10){
                            date.setText(("0" + mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                        }
                        else if(mMonth+1<10){
                            date.setText((mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                        }
                        else {
                            date.setText((mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        endDateCalendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(EditPermamentOperationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        if(mDayOfMonth<10 && mMonth+1<10) {
                            endDate.setText(("0" + mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                        }
                        else if(mDayOfMonth<10){
                            endDate.setText(("0" + mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                        }
                        else if(mMonth+1<10){
                            endDate.setText((mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                        }
                        else {
                            endDate.setText((mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        frequencyListToSpinner();
        categoryListToSpinner();
        operationList = databaseHelper.getOperations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final PermamentOperation permamentOperation = databaseHelper.getParmamentOperation(operationID);
        dataToForm(permamentOperation);
        saveOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Operation> operations = new ArrayList<>();
                for (int i=0; i < operationList.size() ; i++){
                    if (operationList.get(i).getTitle().toString().equals(permamentOperation.getTitle().toString())){
                        if (operationList.get(i).getNote().toString().equals(permamentOperation.getNote().toString() + "(stala operacja)")){
                            operations.add(operationList.get(i));
                        }
                    }
                }
                for (int i=0; i < operations.size(); i++){
                    databaseHelper.deleteOperation(operations.get(i));
                }
                PermamentOperation beforeOperation = permamentOperation;
                int radioButtonId= radioGroup.getCheckedRadioButtonId();
                radioButton= (RadioButton) findViewById(radioButtonId);
                if (ischeckForm(radioButtonId)) {
                    String balance = radioButton.getText().toString();
                    permamentOperation.setPrice(Double.parseDouble(price.getText().toString()));
                    permamentOperation.setCategory(categoryList.getSelectedItem().toString());
                    permamentOperation.setBalance(balance);
                    permamentOperation.setTitle(title.getText().toString());
                    permamentOperation.setDate(date.getText().toString());
                    permamentOperation.setNote(note.getText().toString());
                    permamentOperation.setEndDate(endDate.getText().toString());
                    permamentOperation.setFrequency(frequency.getSelectedItem().toString());
                    operations = getOperationList(permamentOperation);
                    //List<Operation>operations;
                    operations = getOperationList(permamentOperation);
                    int dupa = 10;
                    databaseHelper.updatePermamentOperation(permamentOperation);
                    for (int i = 0; i < operations.size(); i++) {
                        databaseHelper.addOperation(operations.get(i));
                    }

                    finish();
                }
                else{
                    Toast.makeText(EditPermamentOperationActivity.this, " nie wypełniono wszystkich pol w formularzu", Toast.LENGTH_LONG).show();
                }
                //databaseHelper.addOperation(operation);
                //databaseHelper.getOperations();
//                Intent intent = new Intent(NewOperationActivity.this, MainActivity.class);
//                startActivity(intent);
                //finish();
            }
        });


        deletePermamentOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Operation> operations = new ArrayList<>();
                for (int i=0; i < operationList.size() ; i++){
                    if (operationList.get(i).getTitle().toString().equals(permamentOperation.getTitle().toString())){
                        if (operationList.get(i).getNote().toString().equals(permamentOperation.getNote().toString() + "(stala operacja)")){
                            operations.add(operationList.get(i));
                        }
                    }
                }
                databaseHelper.deletePermamentOperation(permamentOperation);
                for (int i=0; i < operations.size(); i++){
                    databaseHelper.deleteOperation(operations.get(i));
                }
                finish();
            }
        });
    }

    private void frequencyListToSpinner(){
        String[] frequency = new String[] { "Codziennie",
                "Co tydzień", "Co dwa tygodnie", "Co trzy tygodnie", "Raz na miesiac",
                "Raz na dwa miesiace", "Raz na trzy miesiace",
                "Raz na cztery miesiace", "Co pół roku", "Co rok", "Co dwa lata" };
        ArrayList<String> frequencyList = new ArrayList<String>();
        frequencyList.addAll(Arrays.asList(frequency));
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, frequency);
        this.frequency.setAdapter(adapter);
        this.adapter2=adapter;
    }

    private void categoryListToSpinner(){
        List<Category>categories = this.databaseHelper.getCategories();
        //List<Category>categories = new LinkedList<>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories);
        categoryList.setAdapter(adapter);
        this.adapter=adapter;
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    private List<Operation> getOperationList(PermamentOperation permamentOperation){
        List<Operation>operations = new ArrayList<>();
        Calendar calendar1 = new GregorianCalendar();
        Calendar calendar2 = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = simpleDateFormat.parse(permamentOperation.getDate());
            calendar1.setTime(date);
            date = simpleDateFormat.parse(permamentOperation.getEndDate());
            calendar2.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //calendar.set(2008, 8, 1);
        //cal2.set(2008, 9, 31);
        int days = daysBetween(calendar1.getTime(),calendar2.getTime());
        String dayInObject = permamentOperation.getDate().substring(0,2);
        int operationDay = Integer.parseInt(dayInObject);
        String monthInObject = permamentOperation.getDate().substring(3,5);
        int operationMonth = Integer.parseInt(monthInObject);
        String yearInObject = permamentOperation.getDate().substring(6,10);
        int operationYear = Integer.parseInt(yearInObject);

        String permamentDayInObject = permamentOperation.getEndDate().substring(0,2);
        int permamentOperationDay = Integer.parseInt(permamentDayInObject);
        String permamentMonthInObject = permamentOperation.getEndDate().substring(3,5);
        int permamentOperationMonth = Integer.parseInt(permamentMonthInObject);
        String permamentYearInObject = permamentOperation.getEndDate().substring(6,10);
        int permamentOperationYear = Integer.parseInt(permamentYearInObject);
        String newDate;
        int months=0;
        int tmpday = operationDay;
        int count = 0;
        int years = 0;
        switch(permamentOperation.getFrequency()){
            case "Codziennie":
                for(int i=0; i <= days; i++){

                    if( operationMonth == 12 && operationDay > 31){
                        operationMonth = 1;
                        operationDay = 1;
                        operationYear++;
                    }

                    if (operationMonth == 1 || operationMonth == 3 ||operationMonth == 5 || operationMonth == 7 || operationMonth == 8 || operationMonth == 10){
                        if (operationDay > 31){
                            operationDay=1;
                            operationMonth++;
                        }
                    }
                    else if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay=1;
                            operationMonth++;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay=1;
                            operationMonth++;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);
                    operationDay += 1;
                }
                break;
            case "Co tydzień":
                int tmp=0;
                for(int i=0; i <= days; i+=7){

                    if( operationMonth == 12 && operationDay > 31){
                        operationMonth = 1;
                        operationDay = operationDay - 31;
                        operationYear++;
                    }

                    if (operationMonth == 1 || operationMonth == 3 ||operationMonth == 5 || operationMonth == 7 || operationMonth == 8 || operationMonth == 10 || operationMonth == 12){
                        if (operationDay > 31){
                            operationDay=operationDay-31;
                            operationMonth++;
                            if( operationMonth == 12){
                                operationMonth=1;
                                operationYear++;
                            }
                        }
                    }
                    else if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay=operationDay-30;
                            operationMonth++;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay=operationDay-28;
                            operationMonth++;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);
                    tmp = operationDay;
                    operationDay += 7;
                }
                break;
            case "Co dwa tygodnie":
                for(int i=0; i <= days; i+=14){

                    if( operationMonth == 12 && operationDay > 31){
                        operationMonth = 1;
                        operationDay = operationDay -31;
                        operationYear++;
                    }

                    if (operationMonth == 1 || operationMonth == 3 ||operationMonth == 5 || operationMonth == 7 || operationMonth == 8 || operationMonth == 10 || operationMonth == 12){
                        if (operationDay > 31){
                            operationDay=operationDay-31;
                            operationMonth++;
                            if( operationMonth == 12){
                                operationMonth=1;
                                operationYear++;
                            }
                        }
                    }
                    else if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay=operationDay-30;
                            operationMonth++;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay=operationDay-28;
                            operationMonth++;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote()  + "(stala operacja)");

                    operations.add(operation);
                    tmp = operationDay;
                    operationDay += 14;
                }
                break;
            case "Co trzy tygodnie":
                for(int i=0; i <= days; i+=21){

                    if( operationMonth == 12 && operationDay > 31){
                        operationMonth = 1;
                        operationDay = operationDay -31;
                        operationYear++;
                    }

                    if (operationMonth == 1 || operationMonth == 3 ||operationMonth == 5 || operationMonth == 7 || operationMonth == 8 || operationMonth == 10 || operationMonth == 12){
                        if (operationDay > 31){
                            operationDay=operationDay-31;
                            operationMonth++;
                            if( operationMonth == 12){
                                operationMonth=operationMonth-31;
                                operationYear++;
                            }
                        }
                    }
                    else if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay=operationDay-30;
                            operationMonth++;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay=operationDay-28;
                            operationMonth++;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);
                    tmp = operationDay;
                    operationDay += 21;
                }
                break;
            case "Raz na miesiac":
                if(permamentOperationYear == operationYear){
                    months = permamentOperationMonth - operationMonth;
                }
                else if(permamentOperationYear > operationYear){
                    months = 12 * (permamentOperationYear - operationYear);
                }
                for(int i = 0; i <= months; i++){
                    if (operationMonth > 12){
                        operationMonth=1;
                        operationYear++;
                    }
                    if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay = 30;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay = 28;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }

                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);

                    operationMonth++;
                    operationDay=tmpday;
                }
                break;
            case "Raz na dwa miesiace":
                if(permamentOperationYear == operationYear){
                    months = permamentOperationMonth - operationMonth;
                }
                else if(permamentOperationYear > operationYear){
                    months = 12 * (permamentOperationYear - operationYear);
                }
                for(int i = 0; i <= months; i+=2){
                    if (operationMonth > 12){
                        operationMonth=operationMonth - 12;
                        operationYear++;
                    }
                    if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay = 30;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay = 28;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }

                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);

                    operationMonth += 2;
                    operationDay=tmpday;
                }
                break;
            case "Raz na trzy miesiace":
                if(permamentOperationYear == operationYear){
                    months = permamentOperationMonth - operationMonth;
                }
                else if(permamentOperationYear > operationYear){
                    months = 12 * (permamentOperationYear - operationYear);
                }
                for(int i = 0; i <= months; i+=3){
                    if (operationMonth > 12){
                        operationMonth=operationMonth - 12;
                        operationYear++;
                    }
                    if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay = 30;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay = 28;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }

                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);

                    operationMonth += 3;
                    operationDay=tmpday;
                }
                break;
            case "Raz na cztery miesiace":
                if(permamentOperationYear == operationYear){
                    months = permamentOperationMonth - operationMonth;
                }
                else if(permamentOperationYear > operationYear){
                    months = 12 * (permamentOperationYear - operationYear);
                }
                for(int i = 0; i <= months; i+=4){
                    if (operationMonth > 12){
                        operationMonth = operationMonth - 12;
                        operationYear++;
                    }
                    if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay = 30;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay = 28;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }

                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);

                    operationMonth += 4;
                    operationDay=tmpday;
                }
                break;
            case "Co pół roku":
                if(permamentOperationYear == operationYear){
                    months = permamentOperationMonth - operationMonth;
                }
                else if(permamentOperationYear > operationYear){
                    months = 12 * (permamentOperationYear - operationYear);
                }
                for(int i = 0; i <= months; i += 5){
                    if (operationMonth > 12){
                        operationMonth = operationMonth - 12;
                        operationYear++;
                    }
                    if( operationMonth == 4 || operationMonth == 6 || operationMonth == 9 || operationMonth == 11){
                        if (operationDay > 30){
                            operationDay = 30;
                        }
                    }
                    else if (operationMonth == 2){
                        if (operationDay > 28){
                            operationDay = 28;
                        }
                    }
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }

                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);

                    operationMonth += 5;
                    operationDay=tmpday;
                }
                break;
            case "Co rok":
                if(permamentOperationYear == operationYear){
                    years = 0;
                }
                else if(permamentOperationYear > operationYear){
                    years = permamentOperationYear - operationYear;
                }
                for(int i = 0; i <= years; i++){
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }

                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);

                    operationYear++;
                    operationDay=tmpday;
                }
                break;
            case "Co dwa lata":
                if(permamentOperationYear == operationYear){
                    years = 0;
                }
                else if(permamentOperationYear > operationYear){
                    years = permamentOperationYear - operationYear;
                }
                for(int i = 0; i <= years; i += 2){
                    if(operationDay<10 && operationMonth<10) {
                        newDate = "0" + operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else if(operationDay<10){
                        newDate = "0" + operationDay + "/" + operationMonth + "/" + operationYear;
                    }
                    else if(operationMonth<10){
                        newDate = operationDay + "/" + "0" + operationMonth + "/" + operationYear;
                    }
                    else {
                        newDate = operationDay + "/" + operationMonth + "/" + operationYear;
                    }

                    Operation operation = new Operation();
                    operation.setPrice(permamentOperation.getPrice());
                    operation.setCategory(permamentOperation.getCategory());
                    operation.setBalance(permamentOperation.getBalance());
                    operation.setTitle(permamentOperation.getTitle());
                    operation.setDate(newDate);
                    operation.setNote(permamentOperation.getNote() + "(stala operacja)");

                    operations.add(operation);

                    operationYear += 2;
                    operationDay=tmpday;
                }
                break;
        }
        List<Operation>testowaLista = new ArrayList<>();
        for (int i=0; i < operations.size() ; i++){
            if (operations.get(i).getTitle().toString().equals(permamentOperation.getTitle().toString())){
                if (operations.get(i).getNote().toString().equals(permamentOperation.getNote().toString() + "(stala operacja)")){
                    testowaLista.add(operations.get(i));
                }
            }
        }
        return operations;
    }

    private void dataToForm(PermamentOperation operation){
        price.setText(Double.toString(operation.getPrice()));
        int spinnerPosition = getCategoryPositionInSpinner(operation);
        int frequencyPosition = getFrequencyPositionInSpinner(operation);
        categoryList.setSelection(spinnerPosition);
        frequency.setSelection(frequencyPosition);
        checkGoodRadioButton(operation);
        title.setText(operation.getTitle());
        date.setText(operation.getDate());
        note.setText(operation.getNote());
        endDate.setText(operation.getEndDate());
    }
    private int getCategoryPositionInSpinner(PermamentOperation operation){
        for(int i=0; i<adapter.getCount(); i++){
            if(operation.getCategory().equals(adapter.getItem(i).toString())){
                return i;
            }
        }
        return 0;
    }

    private int getFrequencyPositionInSpinner(PermamentOperation operation){
        for(int i=0; i<adapter2.getCount(); i++){
            if(operation.getFrequency().equals(adapter2.getItem(i).toString())){
                return i;
            }
        }
        return 0;
    }

    private void checkGoodRadioButton(PermamentOperation operation){
        if(operation.getBalance().equals(minusBalance)){
            radioGroup.check(R.id.radioEdit1);
        }
        else {
            radioGroup.check(R.id.radioEdit2);
        }
    }

    private boolean ischeckForm(int radioButtonId){
        if (price.getText().toString().equals("") || categoryList.getSelectedItem().toString().equals("")
                ||  title.getText().toString().equals("") || date.getText().toString().equals("") || endDate.getText().toString().equals("") || frequency.getSelectedItem().toString().equals("")
                || note.getText().toString().equals("") || radioButtonId <0){
            return false;
        }
        return true;
    }
}

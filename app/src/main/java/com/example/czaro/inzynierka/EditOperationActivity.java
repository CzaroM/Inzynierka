package com.example.czaro.inzynierka;

import android.app.DatePickerDialog;
import android.graphics.Region;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditOperationActivity extends AppCompatActivity {

    private EditText priceEdit;
    private Spinner categoryList;
    private EditText titleEdit;
    private EditText dateEdit;
    private EditText noteEdit;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Button buttonEdit;
    private Button saveOperationButtonEdit;
    private Button deleteOperationButton;
    private RadioGroup radioGroupEdit;
    private RadioButton radioButtonEdit;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter adapter;
    private int operationID;
    private static final String minusBalance="Wydatek";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operation);
        this.databaseHelper = new DatabaseHelper(this);
        this.priceEdit = (EditText) findViewById(R.id.priceEdit);
     //   this.priceEdit.setText("0.00");
        this.categoryList=(Spinner) findViewById(R.id.categoryListEdit);
        this.titleEdit = (EditText) findViewById(R.id.titleEdit);
        this.noteEdit = (EditText) findViewById(R.id.noteEdit);
        this.dateEdit = (EditText) findViewById(R.id.dateEdit);
        this.buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(EditOperationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        if(mDayOfMonth<10 && mMonth+1<10) {
                            dateEdit.setText(("0" + mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                        }
                        else if(mDayOfMonth<10){
                            dateEdit.setText(("0" + mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                        }
                        else if(mMonth+1<10){
                            dateEdit.setText((mDayOfMonth + "/" + "0" + (mMonth + 1) + "/" + mYear));
                        }
                        else {
                            dateEdit.setText((mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear));
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        saveOperationButtonEdit = (Button) findViewById(R.id.saveOperationButtonEdit);
        this.deleteOperationButton = (Button) findViewById(R.id.deleteOperationButton);
        radioGroupEdit = (RadioGroup) findViewById(R.id.radioGroup2Edit);
        this.operationID =getIntent().getExtras().getInt("operationID");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Operation operation=databaseHelper.getOperation(operationID);
        List<Category> categories = getCategoriesFromDataBase();
        categoryListToSpinner(categories);
        dataToForm(operation);
        clickUpdateButton();
        clickDeleteButton(operation);
    }

    private List<Category> getCategoriesFromDataBase(){
        List<Category> categories = new ArrayList<>();
        return categories;
    }

    private void categoryListToSpinner(List<Category>categories){
        categories=this.databaseHelper.getCategories();
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories);
        categoryList.setAdapter(adapter);
    }

    private void dataToForm(Operation operation){
        priceEdit.setText(Double.toString(operation.getPrice()));
        int spinnerPosition = getCategoryPositionInSpinner(operation);
        categoryList.setSelection(spinnerPosition);
        checkGoodRadioButton(operation);
        titleEdit.setText(operation.getTitle());
        dateEdit.setText(operation.getDate());
        noteEdit.setText(operation.getNote());
    }

    private int getCategoryPositionInSpinner(Operation operation){
        for(int i=0; i<adapter.getCount(); i++){
            if(operation.getCategory().equals(adapter.getItem(i).toString())){
                return i;
            }
        }
        return 0;
    }

    private void checkGoodRadioButton(Operation operation){
        if(operation.getBalance().equals(minusBalance)){
            radioGroupEdit.check(R.id.radioEdit1Edit);
        }
        else {
            radioGroupEdit.check(R.id.radioEdit2Edit);
        }
    }

    private void clickUpdateButton(){
        saveOperationButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId= radioGroupEdit.getCheckedRadioButtonId();
                radioButtonEdit= (RadioButton) findViewById(radioButtonId);
                if (ischeckForm(radioButtonId)) {
                    String balance = radioButtonEdit.getText().toString();
                    Operation operation = new Operation(operationID,
                            Double.parseDouble(priceEdit.getText().toString()),
                            categoryList.getSelectedItem().toString(), balance,
                            titleEdit.getText().toString(), dateEdit.getText().toString(),
                            noteEdit.getText().toString());
                    databaseHelper.updateOperation(operation);
                    //dodac informacje o edycji
                    finish();
                }
                else{
                    Toast.makeText(EditOperationActivity.this, " nie wypełniono wszystkich pol w formularzu", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clickDeleteButton(final Operation operation){
        deleteOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteOperation(operation);
                // dodac informacjeo usunieciu
                finish();
            }
        });
    }

    private boolean ischeckForm(int radioButtonId){
        if (priceEdit.getText().toString().equals("") || categoryList.getSelectedItem().toString().equals("")
                ||  titleEdit.getText().toString().equals("") || dateEdit.getText().toString().equals("")
                || noteEdit.getText().toString().equals("") || radioButtonId <0){
            return false;
        }
        return true;
    }
}

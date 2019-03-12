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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewOperationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText price;
    private Spinner categoryList;
    private EditText title;
    private EditText date;
    private EditText note;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Button button;
    private Button saveOperationButton;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);
        databaseHelper = new DatabaseHelper(this);
        price = (EditText) findViewById(R.id.price);
      //  price.setText("0.00");
        categoryList=(Spinner) findViewById(R.id.categoryList);
        title = (EditText) findViewById(R.id.title);
        note = (EditText) findViewById(R.id.note);
        date= (EditText) findViewById(R.id.filterStartDate);
        button = (Button) findViewById(R.id.filterStartDateCalendarButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(NewOperationActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        saveOperationButton = (Button) findViewById(R.id.saveOperationButton);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Pozniej wstawic tutaj kategorie z bazy.
//        String[] items = new String[]{"1", "2", "three"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        categoryList.setAdapter(adapter);
        List<Category>categories = getCategoriesFromDataBase();
        categoryListToSpinner(categories);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
//        double priceValue =  Double.parseDouble(price.getText().toString());
//        String formattedValue = decimalFormat.format(priceValue);
//        price.setText(formattedValue+" zł");

        saveOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId= radioGroup.getCheckedRadioButtonId();
                radioButton= (RadioButton) findViewById(radioButtonId);
                if (ischeckForm(radioButtonId)) {
                    String balance = radioButton.getText().toString();
                        Operation operation = new Operation(Double.parseDouble(price.getText().toString()),
                                categoryList.getSelectedItem().toString(), balance,
                                title.getText().toString(), date.getText().toString(),
                                note.getText().toString());
                        databaseHelper.addOperation(operation);
                        databaseHelper.getOperations();
                    finish();
                }
                else{
                    Toast.makeText(NewOperationActivity.this, " nie wypełniono wszystkich pol w formularzu", Toast.LENGTH_LONG).show();
                }
//                Intent intent = new Intent(NewOperationActivity.this, MainActivity.class);
//                startActivity(intent);

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {

    }

    private List<Category> getCategoriesFromDataBase(){
        List<Category> categories = new ArrayList<>();
//        categories=this.databaseHelper.getCategories();
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories);
//        categoryList.setAdapter(adapter);
        return categories;
    }

    private void categoryListToSpinner(List<Category>categories){
        categories=this.databaseHelper.getCategories();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categories);
        categoryList.setAdapter(adapter);
    }

    private boolean ischeckForm(int radioButtonId){
        if (price.getText().toString().equals("") || categoryList.getSelectedItem().toString().equals("")
                ||  title.getText().toString().equals("") || date.getText().toString().equals("")
                || note.getText().toString().equals("") || radioButtonId <0){
            return false;
        }
        return true;
    }
}

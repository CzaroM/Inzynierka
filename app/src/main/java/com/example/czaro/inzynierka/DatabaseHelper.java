package com.example.czaro.inzynierka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME1="operations";
    public static final String TABLE_NAME2="categories";
    public static final String TABLE_NAME3="permamentOperations";
    public DatabaseHelper(Context context) {
        super(context, "finance.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME1 + "(" +
                "id integer primary key autoincrement," +
                "price real," +
                "category text," +
                "balance text," +
                "title text," +
                "date text," +
                "note text);" +"");

        db.execSQL("create table "+TABLE_NAME2+"(" +
                "id integer primary key autoincrement," +
                "name text);" +"");

        db.execSQL("create table " + TABLE_NAME3 + "(" +
                "id integer primary key autoincrement," +
                "price real," +
                "category text," +
                "balance text," +
                "title text," +
                "date text," +
                "note text," +
                "frequency text," +
                "endDate text);" +"");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addOperation(Operation operation){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("price", operation.getPrice());
        contentValues.put("category", operation.getCategory().toString());
        contentValues.put("balance", operation.getBalance().toString());
        contentValues.put("title", operation.getTitle().toString());
        contentValues.put("date", operation.getDate().toString());
        contentValues.put("note", operation.getNote().toString());
        database.insertOrThrow(TABLE_NAME1,null, contentValues);
    }

    public void updateOperation(Operation operation){
        SQLiteDatabase database= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("price", operation.getPrice());
        contentValues.put("category", operation.getCategory().toString());
        contentValues.put("balance", operation.getBalance().toString());
        contentValues.put("title", operation.getTitle().toString());
        contentValues.put("date", operation.getDate().toString());
        contentValues.put("note", operation.getNote().toString());
        database.update(TABLE_NAME1, contentValues, "id=?",
                new String[] {(Integer.toString(operation.getId()))});
    }

    public void deleteOperation(Operation operation){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME1, "id = ?", new String[] {(Integer.toString(operation.getId()))});
    }

    public Operation getOperation(int operationID){
        Operation operation = new Operation();
        String[] columns = {"id", "price", "category", "balance", "title", "date", "note"};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME1, columns, "id=?", new String[] {(Integer.toString(operationID))}, null, null,null);
        while(cursor.moveToNext()){
            operation.setId(cursor.getInt(0));
            operation.setPrice(cursor.getDouble(1));
            operation.setCategory(cursor.getString(2));
            operation.setBalance(cursor.getString(3));
            operation.setTitle(cursor.getString(4));
            operation.setDate(cursor.getString(5));
            operation.setNote(cursor.getString(6));
        }
        return operation;
    }

    public List<Operation> getOperations(){
        List<Operation> operations = new LinkedList<Operation>();
        String[] columns = {"id", "price", "category", "balance", "title", "date", "note"};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME1, columns, null, null, null, null,null);
        while(cursor.moveToNext()){
            Operation operation = new Operation();
            operation.setId(cursor.getInt(0));
            operation.setPrice(cursor.getDouble(1));
            operation.setCategory(cursor.getString(2));
            operation.setBalance(cursor.getString(3));
            operation.setTitle(cursor.getString(4));
            operation.setDate(cursor.getString(5));
            operation.setNote(cursor.getString(6));
            operations.add(operation);
        }
        return operations;
    }

    public List<Operation>getOperations(String columnName, String columnValue){
        List<Operation> operations = new LinkedList<Operation>();
        String[] columns = {"id", "price", "category", "balance", "title", "date", "note"};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME1, columns, columnName+"=?", new String[] {columnValue}, null, null,null);
        while(cursor.moveToNext()){
            Operation operation = new Operation();
            operation.setId(cursor.getInt(0));
            operation.setPrice(cursor.getDouble(1));
            operation.setCategory(cursor.getString(2));
            operation.setBalance(cursor.getString(3));
            operation.setTitle(cursor.getString(4));
            operation.setDate(cursor.getString(5));
            operation.setNote(cursor.getString(6));
            operations.add(operation);
        }
        return operations;
    }

    public void addCategory(Category category){
        // dodac sprawdzenie czy kategoria juz istnieje
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", category.getName().toString());
        database.insertOrThrow(TABLE_NAME2,null, contentValues);
    }

    public List<Category> getCategories(){
        List<Category> categories = new LinkedList<Category>();
        String[] columns = {"id", "name"};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME2, columns, null, null, null, null,null);
        while(cursor.moveToNext()){
            Category category = new Category();
            category.setId(cursor.getInt(0));
            category.setName(cursor.getString(1));
            categories.add(category);
        }
        return categories;
    }

    public void deleteCategory(Category category){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME2, "id = ?", new String[] {(Integer.toString(category.getId()))});
    }

    public void addPermamentOperation(PermamentOperation operation){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("price", operation.getPrice());
        contentValues.put("category", operation.getCategory().toString());
        contentValues.put("balance", operation.getBalance().toString());
        contentValues.put("title", operation.getTitle().toString());
        contentValues.put("date", operation.getDate().toString());
        contentValues.put("note", operation.getNote().toString());
        contentValues.put("frequency", operation.getFrequency().toString());
        contentValues.put("endDate", operation.getEndDate().toString());
        database.insertOrThrow(TABLE_NAME3,null, contentValues);
    }

    public void updatePermamentOperation(PermamentOperation operation){
        SQLiteDatabase database= getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("price", operation.getPrice());
        contentValues.put("category", operation.getCategory().toString());
        contentValues.put("balance", operation.getBalance().toString());
        contentValues.put("title", operation.getTitle().toString());
        contentValues.put("date", operation.getDate().toString());
        contentValues.put("note", operation.getNote().toString());
        contentValues.put("frequency", operation.getFrequency().toString());
        contentValues.put("endDate", operation.getEndDate().toString());
        database.update(TABLE_NAME3, contentValues, "id=?",
                new String[] {(Integer.toString(operation.getId()))});
    }

    public void deletePermamentOperation(PermamentOperation operation){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME3, "id = ?", new String[] {(Integer.toString(operation.getId()))});
    }

    public List<PermamentOperation> getPermamentOperations(){
        List<PermamentOperation> operations = new LinkedList<PermamentOperation>();
        String[] columns = {"id", "price", "category", "balance", "title", "date", "note", "frequency", "endDate"};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME3, columns, null, null, null, null,null);
        while(cursor.moveToNext()){
            PermamentOperation operation = new PermamentOperation();
            operation.setId(cursor.getInt(0));
            operation.setPrice(cursor.getDouble(1));
            operation.setCategory(cursor.getString(2));
            operation.setBalance(cursor.getString(3));
            operation.setTitle(cursor.getString(4));
            operation.setDate(cursor.getString(5));
            operation.setNote(cursor.getString(6));
            operation.setFrequency(cursor.getString(7));
            operation.setEndDate(cursor.getString(8));
            operations.add(operation);
        }
        return operations;
    }

    public PermamentOperation getParmamentOperation(int operationID){
        PermamentOperation operation = new PermamentOperation();
        String[] columns = {"id", "price", "category", "balance", "title", "date", "note", "frequency", "endDate"};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME3, columns, "id=?", new String[] {(Integer.toString(operationID))}, null, null,null);
        while(cursor.moveToNext()){
            operation.setId(cursor.getInt(0));
            operation.setPrice(cursor.getDouble(1));
            operation.setCategory(cursor.getString(2));
            operation.setBalance(cursor.getString(3));
            operation.setTitle(cursor.getString(4));
            operation.setDate(cursor.getString(5));
            operation.setNote(cursor.getString(6));
            operation.setFrequency(cursor.getString(7));
            operation.setEndDate(cursor.getString(8));
        }
        return operation;
    }
}

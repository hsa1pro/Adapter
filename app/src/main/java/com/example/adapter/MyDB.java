package com.example.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {
    public static final String TableName = "ContactTable";
    public static final String Image = "Image";
    public static final String Id = "Id";
    public static final String Name = "Name";
    public static final String Phone = "PhoneNumber";

    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context,name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "Create table "+TableName+"(" +
                Id+" integer primary key, " +
                Image+" TEXT, " +
                Name + " TEXT, " +
                Phone + " TEXT ) ";
        db.execSQL(sqlCreate);
//        addContact(new Contact(1, "img1", "Nguyen Van Quynh", "189481646"));
//        addContact(new Contact(2, "img2", "Tran Thi Be", "26265646"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TableName);
        onCreate(db);
    }
    public ArrayList<Contact> getAllContact(){
        ArrayList<Contact> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TableName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor!=null){
            while (cursor.moveToNext()){
                Contact contact = new Contact(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));

                list.add(contact);
            }
        }
        return list;
    }
    public void addContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Id, contact.getId());
        value.put(Image, contact.getImages());
        value.put(Name, contact.getName());
        value.put(Phone, contact.getPhone());
        db.insert(TableName, null, value);
        db.close();
    }
    public void updateContact( int id, Contact contact){

    }
}

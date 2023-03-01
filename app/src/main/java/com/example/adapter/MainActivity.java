package com.example.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> ContactList;
    private Adapter ListAdapter;
    private EditText etSearch;
    private ListView lstContact;
    private FloatingActionButton btnAdd;
    private int selectedItemId;
    private  MyDB db;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactList = new ArrayList<>();
//        ContactList.add(new Contact(1, "img1", "Nguyen Van Quynh", "189481646"));
//        ContactList.add(new Contact(2, "img2", "Tran Thi Be", "26265646"));
//        ContactList.add(new Contact(3, "img3", "An", "3677849646"));
//        ContactList.add(new Contact(4, "img4", "Le thi Van Anh ", "8677849646"));

        //tao moi csdl
        db = new MyDB(this, "ContactDB", null, 2);
//        db.close();
//        this.deleteDatabase("ContactDB");
        // them du lieu LAN DAU
//        db.addContact(new Contact(1, "img1", "Nguyen Van Quynh", "189481646"));
//        db.addContact(new Contact(2, "img2", "Tran Thi Be", "26265646"));
//        db.addContact(new Contact(3, "img3", "An", "3677849646"));
//        db.addContact(new Contact(4, "img4", "Le thi Van Anh ", "8677849646"));
//
       ContactList = db.getAllContact();

        ListAdapter = new Adapter(ContactList, this);
        etSearch = findViewById(R.id.etSearch);
        lstContact = findViewById((R.id.lstContact));
        btnAdd = findViewById(R.id.btnAdd);
        registerForContextMenu(lstContact);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. Tao intent de mo activity
                Intent intent = new Intent(MainActivity.this, SubActivite.class);

                //2. Truyen du lieu sang sub activity bang bundle
                //3. Mo subactivity bang cach goi ham startactivity hoac startactivityforresult
                startActivityForResult(intent, 100);
            }
        });

        lstContact.setAdapter(ListAdapter);
        lstContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItemId = i;
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ListAdapter.getFilter().filter(charSequence.toString());
                ListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.actionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public class sortName implements Comparator<Contact>{

        @Override
        public int compare(Contact ct1, Contact ct2) {
            String name1 = ct1.getName().split(" ")[ct1.getName().split(" ").length - 1];
            String name2 = ct2.getName().split(" ")[ct2.getName().split(" ").length - 1];
            return name1.compareTo(name2);
        }
    }
    public class sortPhone implements  Comparator<Contact>{

        @Override
        public int compare(Contact ct1, Contact ct2) {
            return ct1.getPhone().compareTo(ct2.getPhone());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b = data.getExtras();
        int id = b.getInt("Id");
        String name = b.getString("Name");
        String phone = b.getString("Phone");
        Contact newcontact = new Contact(id, "Image", name, phone);
        if (requestCode ==100 && resultCode == 150){
            //Them moi
            ContactList.add(newcontact);
            ListAdapter = new Adapter(ContactList, this);
            lstContact.setAdapter(ListAdapter);

        }
        else if (requestCode == 200 && resultCode == 150){
            //sua
            ContactList.remove(selectedItemId);
            ContactList.add(selectedItemId,newcontact);
            ListAdapter = new Adapter(ContactList, this);
            lstContact.setAdapter(ListAdapter);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Contact c = ContactList.get(selectedItemId);
        switch (item.getItemId()){
            case R.id.mnuEdit:
                //1. Tao intent de mo activity
                Intent intent = new Intent(MainActivity.this, SubActivite.class);

                //2. Truyen du lieu sang sub activity bang bundle
                Bundle b = new Bundle();
                b.putInt("Id", c.getId());
                b.putString("Image",c.getImages());
                b.putString("Name", c.getName());
                b.putString("Phone", c.getPhone());
                intent.putExtras(b);
                //3. Mo subactivity bang cach goi ham startactivity hoac startactivityforresult
                startActivityForResult(intent, 200);
                break;
            case R.id.mnuDelete:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Bạn có muốn xóa " + ContactList.get(selectedItemId).getName())
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContactList.remove(selectedItemId);
                                ListAdapter = new Adapter(ContactList, MainActivity.this);
                                lstContact.setAdapter(ListAdapter);
                            }
                        }).show();
                break;
            case R.id.mnuCall:
                Intent inCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + c.getPhone()));
                startActivity(inCall);
                break;
            case R.id.mnuSms:
                Intent inSms = new Intent(Intent.ACTION_SEND);
                inSms.setType("text/plain");
                inSms.putExtra(Intent.EXTRA_TEXT , c.getPhone());
                startActivity(inSms);
                break;
            case R.id.mnuSendemail:
                Intent inUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.com/jjlj"+c.getPhone()));
                startActivity(inUrl);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.mnuSortName:
                //Sapxep theo ten
                Collections.sort(ContactList,new sortName());
                ListAdapter = new Adapter(ContactList, this);
                lstContact.setAdapter(ListAdapter);
                break;
            case R.id.mnuSortPhone:
                //Sapxeptheophone
                Collections.sort(ContactList,new sortPhone());
                ListAdapter = new Adapter(ContactList, this);
                lstContact.setAdapter(ListAdapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

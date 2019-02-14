package com.example.phonecall;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int pos;
    private int PhoneCall = 1;
    Button request;
    EditText Phone ;
    ListView PhoneNumbers;
    ArrayList <String> Numbers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void call()
    {
        Phone = (EditText) findViewById(R.id.phone);
        //String phonenumber = Phone.getText().toString();
        Intent calltel = new Intent(Intent.ACTION_CALL );
        calltel.setData(Uri.parse("tel:"+Numbers.get(pos)));
        if(ActivityCompat.checkSelfPermission(MainActivity.this , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            return;
        else
            startActivity(calltel);

    }
    public void CheckPhone()
    {
        //request = (Button) findViewById(R.id.call);
        if(ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED )
        {
            call();
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.CALL_PHONE))
            {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission needed").setMessage("You should allow permission to can make a call")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String [] {Manifest.permission.CALL_PHONE} , PhoneCall);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.CALL_PHONE} , PhoneCall);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PhoneCall)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
            {
                //Toast.makeText(MainActivity.this , "Permission GRANTED" , Toast.LENGTH_LONG).show();
                call();
            }
            else
            {
                Toast.makeText(MainActivity.this , "Permission Denied" , Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addToList(View v)
    {
        PhoneNumbers = (ListView) findViewById(R.id.numbers);
        Phone = (EditText) findViewById(R.id.phone);
        String phonenumber = Phone.getText().toString();
        if(!phonenumber.isEmpty())
            Numbers.add(phonenumber);
        Phone.setText("");
        ArrayAdapter arr = new ArrayAdapter(MainActivity.this , android.R.layout.simple_list_item_1 , Numbers);
        PhoneNumbers.setAdapter(arr);
        PhoneNumbers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                CheckPhone();
            }
        });
    }
}

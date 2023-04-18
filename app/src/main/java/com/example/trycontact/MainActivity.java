package com.example.trycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView tvPhone, tvName , tvId;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btnShowAll);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowContactsPicker();
            }

        });
        tvId = (TextView) findViewById(R.id.tvId);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
    }










        public void ShowContactsPicker(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,0);
    }

    protected void onActivityResult(int requestCode , int resultCode , Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 0) {
                Cursor cursor;
                CursorLoader cursorLoader = new CursorLoader(this, data.getData(), null, null, null, null);
                cursor = cursorLoader.loadInBackground();
                cursor.moveToFirst();

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                tvId.setText(id);
                tvName.setText(name);

                if (hasPhone.equalsIgnoreCase("1")) { //בדיקה אם יש לאיש הקשר מספר טלפון
                    String phoneNum = " ";
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

                    if (phones.moveToNext()) {
                        phoneNum = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                    cursor.close();
                    tvPhone.setText(phoneNum);
                    Toast.makeText(this, "יש מספר טלפון", Toast.LENGTH_SHORT).show();
                }


                else tvPhone.setText("לא קיים מספר טלפון");



            }
        }



    }
}
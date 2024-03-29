package com.example.mywhatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        userList=new ArrayList<>();

        initializeRecycleView();
        getContactList();
    }
    private  void getContactList(){
        Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null ,null,null );
        while(phones.moveToNext()){
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserObject mContact=new UserObject(name,phone);
            userList.add(mContact);
            mUserListAdapter.notifyDataSetChanged();
        }
    }

    private void initializeRecycleView() {
        mUserList=findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);//to scrol view work fine
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager =new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter =new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    }
}

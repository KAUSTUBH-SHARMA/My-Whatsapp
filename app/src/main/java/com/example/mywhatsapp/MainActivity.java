package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText mPhoneNumber,mCode;
    private Button mSend;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this); //used so that we can use firebase in our code

        userIsLoggedIn();  //to check if user is logged in from before then we dont want to again do all login process

        mPhoneNumber=findViewById(R.id.phoneNumber);
        mCode=findViewById(R.id.mCode);
        mSend=findViewById(R.id.mSend);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVerificationId != null)
                    verifyPhoneNumberWithCode();
                else
                    startPhoneNumberVerification();  //our function that we will create to verify phone number
            }
        });

        mCallBacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {  //this will be sent wenever the code is sent to the user
                super.onCodeSent(verificationId, forceResendingToken);

                mVerificationId=verificationId;
                mSend.setText("Verify Code"); //we have to verify this code we will do this in verify phone number with code method
            }
        };

    }

    private void verifyPhoneNumberWithCode(){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,mCode.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    userIsLoggedIn();
            }
        });
    }

    private void userIsLoggedIn() {  //if the user is logged in we will move to the next activity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null){
            startActivity(new Intent(getApplicationContext(),MainPageActivity.class));   //start the new activity
            finish();
            return; //finish and return so that the user cant return  to this page
        }
    }

    private void startPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber.getText().toString(),
                60, //time the user have to reciever the code
                TimeUnit.SECONDS,  //unit in which we have specified time
                this,
                mCallBacks  //it will handle what happens next falior or success
        );
    }
}

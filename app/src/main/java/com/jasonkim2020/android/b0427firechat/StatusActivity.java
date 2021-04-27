package com.jasonkim2020.android.b0427firechat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private EditText mStatus;
    private Button mSavebtn;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    //Progress
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);


        mToolbar = (Toolbar) findViewById(R.id.status_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value");

        mStatus = (EditText) findViewById(R.id.status_input);
        mStatus.setText(status_value);
        mSavebtn = (Button) findViewById(R.id.status_save_btn);
        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Progress
                mProgress = new ProgressDialog(StatusActivity.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.show();

//                String status = mStatus.getEditText().getText().toString();
                String status = mStatus.getText().toString();
                Toast.makeText(StatusActivity.this, status, Toast.LENGTH_SHORT).show();

                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgress.dismiss();
                        }else{
                            Toast.makeText(StatusActivity.this, "There was some error in saving Changes.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
package com.example.jaikh.movies;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {

    private String email,feedback;
    private EditText Email,Feedback;
    private Snackbar snackbar;
    private Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Email = (EditText)findViewById(R.id.feedback_email);
        Feedback = (EditText) findViewById(R.id.feedback_text);
        submit_button = (Button) findViewById(R.id.feedback_submit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submitReview()
    {
        email = Email.getText().toString();
        feedback = Feedback.getText().toString();

        if(email.isEmpty() || feedback.isEmpty())
        {
            snackbar = Snackbar.make(findViewById(R.id.activity_feedback), R.string.sb_empty_fields, Snackbar.LENGTH_SHORT);
            snackbar.setAction("dismiss",null).show();
        }
        else
        {
            FirebaseDatabase reviewDatabase = FirebaseDatabase.getInstance();
            DatabaseReference feedbackReference = reviewDatabase.getReference().child("feedbacks").push();
            feedbackReference.child("EmailId").setValue(email);
            feedbackReference.child("Feedback").setValue(feedback);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.activity_feedback).getWindowToken(), 0);
            snackbar = Snackbar.make(findViewById(R.id.activity_feedback), R.string.sb_submission_successful, Snackbar.LENGTH_SHORT);
            snackbar.setAction("dismiss",null).show();
            new CountDownTimer(1000,1000)
            {
                public void onFinish() {
                    // When timer is finished
                    // Execute your code here
                    startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished    The amount of time until finished.
                }
            }.start();
        }
    }
}

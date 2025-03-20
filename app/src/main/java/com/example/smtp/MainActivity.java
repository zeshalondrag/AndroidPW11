package com.example.smtp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText emailTo, subject, message;
    private MailSender mailSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailTo = findViewById(R.id.emailTo);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        Button sendButton = findViewById(R.id.sendButton);

        mailSender = new MailSender();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        String to = emailTo.getText().toString().trim();
        String emailSubject = subject.getText().toString().trim();
        String emailMessage = message.getText().toString().trim();

        if (to.isEmpty() || emailSubject.isEmpty() || emailMessage.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new SendMailTask().execute(to, emailSubject, emailMessage);
    }

    private class SendMailTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                mailSender.sendEmail(params[0], params[1], params[2]);
                return true;
            } catch (Exception e) {
                this.exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(MainActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                emailTo.setText("");
                subject.setText("");
                message.setText("");
            } else {
                Toast.makeText(MainActivity.this,
                        "Failed to send email: " + exception.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
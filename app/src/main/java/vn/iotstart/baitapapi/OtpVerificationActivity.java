package vn.iotstart.baitapapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;

public class OtpVerificationActivity extends AppCompatActivity {
    private EditText etOtp;
    private Button btnVerify, btnResendOtp;
    private String correctOtp, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        etOtp = findViewById(R.id.et_otp);
        btnVerify = findViewById(R.id.btn_verify_otp);
        btnResendOtp = findViewById(R.id.btn_resend_otp);

        // Retrieve data from previous intent
        email = getIntent().getStringExtra("EMAIL");
        password = getIntent().getStringExtra("PASSWORD");
        correctOtp = getIntent().getStringExtra("OTP");

        // Send OTP via email
        sendEmailOtp(correctOtp);

        btnVerify.setOnClickListener(v -> {
            String userOtp = etOtp.getText().toString().trim();
            verifyOtp(userOtp);
        });

        btnResendOtp.setOnClickListener(v -> {
            String newOtp = generateNewOtp();
            sendEmailOtp(newOtp);
        });
    }

    private void sendEmailOtp(String otpCode) {
        new EmailSenderTask(email, otpCode).execute();
    }

    private void verifyOtp(String userOtp) {
        if (userOtp.equals(correctOtp)) {
            // Successful verification - proceed to next screen
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateNewOtp() {
        String newOtp = String.format("%06d", new Random().nextInt(999999));
        correctOtp = newOtp;
        return newOtp;
    }

    // Inner class for sending emails asynchronously
    private class EmailSenderTask extends AsyncTask<Void, Void, Boolean> {
        private String recipientEmail;
        private String otpCode;

        public EmailSenderTask(String email, String otp) {
            this.recipientEmail = email;
            this.otpCode = otp;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("phuongtrinhdangthuc@gmail.com", "xtly sacy dley gisq");
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("lyhung10nctlop95@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Mã OTP đăng ký");
                message.setText("Mã OTP của bạn là: " + otpCode);

                Transport.send(message);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(OtpVerificationActivity.this,
                        "OTP đã được gửi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OtpVerificationActivity.this,
                        "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
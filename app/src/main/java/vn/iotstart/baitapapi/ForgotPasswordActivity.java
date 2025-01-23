package vn.iotstart.baitapapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmailForgot;
    private Button btnSubmitEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmailForgot = findViewById(R.id.et_email_forgot);
        btnSubmitEmail = findViewById(R.id.btn_submit_email);

        btnSubmitEmail.setOnClickListener(v -> {
            String email = etEmailForgot.getText().toString().trim();

            if (validateEmail(email)) {
                sendOtp(email);
            }
        });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty() || !email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendOtp(String email) {
        // Logic gửi OTP (thực tế bạn sẽ sử dụng server hoặc API để gửi OTP)
        String otpCode = String.format("%06d", new java.util.Random().nextInt(999999));

        Toast.makeText(this, "OTP has been sent to your email: " + otpCode, Toast.LENGTH_LONG).show();

        // Chuyển sang màn hình nhập OTP
        Intent intent = new Intent(this, OtpVerificationActivity.class);
        intent.putExtra("EMAIL", email);
        intent.putExtra("OTP", otpCode);
        startActivity(intent);
        finish();
    }
}
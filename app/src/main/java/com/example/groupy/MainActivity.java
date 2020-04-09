package com.example.groupy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class MainActivity extends AppCompatActivity {
    ProgressBar pb;
    String phone_string;
    FirebaseAuth mAuth;
    String code;
    String verificationID;
    PhoneAuthCredential phoneAuthCredential;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
       pb=findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);




        final Button signin=findViewById(R.id.signin);
       signin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EditText phoneno = findViewById(R.id.phone);
               phone_string = phoneno.getText().toString();
               if (phone_string.isEmpty() || phone_string.length() < 10) {
                   Toast.makeText(MainActivity.this, "Invalid Phone No", Toast.LENGTH_LONG).show();
               } else {
                   signin.setVisibility(View.INVISIBLE);
                   pb.setVisibility(View.VISIBLE);

                   try {
                       PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone_string,        // Phone number to verify
                               60,                 // Timeout duration
                               TimeUnit.SECONDS,   // Unit of timeout
                               MainActivity.this,               // Activity (for callback binding)
                               mCallbacks);        // OnVerificationStateChangedCallbacks
                   } catch (Exception e) {
                       Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                   }

               }


           }

       });
       

                 
               }

       

    


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID=s;


        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationID,code);
                signinwithcredential(credential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            Intent intentp=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intentp);

        }
    };

    private void signinwithcredential(PhoneAuthCredential credential) {


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNew)
                            {
                                Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
                                intent1.putExtra("base","otp");
                                startActivity(intent1);


                            }
                            else {


                                Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
                                startActivity(intent1);
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(MainActivity.this, "VERIFICATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                        {
                            Toast.makeText(MainActivity.this,"ERROR ",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
}
    

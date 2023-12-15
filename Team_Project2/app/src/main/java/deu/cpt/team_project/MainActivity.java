package deu.cpt.team_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth Firebaseauth;
    private DatabaseReference Databaseref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("부산복지관센터정보공유");

        Firebaseauth = FirebaseAuth.getInstance();
        Databaseref = FirebaseDatabase.getInstance().getReference("User");
        EditText edtId = (EditText) findViewById(R.id.edtLogId);
        EditText edtPw = (EditText) findViewById(R.id.edtLogPw);
        Button btnLogIn = (Button) findViewById(R.id.btnLogIn);
        Button btnSingUp = (Button) findViewById(R.id.btnSignUp);

        //로그인 버튼 클릭 시
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 singleLine을 줘서 엔터키 입력시 줄 바꿈이 일어나지 않고 다음으로 넘어가게 함
                String SId = edtId.getText().toString();
                String SPw = edtPw.getText().toString();

                //파이어베이스 이메일, 비밀번호양식 계정 로그인 양식
                Firebaseauth.signInWithEmailAndPassword(SId, SPw).addOnCompleteListener
                        (MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //로그인 성공 시
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(), community.class);
                                    startActivityForResult(intent, 0);
                                }
                                //로그인 실패 시
                                else{
                                    Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //회원가입 버튼 클릭 시
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), sign_up.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
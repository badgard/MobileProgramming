package deu.cpt.team_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {
    private FirebaseAuth Firebaseauth;
    private DatabaseReference Databaseref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        setTitle("회원가입");

        Firebaseauth = FirebaseAuth.getInstance();
        Databaseref = FirebaseDatabase.getInstance().getReference("memo20173057");
        EditText edtSignId = findViewById(R.id.edtSignId);
        EditText edtSIgnPw = findViewById(R.id.edtSignPw);
        Button btnSign_Sign_Up = findViewById(R.id.btnSign_SignUp);

        //회원가입 버튼 클릭 시
        btnSign_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 singleLine을 줘서 엔터키 입력시 줄 바꿈이 일어나지 않고 다음으로 넘어가게 함
                String SId = edtSignId.getText().toString();
                String SPw = edtSIgnPw.getText().toString();
                //파이어베이스 이메일, 비밀번호양식 계정 생성 양식
                Firebaseauth.createUserWithEmailAndPassword(SId, SPw).addOnCompleteListener
                        (sign_up.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //생성 성공 시
                                if(task.isSuccessful()){
                                    //파이어베이스 계정 생성
                                    FirebaseUser user = Firebaseauth.getCurrentUser();
                                    User account = new User();
                                    account.setIdToken(user.getUid());
                                    account.setId(user.getEmail());
                                    account.setPw(SPw);
                                    //데이터 양식의 "memo20173057/User/Uid/"에 account 저장
                                    Databaseref.child("User").child(user.getUid()).setValue(account);
                                    Toast.makeText(sign_up.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivityForResult(intent, 0);
                                }
                                //생성 실패 시
                                else{
                                    if(SPw.length() < 6){
                                        Toast.makeText(sign_up.this, "비밀번호를 6자리 이상 입력해야 합니다", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(sign_up.this, "이메일 형식을 입력해야 합니다", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(sign_up.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

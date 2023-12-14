package deu.cpt.team_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class new_post extends AppCompatActivity {
    private FirebaseAuth Firebaseauth;
    private DatabaseReference Databaseref;
    private FirebaseDatabase db;
    private FirebaseUser user;
    long mNow;
    Date mDate;
    //SimpleDateFormat 클래스로 날짜를 문자열 포맷으로저장
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    View dialogview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);
        setTitle("게시글 작성");

        Firebaseauth = FirebaseAuth.getInstance();
        Databaseref = FirebaseDatabase.getInstance().getReference("memo20173057/Post/");
        user = Firebaseauth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnClose = (Button) findViewById(R.id.btnClose);
        EditText edtNewTitle = (EditText) findViewById(R.id.edtNewTitle);
        EditText edtNewDetail = (EditText) findViewById(R.id.edtNewDetail);

        //메모 기록 버튼 클릭 시
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //제목란이 비워졌을 경우
                if(edtNewTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(new_post.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                //아닐 경우
                else{
                    mNow = System.currentTimeMillis();
                    mDate = new Date(mNow);
                    Post post = new Post();

                    post.setTitle(edtNewTitle.getText().toString());
                    post.setDetail(edtNewDetail.getText().toString());
                    post.setWriter(user.getEmail());
                    post.setDate(getTime());
                    //파이어베이스 저장소에 memo 데이터 푸시
                    Databaseref = db.getReference("memo20173057").child("Post").push();
                    //파이어베이스 저장소에 memo 저장
                    Databaseref.setValue(post);
                    Intent intent = new Intent(new_post.this, community.class);
                    startActivityForResult(intent, 0);
                    finish();
                }
            }
        });
        //취소 버튼 클릭 시
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogview = (View) View.inflate(new_post.this, R.layout.dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(new_post.this);
                dlg.setMessage("게시글 작성을 취소하시겠습니까?");
                dlg.setView(dialogview);
                dlg.setNegativeButton("취소", null);
                //확인 클릭 메소드
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dlg.show();
            }
        });
    }
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return mFormat.format(mDate);
    }
}

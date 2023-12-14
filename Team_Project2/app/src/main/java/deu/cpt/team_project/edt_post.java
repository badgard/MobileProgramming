package deu.cpt.team_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class edt_post extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference Databaseref;
    View dialogview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edt_post);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        String getDate = intent.getStringExtra("Date");
        String getTitle = intent.getStringExtra("Title");
        String getDetail = intent.getStringExtra("Detail");
        String getWriter = intent.getStringExtra("Writer");
        String getKey = intent.getStringExtra("Key");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        Databaseref = db.getReference("memo20173057/Post/" + getKey);
        EditText edtEditTitle = (EditText) findViewById(R.id.edtEditTitle);
        EditText edtEditDetail = (EditText) findViewById(R.id.edtEditDetail);
        Button btnMod = (Button) findViewById(R.id.btnMod);
        Button btnClose = (Button) findViewById(R.id.btnClose);

        edtEditTitle.setText(getTitle);
        edtEditDetail.setText(getDetail);
        //메모 수정 버튼 클릭 시
        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogview = (View) View.inflate(edt_post.this, R.layout.dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(edt_post.this);
                String Message = "수정을 완료하시겠습니까?";
                dlg.setMessage(Message);
                dlg.setView(dialogview);
                dlg.setNegativeButton("취소",  null);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Post post = new Post();

                        post.setTitle(edtEditTitle.getText().toString());
                        post.setDetail(edtEditDetail.getText().toString());
                        post.setDate(getDate);
                        Databaseref.setValue(post);

                        Intent intent = new Intent(edt_post.this, community.class);
                        startActivityForResult(intent, 0);
                        finish();
                    }
                });
                dlg.show();
            }
        });
        //취소 버튼 클릭 시
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogview = (View) View.inflate(edt_post.this, R.layout.dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(edt_post.this);
                dlg.setMessage("수정을 취소하시겠습니까?");
                dlg.setView(dialogview);
                dlg.setNegativeButton("취소", null);
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
}

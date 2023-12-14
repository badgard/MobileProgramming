package deu.cpt.team_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class comment_post extends AppCompatActivity {
    private FirebaseAuth Firebaseauth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference Databaseref;
    private ListView listview;
    private CommetAdapter commentAdapter;
    private ArrayList<Comment> arrayList = new ArrayList<>();
    View dialogview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_post);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        String getToken = intent.getStringExtra("Token");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        Databaseref = db.getReference("memo20173057/Post/" + getToken + "/postToken");

        EditText edtWrite = (EditText) findViewById(R.id.edtWrite);
        Button btnCommentBack = (Button) findViewById(R.id.btnCommentBack);
        Button btnWrite = (Button) findViewById(R.id.btnWrite);
        listview = (ListView) findViewById(R.id.lvComment);

        commentAdapter = new CommetAdapter(arrayList, getApplicationContext());
        //엔터키를 누를 시 작성 버튼 클릭
        edtWrite.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == keyEvent.KEYCODE_ENTER){
                    btnWrite.callOnClick();
                }
                return false;
            }
        });


        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post();
                Comment comment = new Comment();
                post.setPostToken(getToken);
                comment.setWriter(user.getEmail());
                comment.setComment(edtWrite.getText().toString());
                Databaseref = db.getReference("memo20173057/Post/" + getToken + "/postToken").push();
                //파이어베이스 저장소에 memo 데이터 푸시
                Databaseref.setValue(comment);
            }
        });

        btnCommentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //데이터 변경 사항 수신
        Databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment comment = new Comment();
                    comment.setWriter(dataSnapshot.child("writer").getValue(String.class));
                    comment.setComment(dataSnapshot.child("comment").getValue(String.class));
                    arrayList.add(comment);
                }
                listview.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

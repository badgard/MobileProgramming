package deu.cpt.team_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class query_post extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference Databaseref;
    private ArrayList<Post> arrayList = new ArrayList<>();
    View dialogview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_post);

        Intent intent = getIntent();
        String getDate = intent.getStringExtra("date");
        String getTitle = intent.getStringExtra("title");
        String getDetail = intent.getStringExtra("detail");
        String getWriter = intent.getStringExtra("writer");
        String getToken = intent.getStringExtra("token");
        setTitle(getTitle);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();

        Button btnQueryBack = (Button) findViewById(R.id.btnQueryBack);
        TextView tvDetail2 = (TextView) findViewById(R.id.tvDetail2);
        //TextView에 스크롤 움직임 허용
        tvDetail2.setMovementMethod(new ScrollingMovementMethod());
        LinearLayout llComment = (LinearLayout) findViewById(R.id.llComment);

        tvDetail2.setText(getDetail);

        llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), comment_post.class);
                intent.putExtra("Token", getToken);
                startActivityForResult(intent, 0);
            }
        });

        btnQueryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = getIntent();
        String getDate = intent.getStringExtra("date");
        String getTitle = intent.getStringExtra("title");
        String getDetail = intent.getStringExtra("detail");
        String getWriter = intent.getStringExtra("writer");
        String getToken = intent.getStringExtra("token");
        //새 메모 작성 메뉴 선택 시
        if(item.getItemId() == R.id.itemMod){
            Intent intent2 = new Intent(query_post.this, edt_post.class);
            intent2.putExtra("Date", getDate);
            intent2.putExtra("Title", getTitle);
            intent2.putExtra("Detail", getDetail);
            intent2.putExtra("Writer", getWriter);
            intent2.putExtra("Token", getToken);
            startActivityForResult(intent2, 0);
        }
        //개발자 정보 메뉴 선택 시
        else if(item.getItemId() == R.id.itemDelete){
            dialogview = (View) View.inflate(query_post.this, R.layout.dialog, null);
            AlertDialog.Builder dlg = new AlertDialog.Builder(query_post.this);
            String Message = "이 게시글을 삭제하시겠습니까?";
            dlg.setMessage(Message);
            dlg.setView(dialogview);
            dlg.setNegativeButton("취소", null);
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int in) {
                    Post post = new Post();
                    Databaseref = db.getReference("memo20173057/Post/" + getToken);
                    Databaseref.removeValue();

                    Intent intent = new Intent(query_post.this, community.class);
                    startActivityForResult(intent, 0);
                    finish();
                }
            });
            dlg.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

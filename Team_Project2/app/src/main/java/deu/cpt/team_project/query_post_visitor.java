package deu.cpt.team_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class query_post_visitor extends AppCompatActivity {
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
}

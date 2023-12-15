package deu.cpt.team_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class community extends AppCompatActivity {
    private FirebaseAuth Firebaseauth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference Databaseref;
    private ListView listview;
    private PostAdapter memoAdapter;
    private ArrayList<Post> arrayList = new ArrayList<>();
    View dialogview;

    //메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //메뉴 선택
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //게시글 작성 선택 시
        if(item.getItemId() == R.id.itemNew){
            //new_post로 이동
            Intent intent = new Intent(getApplicationContext(), new_post.class);
            startActivityForResult(intent, 0);
        }
        //로그아웃 선택 시
        else if(item.getItemId() == R.id.itemLogOut){
            dialogview = (View) View.inflate(community.this, R.layout.dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(community.this);
                dlg.setMessage("정말 로그아웃 하시겠습니까?");
                dlg.setView(dialogview);
                dlg.setNegativeButton("취소", null);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Firebaseauth.signOut();
                        //메인으로 이동
                        Intent intent = new Intent(community.this, MainActivity.class);
                        startActivityForResult(intent, 0);
                        finish();
                    }
                });
                dlg.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        Databaseref = db.getReference("memo20173057/Post/");
        setTitle(user.getEmail());

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        EditText edtSearch = (EditText) findViewById(R.id.edtSearch);
        listview = (ListView) findViewById(R.id.lv);

        memoAdapter = new PostAdapter(arrayList, getApplicationContext());
        //엔터키를 누를 시 검색 버튼 클릭
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == keyEvent.KEYCODE_ENTER){
                    btnSearch.callOnClick();
                }
                return false;
            }
        });
        //데이터 변경 사항 수신
        Databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = new Post();
                    post.setTitle(dataSnapshot.child("title").getValue(String.class));
                    post.setDate(dataSnapshot.child("date").getValue(String.class));
                    arrayList.add(post);
                    post.setDetail(dataSnapshot.child("detail").getValue(String.class));
                    post.setWriter(dataSnapshot.child("writer").getValue(String.class));
                    post.setPostToken(dataSnapshot.getKey());
                }
                listview.setAdapter(memoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //검색 버튼 클릭 시
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtSearch.getText().toString();

                //검색 단어가 없을 경우
                if(text.length() == 0){
                    Toast.makeText(getApplicationContext(), "검색단어가 없습니다.", Toast.LENGTH_SHORT).show();

                    //리스트뷰 초기화
                    Databaseref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();

                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Post post = new Post();
                                post.setTitle(dataSnapshot.child("title").getValue(String.class));
                                post.setDate(dataSnapshot.child("date").getValue(String.class));
                                arrayList.add(post);
                                post.setDetail(dataSnapshot.child("detail").getValue(String.class));
                                post.setWriter(dataSnapshot.child("writer").getValue(String.class));
                                post.setPost_Comment(dataSnapshot.child("comment").getValue(Comment.class));
                                post.setPostToken(dataSnapshot.getKey());
                            }
                            listview.setAdapter(memoAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                else{
                    arrayList.clear();
                    //제목과 내용을 합친 combined를 생성하고 combined를 대상으로 검색하는 쿼리문
                    Query query = Databaseref.orderByChild("combined");

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                //combined값 가져오기
                                String combined = dataSnapshot.child("combined").getValue(String.class);
                                //combined에 EditText에 입력된 text값이 포함될 경우
                                if(combined != null && combined.toLowerCase().contains(text.toLowerCase())){
                                    Post post = new Post();
                                    post.setTitle(dataSnapshot.child("title").getValue(String.class));
                                    post.setDate(dataSnapshot.child("date").getValue(String.class));
                                    arrayList.add(post);
                                    post.setDetail(dataSnapshot.child("detail").getValue(String.class));
                                    post.setWriter(dataSnapshot.child("writer").getValue(String.class));
                                    post.setPost_Comment(dataSnapshot.child("comment").getValue(Comment.class));
                                    post.setPostToken(dataSnapshot.getKey());
                                }
                            }
                            listview.setAdapter(memoAdapter);
                            //검색 결과와 일치하는 데이터가 없을 경우
                            if(arrayList.isEmpty()){
                                Toast.makeText(getApplicationContext(), "'" + text + "'에 대한 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                memoAdapter.notifyDataSetChanged();
            }
        });

        //게시글 클릭 시
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String date = arrayList.get(i).getDate();
                String title = arrayList.get(i).getTitle();
                String detail = arrayList.get(i).getDetail();
                String writer = arrayList.get(i).getWriter();
                String token = arrayList.get(i).getPostToken();

                if(user.getEmail().equals(writer) || user.getEmail().equals("aa@gmail.com")){
                    Intent intent = new Intent(getApplicationContext(), query_post.class);
                    intent.putExtra("date", date);
                    intent.putExtra("title", title);
                    intent.putExtra("detail", detail);
                    intent.putExtra("writer", writer);
                    intent.putExtra("token", token);
                    startActivityForResult(intent, 0);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), query_post_visitor.class);
                    intent.putExtra("date", date);
                    intent.putExtra("title", title);
                    intent.putExtra("detail", detail);
                    intent.putExtra("writer", writer);
                    intent.putExtra("token", token);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }
}

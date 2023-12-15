package deu.cpt.team_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PostAdapter extends BaseAdapter {
    ArrayList<Post> arrayList = new ArrayList<>();
    Context context;
    public PostAdapter(ArrayList<Post> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View itemview = inflater.inflate(R.layout.item_list, viewGroup, false);

        TextView tvTitle = (TextView) itemview.findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) itemview.findViewById(R.id.tvDate);

        //리스트뷰의 각 포지션 값을 arrayList에 추가
        tvTitle.setText(arrayList.get(position).getTitle());
        tvDate.setText(arrayList.get(position).getDate());
        return itemview;
    }
}

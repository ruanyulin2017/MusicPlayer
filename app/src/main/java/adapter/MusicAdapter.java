package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruanyulin.musicplayer.R;

import java.text.SimpleDateFormat;
import java.util.List;

import music.MusicResource;

/**
 * Created by ruanyulin on 17-8-22.
 */

public class MusicAdapter extends BaseAdapter {

    private int flag;

    private List<MusicResource> olist = null;
    private Context context;
    private LayoutInflater layoutInflater;

    public MusicAdapter(List<MusicResource> olist,Context context) {
        this.olist = olist;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return olist.size();
    }

    @Override
    public Object getItem(int i) {
        return olist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoulder viewHoulder = null;
        if (view == null) {
            viewHoulder = new ViewHoulder();
            view = layoutInflater.inflate(R.layout.music_item,null);
            viewHoulder.name = (TextView) view.findViewById(R.id.musicname);
            viewHoulder.author = (TextView) view.findViewById(R.id.musicauthor);
            viewHoulder.time = (TextView) view.findViewById(R.id.musictime);
            viewHoulder.playing = (ImageView) view.findViewById(R.id.playing);
            view.setTag(viewHoulder);
        } else {
            viewHoulder = (ViewHoulder) view.getTag();
        }
        viewHoulder.name.setText(olist.get(i).getName());
        viewHoulder.author.setText(olist.get(i).getAuthor());
        viewHoulder.time.setText(getTimes(olist.get(i).getTime()));
        if (i == flag) {
            viewHoulder.playing.setVisibility(View.VISIBLE);
        } else {
            viewHoulder.playing.setVisibility(View.GONE);
        }

        return view;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
    private String getTimes(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String times = simpleDateFormat.format(time);
        return times;
    }
    class ViewHoulder{
        //public ImageView img;
        private ImageView playing;
        public TextView name;
        public TextView author;
        public TextView time;
    }
}

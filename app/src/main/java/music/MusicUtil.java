package music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruanyulin on 17-8-22.
 */

public class MusicUtil {
    public static List<MusicResource> getmusic(Context context){
        List<MusicResource> olist = new ArrayList<MusicResource>();
        ContentResolver resolver = context.getContentResolver();
        //查询音乐文件
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while (cursor.moveToNext()) {
            MusicResource resource = new MusicResource();
            //得到歌曲名
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            //演唱者
            String author = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
/*
            if (resource == null) {
                Toast.makeText(context,"resourcenull",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"notnull",Toast.LENGTH_SHORT).show();
            }
*/
            if (time >60000) {
                resource.setName(name);
                resource.setAuthor(author);
                resource.setPath(path);
                resource.setTime(time);
                olist.add(resource);
            }


        }
        return olist;
    }

}

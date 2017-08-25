package music;

import java.io.Serializable;

/**
 * Created by ruanyulin on 17-8-22.
 */

public class MusicResource implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name;
    private String author;
    private String path;
    private long time;

    public static long getSerialVersionUID(){
        return serialVersionUID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }




}

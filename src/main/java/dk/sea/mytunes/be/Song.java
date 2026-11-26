package dk.sea.mytunes.be;

public class Song {

    private int songId;
    private String title;
    private String artist;
    private String category;
    private int duration;
    private String filePath;

    public Song(int songId, String title, String artist, String category, int duration, String filePath) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.duration = duration;
        this.filePath = filePath;
    }

    public Song(String title, String artist, String category, int duration, String filePath) {
        this(-1,title,artist,category, duration,filePath);
    }

    public int getId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String formatDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return title+" : "+artist;
    }
}



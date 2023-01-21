package vibe;

import connectingBase.Connecting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Mp3 {
    String title;
    String Genre;
    String rating;
    String comments;
    String album;
    String artist;
    String length;
    String size;

    //setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String Genre) {
        this.Genre = Genre;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setSize(String size) {
        this.size = size;
    }

    //getters
    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return Genre;
    }

    public String getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getLength() {
        return length;
    }

    public String getSize() {
        return size;
    }

    public Mp3(String title, String Genre, String rating, String comments, String album, String artist, String length, String size) {
        setTitle(title);
        setGenre(Genre);
        setRating(rating);
        setComments(comments);
        setAlbum(album);
        setArtist(artist);
        setLength(length);
        setSize(size);
    }

    public void save(Connection con) throws Exception{
        con = Connecting.getConnect();
        PreparedStatement st = null;
        con.setAutoCommit(false);
        String sql = "INSERT INTO MP3INFO VALUES(nextval('seq'),?,?,?,?,?,?,?,?)";
        try {
            st = con.prepareStatement(sql);
            st.setString(1,this.getTitle());
            st.setString(2,this.getGenre());
            st.setString(3,this.getRating());
            st.setString(4,this.getComments());
            st.setString(5,this.getAlbum());
            st.setString(6,this.getArtist());
            st.setString(7,this.getLength());
            st.setString(8,this.getSize());

            st.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            Connecting.returnRollback(con);
            e.printStackTrace();
        } finally {
            Connecting.freeStatement(st);
            Connecting.freeConnection(con);
        }
        System.out.println(sql);
    }
}
















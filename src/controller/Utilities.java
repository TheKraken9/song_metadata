package controller;

import vibe.Mp3;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import connectingBase.Connecting;
import org.apache.tika.sax.BodyContentHandler;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Arrays;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.LyricsHandler;
import org.apache.tika.parser.mp3.Mp3Parser;

public class Utilities {
    public void reset(String path) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = Connecting.getConnect();
            if(conn != null) {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                File directory = new File(path);
                File[] listOfFiles = directory.listFiles();
                if(listOfFiles != null) {
                    Arrays.sort(listOfFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                    for (File file : listOfFiles) {
                        try {
                            Mp3 mp3 = this.retrieveMetadata(file);
                            mp3.save(conn);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    insertTime(getLastFile(listOfFiles));
                    conn.commit();
                }
            }
        } catch (Exception e) {
            Connecting.returnRollback(conn);
            e.printStackTrace();
        } finally {
            Connecting.freeStatement(stmt);
            Connecting.freeConnection(conn);
        }
    }

    public void recentReset(String path) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = Connecting.getConnect();
            if(conn != null) {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                File directory = new File(path);
                File[] listOfFiles = directory.listFiles();
                if(listOfFiles != null) {
                    Arrays.sort(listOfFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                    Timestamp lastTime = getLastTime();
                    for (File file : listOfFiles) {
                        try {
                            Mp3 mp3 = this.retrieveMetadata(file);
                            Timestamp time = new Timestamp(file.lastModified());
                            if(time.compareTo(lastTime) <= 0) break;
                            mp3.save(conn);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    updateTime(getLastFile(listOfFiles));
                    conn.commit();
                }
            }
        } catch (Exception e) {
            Connecting.returnRollback(conn);
            e.printStackTrace();
        } finally {
            Connecting.freeStatement(stmt);
            Connecting.freeConnection(conn);
        }
    }

    public static void insertTime(Timestamp time) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = Connecting.getConnect();
            if(conn != null) {
                conn.setAutoCommit(false);
                String sql = "INSERT INTO Modified VALUES (1, '" + time + "')";
                stmt = conn.createStatement();
                System.out.println(sql);
                stmt.executeUpdate(sql);
                conn.commit();
            }
        } catch (Exception e) {
            Connecting.returnRollback(conn);
            e.printStackTrace();
        } finally {
            Connecting.freeStatement(stmt);
            Connecting.freeConnection(conn);
        }
    }

    public static void updateTime(Timestamp time) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = Connecting.getConnect();
            if(conn != null) {
                conn.setAutoCommit(false);
                String sql = "UPDATE modified SET DATEOFMODIFIED ='" + time + "' WHERE idmodified = 1";
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                conn.commit();
            }
        } catch(Exception e) {
            Connecting.returnRollback(conn);
            e.printStackTrace();
        } finally {
            Connecting.freeStatement(stmt);
            Connecting.freeConnection(conn);
        }
    }

    public Mp3 retrieveMetadata(File path) throws Exception {
        if (!path.isFile() || !path.getName().endsWith(".mp3")) {
            throw new Exception(path.getName() + " n'est pas un fichier mp3");
        }
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(path);
        ParseContext pcontext = new ParseContext();

        Mp3Parser mp3Parser = new Mp3Parser();
        mp3Parser.parse(inputstream, handler, metadata, pcontext);
        LyricsHandler lyrics = new LyricsHandler(inputstream, handler);

        Mp3 mp3 = new Mp3(metadata.get("dc:title"), metadata.get("xmpDM:genre"),
                metadata.get("xmpDM:audioSampleRate"), metadata.get("xmpDM:logComment"),
                metadata.get("xmpDM:album"), metadata.get("xmpDM:artist"), metadata.get("xmpDM:duration"),
                metadata.get("xmpDM:fileDataRate"));
        //System.out.println(metadata.get(Metadata.CREATION_DATE));

        return mp3;
    }

    public static Timestamp getLastTime() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        Timestamp time = null;
        try {
            conn = Connecting.getConnect();
            if(conn != null) {
                conn.setAutoCommit(false);
                String sql = "SELECT * FROM modified";
                stmt = conn.createStatement();
                result = stmt.executeQuery(sql);
                result.next();
                time = result.getTimestamp(2);
            }
        } catch (Exception e) {
            Connecting.returnRollback(conn);
            e.printStackTrace();
        } finally {
            Connecting.freeStatement(stmt);
            Connecting.freeResultSet(result);
            Connecting.freeConnection(conn);
        }
        return time;
    }

    public static Timestamp getLastFile(File[] files) {
        for (File file : files) {
            Timestamp time = new Timestamp(file.lastModified());
            if (file.isFile() && file.getName().contains(".mp3"))
                return time;
        }
        return null;
    }
}

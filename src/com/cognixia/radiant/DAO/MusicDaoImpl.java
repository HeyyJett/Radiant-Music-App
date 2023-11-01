package com.cognixia.radiant.DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.radiant.connection.ConnectionManager;
import com.cognixia.radiant.exceptions.PreventCompleteFromUnComplete;

public class MusicDaoImpl implements MusicDao{
    private Connection connection = null;

    @Override
    public void establishConnection() throws ClassNotFoundException, SQLException {

        if(connection == null) {
            try {
                connection = ConnectionManager.getConnection();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeConnection() throws SQLException {
        connection.close();
    }

    public List<Music> getAllMusic() {

        List<Music> musicList = new ArrayList<>();

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM music")){

            while(rs.next()) {
                int id = rs.getInt("music_id");
                String title = rs.getString("title");
                String artist_name = rs.getString("artist_name");
                int length_sec = rs.getInt("length_sec");

                Music music = new Music(id, title, artist_name, length_sec);
                musicList.add(music);

            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return musicList;
    }

    @Override
    public boolean addMusicById(int music_id, int user_id) {

        try(PreparedStatement pstmt = connection.prepareStatement("INSERT INTO user_music(user_id, music_id, status) VALUES (?, ?, 'INCOMPLETE')")) {
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, music_id);
            int count=pstmt.executeUpdate();

            if(count>0) {
                return true;
            }

        }
        catch(SQLException e){
            System.out.println("Wrong ID Entered");
        }
        return false;
    }

    @Override
    public List<Music> getMusicByStatus(String status, int user_id) {

        List<Music> musicList = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM music WHERE music_id IN (SELECT music_id FROM user_music WHERE status = ? AND user_id = ?)")) {
            pstmt.setString(1, status);
            pstmt.setInt(2, user_id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("music_id");
                String title = rs.getString("title");
                String artist_name = rs.getString("artist_name");
                int length_sec = rs.getInt("length_sec");

                Music music = new Music(id, title, artist_name, length_sec);
                musicList.add(music);

            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return musicList;
    }

    @Override
    public boolean addMusicToStatus(String status, int user_id, int music_id){

        // Get current Status
        String currentStatus = getCurrentMusicStatus(user_id, music_id, status);

        // Check if current status didn't return an error
        if(currentStatus.equals("ERROR"))
            return false;

        try(PreparedStatement pStmt = connection.prepareStatement("update user_music set status = ? where user_id = ? AND music_id = ?")) {

            pStmt.setString(1, status);
            pStmt.setInt(2, user_id);
            pStmt.setInt(3, music_id);

            int StatusChange = pStmt.executeUpdate();

            return StatusChange > 0;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean listenToMusic(int user_id, int music_id, int seconds) {

        // Need to check if status is IN-Progress

        try(PreparedStatement pStmt = connection.prepareStatement("update user_music set music_progress_sec = ? where user_id = ? AND music_id = ?")) {

            // Make calculations

            // We need the length of the song
            int song_length = getLengthOfSong(music_id);

            // We need current progress of the song
            int music_progress = getCurrentProgress(music_id, user_id) + seconds;

            if (music_progress >= song_length){
                music_progress = song_length;
            }

            double percentage = (double) music_progress/song_length * 100;

            pStmt.setInt(1, music_progress);
            pStmt.setInt(2, user_id);
            pStmt.setInt(3, music_id);

            int listenedToSong = pStmt.executeUpdate();

            if(listenedToSong > 0){

                if(percentage == 100){
                    System.out.println("You have finished this song");
                }
                else{
                    String formattedValue = String.format("%.2f%%", percentage);
                    System.out.println("This song is currently at " + formattedValue + ".");
                }
                return true;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();

        }
        return false;
    }

    public String getCurrentMusicStatus(int user_id, int music_id, String status) throws PreventCompleteFromUnComplete{

        String currentStatus = "ERROR";

        try(PreparedStatement pStmt = connection.prepareStatement("SELECT status FROM user_music where user_id = ? AND music_id = ?")){

            pStmt.setInt(1, user_id);
            pStmt.setInt(2, music_id);

            ResultSet rs = pStmt.executeQuery();

            // If Status is not empty ("INCOMPLETE", "IN-PROGRESS", "COMPLETE")
            if(rs.next()) {
                currentStatus = rs.getString(1);
                
                if (currentStatus.equals(status) ||
                        (currentStatus.equals("INCOMPLETE") && status.equals("COMPLETE")) ||
                        (currentStatus.equals("COMPLETE") && status.equals("IN-PROGRESS")) ||
                        (currentStatus.equals("COMPLETE") && status.equals("INCOMPLETE")))
                    throw new PreventCompleteFromUnComplete(currentStatus, status);
                else
                    return currentStatus;
            }
            else{
                // This music has not been added to the list
                System.out.println("The song you have selected has not been added to your list.");
            }

        }catch (SQLException e){
            e.printStackTrace();
            currentStatus = "ERROR";

        } catch (PreventCompleteFromUnComplete e) {
            System.out.println(e.getMessage());
            currentStatus = "ERROR";
        }

        return currentStatus;
    }

    public int getLengthOfSong(int music_id){

        int length = 0;

        try(PreparedStatement pstmt = connection.prepareStatement("SELECT length_sec FROM music WHERE music_id = ?")){

            pstmt.setInt(1, music_id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                length = rs.getInt("length_sec");
            }
        }
        catch(SQLException e){
            System.out.println("Error Getting Length of Song");
        }

        return length;
    }

    public int getCurrentProgress(int music_id, int user_id){

        int progress = 0;

        try(PreparedStatement pstmt = connection.prepareStatement("SELECT music_progress_sec FROM user_music WHERE music_id = ? AND user_id = ?")){

            pstmt.setInt(1, music_id);
            pstmt.setInt(2, user_id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                progress = rs.getInt("music_progress_sec");
            }
        }
        catch(SQLException e){
            System.out.println("Error Getting Current Progress of Song");
        }

        return progress;
    }

}

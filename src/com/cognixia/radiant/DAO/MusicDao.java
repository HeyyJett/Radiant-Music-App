package com.cognixia.radiant.DAO;

import java.sql.SQLException;
import java.util.List;


public interface MusicDao {
    // needed for later so we make sure that the connection manager gets called
    public void establishConnection() throws ClassNotFoundException, SQLException;

    // as well, this method will help with closing the connection
    public void closeConnection() throws SQLException ;

    //Returns the list of all music
    public List<Music> getAllMusic();

    //Adding music by id
    public boolean addMusicById(int id, int user_id);

    //Getting music by status
    public List<Music> getMusicByStatus(String status, int user_id);

    //Adding music by status
    public boolean addMusicToStatus(String status, int user_id, int music_id);

    //Listen to Music
    public boolean listenToMusic(int user_id, int music_id, int seconds);
}

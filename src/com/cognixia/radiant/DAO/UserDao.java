package com.cognixia.radiant.DAO;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    // needed for later so we make sure that the connection manager gets called
    public void establishConnection() throws ClassNotFoundException, SQLException;

    // as well, this method will help with closing the connection
    public void closeConnection() throws SQLException ;

    public Optional<User> getUsernameAndPassword(User user);
}

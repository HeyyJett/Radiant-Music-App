package com.cognixia.radiant.DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.cognixia.radiant.connection.ConnectionManager;
import com.cognixia.radiant.exceptions.InvalidLoginException;

public class UserDaoImpl implements UserDao{
    private static Optional<User> currUser;
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

    public Optional<User> getUsernameAndPassword(User user) throws InvalidLoginException {

        try(PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")){

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                int user_id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                rs.close();

                User userObj = new User(user_id, username, password);
                Optional<User> userFound = Optional.of(userObj);


                currUser = userFound;
                return userFound;
            }


        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        throw new InvalidLoginException();
    }

    // This will help us get the user for user_id in the menu
    public Optional<User> getCurrUser() {
        return currUser;
    }

	@Override
	public boolean createUser(String username, String password) {
		
		try(PreparedStatement pstmt = connection.prepareStatement("INSERT INTO users(username, password) VALUES(?,?)")){
			
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			int rs = pstmt.executeUpdate();
			
			if(rs > 0) return true;
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

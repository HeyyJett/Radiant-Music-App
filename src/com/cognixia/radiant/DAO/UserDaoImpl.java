package com.cognixia.radiant.DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
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
                String permission = rs.getString("permission");

                rs.close();

                User userObj = new User(user_id, username, password, permission);
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

    // Used by Sign-Up
	@Override
	public boolean createUser(String username, String password) {
		
		try(PreparedStatement pstmt = connection.prepareStatement("INSERT INTO users(username, password, permission) VALUES(?,?,'USER')")){
			
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

    // Used by Admin
    @Override
    public boolean createUser(String username, String password, String permission) {

        try(PreparedStatement pstmt = connection.prepareStatement("INSERT INTO users(username, password, permission) VALUES(?,?,?)")){

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, permission);

            int rs = pstmt.executeUpdate();

            if(rs > 0)
                return true;

        }
        catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Username already exists. Please try again.");
            return false;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

	public boolean deleteUser(int user_id) {

        // Need to delete from user_music first
        deleteFromUserMusic(user_id);

        try(PreparedStatement pstmt = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
			
			pstmt.setInt(1, user_id);
			
			int rs = pstmt.executeUpdate();
			
			if(rs > 0) return true;	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

    public int getIdByUsername(String username){

        try(PreparedStatement pstmt = connection.prepareStatement("SELECT user_id FROM users WHERE username = ?")) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getInt("user_id");
            }
            else{
                System.out.println("Invalid Username");
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
	
	public boolean updateUser(String username, String newUsername, String newPassword, String newPermission) {

        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE users SET username = ?, password = ?, permission = ? WHERE username = ?")) {

            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setString(3, newPermission);
            pstmt.setString(4, username);

            int rs = pstmt.executeUpdate();

            if (rs > 0)
                return true;
            else
                System.out.println("Username was not found. Please try again.");

        } catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Username already exists. Please try again.");

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public boolean deleteFromUserMusic(int user_id) {

        try(PreparedStatement pstmt = connection.prepareStatement("DELETE FROM user_music WHERE user_id = ?")) {

            pstmt.setInt(1, user_id);

            int rs = pstmt.executeUpdate();

            if(rs > 0) return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

	
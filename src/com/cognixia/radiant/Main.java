package com.cognixia.radiant;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.cognixia.radiant.DAO.Music;
import com.cognixia.radiant.DAO.MusicDaoImpl;
import com.cognixia.radiant.DAO.User;
import com.cognixia.radiant.DAO.UserDao;
import com.cognixia.radiant.DAO.UserDaoImpl;
import com.cognixia.radiant.exceptions.InvalidLoginException;

public class Main {

    public static UserDaoImpl userDao = new UserDaoImpl();
    public static MusicDaoImpl musicDao = new MusicDaoImpl();

    public static int user_id = -1;
    public static String Username = null;

    public static void main(String[] args) {

        // Establish connection
        try {
            userDao.establishConnection();
            musicDao.establishConnection();
        }
        catch (Exception e) {
            System.out.println("Error: Could not establish a connection");
        }

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while(true) {

            try {
                System.out.println();
                System.out.println("Welcome to The Radiant Music App, please choose an option:");
                System.out.println("--------------------------------------------------------------");
                System.out.println("1: Explore Music Library");
                System.out.println("2: Login");
                System.out.println("3: Admin Login");
                System.out.println("4: Sign Up");
                System.out.println("5: Exit");

                int userOption = sc.nextInt();
                sc.nextLine();

                switch (userOption) {
                    case 1:
                        exploreMusicList(musicDao.getAllMusic(), "N/A");
                        break;
                    case 2:
                        login(sc);
                        break;
                    case 3:
                        adminLogin(sc);
                        break;
                    case 4:
                        signup(sc);
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid Option. Please choose from the following options.");
                }

                if (exit) {
                    sc.close();
                    System.out.println("Have a nice day! ~");
                    System.exit(0);
                }
            }
            catch (InputMismatchException e){
                System.out.println("Invalid Input. Please try again.");
                sc.nextLine();
            }
        }
    }

    static void signup(Scanner sc) {

        System.out.println("Enter a cool username:");
        String username = sc.nextLine();

        System.out.println("Enter a secure password:");
        String password = sc.nextLine();
         
        userDao.createUser(username, password);
         
        System.out.println("Successfully created user " + username + ".");
		
	}

	static void exploreMusicList(List<Music> musicList, String status) {

        if(status.equals("INCOMPLETE"))
            System.out.println("\nIncomplete Music List:");
        else if(status.equals("IN-PROGRESS"))
            System.out.println("\nIn-Progress Music List:");
        else if(status.equals("COMPLETE"))
            System.out.println("\nComplete Music List:");
        else
            System.out.println("\nMusic Library:");

        System.out.println("-----------------------");
        for(Music m : musicList){
            System.out.println(m.toString());
        }
        System.out.println();

    }

    static void login(Scanner sc) {

        System.out.println("Please enter your username:");
        String username = sc.nextLine();

        System.out.println("Please enter your password:");
        String password = sc.nextLine();

        User user=new User(username,password, "USER");

        try {
            userDao.getUsernameAndPassword(user);

            // Check if user is USER
            if(userDao.getCurrUser().get().getPermission().equals("USER")) {

                user_id = userDao.getCurrUser().get().getUser_id();
                Username = userDao.getCurrUser().get().getUsername();
                userMenu(sc);
            }
            else{
                System.out.println("Admin cannot log into user menu.");
            }

        }catch (InvalidLoginException e){
            System.out.println(e.getMessage());
            System.out.println();
        }
    }
    
    //admin
    static void adminLogin(Scanner sc) {

        System.out.println("Please enter your username:");
        String username = sc.nextLine();

        System.out.println("Please enter your password:");
        String password = sc.nextLine();

        User Admin = new User(username,password, "ADMIN");

        try {
            userDao.getUsernameAndPassword(Admin);

            // Check if user is ADMIN
            if(userDao.getCurrUser().get().getPermission().equals("ADMIN")) {

                user_id = userDao.getCurrUser().get().getUser_id();
                Username = userDao.getCurrUser().get().getUsername();
                adminMenu(sc);
            }
            else{
                System.out.println("Invalid admin Login.");
            }

        }catch (InvalidLoginException e){
            System.out.println(e.getMessage());
            System.out.println();
        }
    }
    
    //Admin menu
    static void adminMenu(Scanner sc) {

        boolean userLoggedIn = true;

        while (userLoggedIn) {

            try {
                System.out.println("\nWelcome, " + Username + ". Please choose an option:");
                System.out.println("-----------------------------------------");
                System.out.println("1: Add User");
                System.out.println("2: Update User");
                System.out.println("3: Delete User");
                System.out.println("4: Add song");
                System.out.println("5: Log out");

                int userOption2 = sc.nextInt();
                sc.nextLine();

                switch (userOption2) {
                    case 1:
                        createUSer(sc);
                        break;
                    case 2:
                        updateUser(sc);
                        break;
                    case 3:
                        deleteUser(sc);
                        break;
                    case 4:
                        addMusic(sc);
                        break;
                    case 5:
                        userLoggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid Option. Please choose from the following options.");
                }
            }
            catch (InputMismatchException e){
                System.out.println("Invalid Input. Please try again.");
                sc.nextLine();
            }
        }
    }

    // create user
    static void createUSer(Scanner sc){

        System.out.println("Please enter user's username:");
        String username = sc.nextLine();

        System.out.println("Please enter user's password:");
        String password = sc.nextLine();

        System.out.println("Select permission to assign:");
        System.out.println("1. USER");
        System.out.println("2. ADMIN");

        int permission = sc.nextInt();
        boolean created = false;

        switch (permission) {
            case 1:
                created = userDao.createUser(username, password, "USER");
                break;
            case 2:
                created = userDao.createUser(username, password, "ADMIN");
                break;
            default:
                System.out.println("Invalid Option. Please Try again.");
        }

        if(created)
            System.out.println("Successfully created user " + username + ".");
    }

    //delete user
    static void deleteUser(Scanner sc) {

        System.out.println("Please enter a username to delete:");
        String username = sc.nextLine();

        int user_id_to_delete = userDao.getIdByUsername(username);

        // Prevent from deleting current Admin
        if(user_id_to_delete == user_id){
            System.out.println("Cannot delete " + username + ". Admin is currently logged in.");
        }
        else {
            userDao.deleteUser(user_id_to_delete);
            System.out.println("Successfully deleted user " + username + ".");
        }
	}
    
    // update user
    static void updateUser(Scanner sc) {

        System.out.println("Please enter user's current username:");
        String username = sc.nextLine();

        System.out.println("Please enter user's new username:");
        String newUsername = sc.nextLine();

        System.out.println("Please enter user's new password:");
        String newPassword = sc.nextLine();

        // Prevent Current Admin from changing it's own permissions.
        if(!username.equals(Username)) {
            System.out.println("Select permission to assign:");
            System.out.println("1. USER");
            System.out.println("2. ADMIN");

            int permission = sc.nextInt();
            boolean updated = false;

            switch (permission) {
                case 1:
                    updated = userDao.updateUser(username, newUsername, newPassword, "USER");
                    break;
                case 2:
                    updated = userDao.updateUser(username, newUsername, newPassword, "ADMIN");
                    break;
                default:
                    System.out.println("Invalid Option. Please Try again.");
            }

            if (updated)
                System.out.println("Successfully updated user: " + newUsername + ".");
        }
        else{
            // Current Admin being updated
            boolean updated = userDao.updateUser(username, newUsername, newPassword, "ADMIN");

            if(updated) {
                System.out.println("Successfully updated user: " + newUsername + ".");
                Username = newUsername;
            }
        }
	}

    // Add music to Library
    static void addMusic(Scanner sc) {
        try {
            System.out.println("Please enter title of the song:");
            String title = sc.nextLine();

            System.out.println("Please enter artist name: ");
            String artist_name = sc.nextLine();

            System.out.println("Please enter length of song:");
            int length_sec = sc.nextInt();

            musicDao.addMusicByAdmin(title, artist_name, length_sec);
            System.out.println("Success");

        }catch (InputMismatchException e){
            System.out.println("Invalid Input. Please try again.");
            sc.nextLine();
        }

    }

    static void userMenu(Scanner sc) {

        boolean userLoggedIn = true;

        while (userLoggedIn) {

            try {
                System.out.println("\nWelcome, " + Username + ". Please choose an option:");
                System.out.println("-----------------------------------------");
                System.out.println("1: Display Music Library");
                System.out.println("2: Display Incomplete Songs");
                System.out.println("3: Display In-progress Songs");
                System.out.println("4: Display Completed Songs");
                System.out.println("5: Add a Song to my List");
                System.out.println("6: Change Song Category");
                System.out.println("7: Listen to a Song");
                System.out.println("8: Log Out");

                int userOption2 = sc.nextInt();

                switch (userOption2) {
                    case 1:
                        exploreMusicList(musicDao.getAllMusic(), "N/A");
                        break;
                    case 2:
                        exploreMusicList(musicDao.getMusicByStatus("INCOMPLETE", user_id), "INCOMPLETE");
                        break;
                    case 3:
                        exploreMusicList(musicDao.getMusicByStatus("IN-PROGRESS", user_id), "IN-PROGRESS");
                        break;
                    case 4:
                        exploreMusicList(musicDao.getMusicByStatus("COMPLETE", user_id), "COMPLETE");
                        break;
                    case 5:
                        addSongMenu(sc);
                        break;
                    case 6:
                        addSongByStatusMenu(sc);
                        break;
                    case 7:
                        listenToSong(sc);
                        break;
                    case 8:
                        userLoggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid Option. Please choose from the following options.");
                }
            }
            catch (InputMismatchException e){
                System.out.println("Invalid Input. Please try again.");
                sc.nextLine();
            }
        }
    }

    static void addSongMenu(Scanner sc) {


        try {
            System.out.println("Enter the ID of the song you wish to add:");
            int userOption3 = sc.nextInt();
            sc.nextLine();

            // Could return song name instead of boolean so that we can specify which song the user added.
            boolean added = musicDao.addMusicById(userOption3, user_id);

            if (added) {
                System.out.println("You have added song " + userOption3 + " to your list.");
            }
        }
        catch (InputMismatchException e){
            System.out.println("Invalid Input. Please try again.");
            sc.nextLine();
        }
    }

    static void addSongByStatusMenu(Scanner sc) {

        try {
            System.out.println("Enter the ID of the song you wish to change:");

            int music_id = sc.nextInt();
            sc.nextLine();

            System.out.println("Please enter the category in which you wish to change this song into:");
            System.out.println("-----------------------------------------");
            System.out.println("1: Move to In-Progress");
            System.out.println("2: Move to Complete");

            int userOption4 = sc.nextInt();
            sc.nextLine();
            boolean changed;

            switch (userOption4) {
                case 1:
                    changed = musicDao.addMusicToStatus("IN-PROGRESS", user_id, music_id);

                    if (changed)
                        System.out.println("Successfully Updated the category of song " + music_id + " to In-Progress");

                    break;
                case 2:
                    changed = musicDao.addMusicToStatus("COMPLETE", user_id, music_id);

                    if (changed)
                        System.out.println("Successfully Updated the category of song " + music_id + " to Complete");

                    break;
                default:
                    System.out.println("Sorry please choose option on the list.");
            }
        }
        catch (InputMismatchException e){
            System.out.println("Invalid Input. Please try again.");
            sc.nextLine();
        }
    }

    static void listenToSong(Scanner sc) {

        try {
            System.out.println("Enter the ID of the song you wish to listen to:");

            int music_id = sc.nextInt();
            sc.nextLine();

            System.out.println("How many seconds do you want to listen to this song for?");

            int listeningTime = sc.nextInt();
            sc.nextLine();

            musicDao.listenToMusic(user_id, music_id, listeningTime);
        }
        catch (InputMismatchException e){
            System.out.println("Invalid Input. Please try again.");
            sc.nextLine();
        }
    }


}



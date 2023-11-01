package com.cognixia.radiant;

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

        try {
            userDao.establishConnection();
            musicDao.establishConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while(true) {

            System.out.println("Welcome to The Radiant Music App, please choose an option:");
            System.out.println("--------------------------------------------------------------");
            System.out.println("1: Explore song list");
            System.out.println("2: Login");
            System.out.println("3: Login as Admin");
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
                    System.out.println("Sorry please choose an option listed.");
            }

            if(exit) {
                sc.close();
                System.out.println("Have a nice day! ~");
                System.exit(0);
            }

        }
    }

    static void signup(Scanner sc) {
    	 System.out.println("Please enter your desired username:");
         String username = sc.nextLine();

         System.out.println("Please enter your desired password:");
         String password = sc.nextLine();
         
         userDao.createUser(username, password);
         
         System.out.println("Successfully created user " + username + "!");
		
	}

	static void exploreMusicList(List<Music> musicList, String status) {

        if(status.equals("INCOMPLETE"))
            System.out.println("\nIncomplete Song List:");
        else if(status.equals("IN-PROGRESS"))
            System.out.println("\nIn-Progress Song List:");
        else if(status.equals("COMPLETE"))
            System.out.println("\nComplete Song List:");
        else
            System.out.println("\nSong List:");

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
            System.out.println("\nWelcome, " + Username + ". Please choose an option:" );
            System.out.println("-----------------------------------------");
            System.out.println("1: Delete User");
            System.out.println("2: Add User");
            System.out.println("3: Udpdate User");
            System.out.println("4: Log out");

            int userOption2 = sc.nextInt();
            sc.nextLine();

            switch (userOption2) {
                case 1:
                    deleteUser(sc);
                    break;
                case 2:
                    createUSer(sc);
                    break;
                case 3:
                    updateUser(sc);
                    break;
                case 4:
                    userLoggedIn = false;
                    break;
                default:
                    System.out.println("Sorry please choose option on the list.");
            }

        }
    }

    // create user
    static void createUSer(Scanner sc){

        System.out.println("Please enter user's username:");
        String username = sc.nextLine();

        System.out.println("Please enter user's password:");
        String password = sc.nextLine();

        System.out.println("Please enter user's permission:");
        String permission = sc.nextLine();

        userDao.createUser(username, password, permission);

        System.out.println("Successfully created user " + username + "!");
    }

    //delete user
    static void deleteUser(Scanner sc) {

        System.out.println("Please enter a username to delete:");
        String username = sc.nextLine();

        int user_id_to_delete = userDao.getIdByUsername(username);
        userDao.deleteUser(user_id_to_delete);
        
        System.out.println("Successfully deleted user " + username + "!");
		
	}
    
    // update user
    static void updateUser(Scanner sc) {
   	    System.out.println("Please enter user's current username:");
        String username = sc.nextLine();

        System.out.println("Please enter user's password:");
        String password = sc.nextLine();
        
        System.out.println("Please enter user's new username:");
        String newUsername = sc.nextLine();

        System.out.println("Please enter user's new password:");
        String newPassword = sc.nextLine();

        System.out.println("Please enter user's new permission:");
        String newPermission = sc.nextLine();
        
        userDao.updateUser(username, password, newUsername, newPassword, newPermission);
        
        System.out.println("Successfully updated the user" + newUsername + "!");
		
	}
    
    static void userMenu(Scanner sc) {

        boolean userLoggedIn = true;

        while (userLoggedIn) {
            System.out.println("\nWelcome, " + Username + ". Please choose an option:" );
            System.out.println("-----------------------------------------");
            System.out.println("1: Display all songs");
            System.out.println("2: Display incomplete songs");
            System.out.println("3: Display in progress songs");
            System.out.println("4: Display complete songs");
            System.out.println("5: Add a song to my list");	// Uncompleted list
            System.out.println("6: Change category of a song");
            System.out.println("7: Listen to a song");
            System.out.println("8: Log out");

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
                    System.out.println("Sorry please choose option on the list.");
            }

        }
    }

    static void addSongMenu(Scanner sc) {

        System.out.println("Enter the ID of the song you wish to add:");
        int userOption3 = sc.nextInt();
        sc.nextLine();

        // Could return song name instead of boolean so that we can specify which song the user added.
        boolean added = musicDao.addMusicById(userOption3, user_id);

        if (added){
            System.out.println("You have added song " + userOption3 + " to your list.");
        }
    }

    static void addSongByStatusMenu(Scanner sc) {

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

                if(changed)
                    System.out.println("Successfully Updated the category of song " + music_id + " to In-Progress");

                break;
            case 2:
                changed = musicDao.addMusicToStatus("COMPLETE", user_id, music_id);

                if(changed)
                    System.out.println("Successfully Updated the category of song " + music_id + " to Complete");

                break;
            default:
                System.out.println("Sorry please choose option on the list.");
        }
    }

    static void listenToSong(Scanner sc) {

        System.out.println("Enter the ID of the song you wish to listen to:");

        int music_id = sc.nextInt();
        sc.nextLine();

        System.out.println("How many seconds do you want to listen to this song for?");

        int listeningTime = sc.nextInt();
        sc.nextLine();

        boolean listenedToSong = musicDao.listenToMusic(user_id, music_id, listeningTime);


    }
}



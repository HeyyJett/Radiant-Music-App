package com.cognixia.radiant;

import java.util.List;
import java.util.Scanner;

import com.cognixia.radiant.DAO.Music;
import com.cognixia.radiant.DAO.MusicDaoImpl;
import com.cognixia.radiant.DAO.User;
import com.cognixia.radiant.DAO.UserDaoImpl;

public class Main {

    public static UserDaoImpl userDao = new UserDaoImpl();
    public static MusicDaoImpl musicDao = new MusicDaoImpl();

    public static int user_id = -1;

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

            System.out.println("Welcome to The Radiant Music Network hig, please choose an option!");
            System.out.println("--------------------------------------------------------------");
            System.out.println("1: Explore song list");
            System.out.println("2: Login");
            System.out.println("3: Exit");
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
                    exit = true;
                    break;
                default:
                    System.out.println("Sorry please choose option 1 or 2.");
            }

            if(exit) {
                sc.close();
                System.out.println("Have a nice day! ~");
                System.exit(0);
            }

        }
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

    }

    static void login(Scanner sc) {

        System.out.println("Please enter your username:");
        String username = sc.nextLine();

        System.out.println("Please enter your password:");
        String password = sc.nextLine();

        User user=new User(username,password);
        userDao.getUsernameAndPassword(user);

        // Maybe catch exeption here so we may loop back to menu and try to log back in again

        user_id = userDao.getCurrUser().get().getUser_id();
        userMenu(sc);

//    	if (username.equals("Jason") && password.equals("123")) {
//    		userMenu();
//    	}
//    	else {
//    		System.out.println("Login Error");
//    	}

//    	sc.close();
    }


    static void userMenu(Scanner sc) {

        boolean userLoggedIn = true;

        while (userLoggedIn) {
            System.out.println("\nWelcome please choose an option:" );
            System.out.println("-----------------------------------------");
            System.out.println("1: Display all songs");
            System.out.println("2: Display incomplete songs");
            System.out.println("3: Display in progress songs");
            System.out.println("4: Display complete songs");
            System.out.println("5: Add a song to my list");	// Uncompleted list
            System.out.println("6: Change progress of a song");
            System.out.println("7: Go Back");

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
                    userLoggedIn = false;
                    break;
                default:
                    System.out.println("Sorry please choose option on the list.");
            }

        }
    }


    static void addSongMenu(Scanner sc) {



//	 System.out.println("\nAdding Song:");
//	 System.out.println("------------");
//	 System.out.println("which category would you like to add to?: ");
//	 System.out.println("-----------------------------------------");
// 	 System.out.println("1: Incomplete");
//     System.out.println("2: In Progress");
//     System.out.println("3: Complete");

        System.out.println("Enter the ID of the song you wish to add:");
        int userOption3 = sc.nextInt();
        sc.nextLine();

        // Could return song name instead of boolean so that we can specify which song the user added.
        boolean added = musicDao.addMusicById(userOption3, user_id);

        if (added){
            System.out.println("You have added song " + userOption3 + " to your list.");
        }

// 	switch (userOption3) {
//     case 1:
//     	addToIncompleteMenu(sc);
//     	break;
//     case 2:
//     	addToInProgressMenu(sc);
//     	break;
//     case 3:
//     	addToCompleteMenu(sc);
//     	break;
//     default:
//    	 System.out.println("Sorry please choose option on the list.");
// 	}


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
                    System.out.println("Successfully Updated the category of song " + music_id + "to In-Progress");

                break;
            case 2:
                changed = musicDao.addMusicToStatus("COMPLETE", user_id, music_id);

                if(changed)
                    System.out.println("Successfully Updated the category of song " + music_id + "to Complete");

                break;
            default:
                System.out.println("Sorry please choose option on the list.");
        }
    }

    static void addToIncompleteMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the title of the song: ");
        String addTitle = sc.nextLine();
        System.out.println("Please enter the artist of the song: ");
        String addArtist = sc.nextLine();
        System.out.println("Please enter the length of the song in sec: ");
        int addLength = sc.nextInt();

        System.out.println("\nThe song '" + addTitle + "' was added to 'incomplete'");
        sc.close();
    }

    static void addToInProgressMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the title of the song: ");
        String addTitle = sc.nextLine();
        System.out.println("Please enter the artist of the song: ");
        String addArtist = sc.nextLine();
        System.out.println("Please enter the length of the song in sec: ");
        int addLength = sc.nextInt();

        System.out.println("\nThe song '" + addTitle + "' was added to 'in progress'");
    }

    static void addToCompleteMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the title of the song: ");
        String addTitle = sc.nextLine();
        System.out.println("Please enter the artist of the song: ");
        String addArtist = sc.nextLine();
        System.out.println("Please enter the length of the song in sec: ");
        int addLength = sc.nextInt();

        System.out.println("\nThe song '" + addTitle + "' was added to 'complete'");
    }

}



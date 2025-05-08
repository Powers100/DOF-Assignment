/************************************************************
  * 
  * Programming Assignment: PA4 Degrees of Kevin Bacon
  *
  * File Created On: 4/21/25
  * 
  * FILENAME: DOFPowers.java
  * 
  * Author: Donovan Powers
  * 
  * Description: Program allows the user to play the game "Degrees of Kevin Bacon" based on a list.txt file 
  *              The programs supports:
  *              - Searching for up to 2 degrees of separation between an actor and Kevin Bacon
  *              - Adding new actor/movie/year entries to the file
  *              - Deleting existing entries from the file
  *              The program stores and updates the data using ArrayLists and modifies list.txt as needed.
  * 
  * *********************************************************/

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
  
// Custom exception for when input is not found in the file
class NotInFileException extends Exception {
    public NotInFileException() {
        super("An error occurred: Entered value(s) is/are not in list.txt");
    }
}
  
public class DOFPowers {

    // Global ArrayLists shared by all the methods to store all actor, movie, and year data from list.txt
    static ArrayList<String> actorNames = new ArrayList<>();
    static ArrayList<String> movieNames = new ArrayList<>();
    static ArrayList<Integer> movieYears = new ArrayList<>();

    // Displays the menu options to the user
    public static void getOptions() {
        System.out.println("Select Options (Enter Q to Quit):");
        System.out.println("1. Bacon Degrees of Freedom");
        System.out.println("2. Add Entry");
        System.out.println("3. Delete Entry");
    }
  
    // Adds a new actor-movie-year entry to the ArrayLists, and to the file (if it doesn't already exist)
    public static void addEntry(String firstName, String lastName, String movieName, int movieYear) throws IOException {
        String fullName = firstName + " " + lastName;

        // Check to see if the entry is already in the file
        for (int i = 0; i < actorNames.size(); i++) {
            if (actorNames.get(i).equalsIgnoreCase(fullName) && movieNames.get(i).equalsIgnoreCase(movieName) && movieYears.get(i) == movieYear) {
                System.out.println("This entry already exists and won't be added again.");
                return;
            }
        }
  
        // Update the ArrayLists
        actorNames.add(fullName);
        movieNames.add(movieName);
        movieYears.add(movieYear);
  
        // Append to the file
        FileOutputStream fileOut = new FileOutputStream("list.txt", true);
        PrintWriter printWriter = new PrintWriter(fileOut);
        printWriter.println();
        printWriter.print(fullName + " " + movieName + " " + movieYear);

        printWriter.close();
        fileOut.close();
    }
  
    // Deletes a matching entry from the ArrayLists and rewrites the file without the entered entry to remove
    public static void deleteEntry(String firstName, String lastName, String movieName, int movieYear) throws IOException {
        String fullName = firstName + " " + lastName;

        // look through the ArrayLists to find the actor name, movie name, and movie year. Remove that item from the lists.
        for (int i = 0; i < actorNames.size(); i++) {
            if (actorNames.get(i).equalsIgnoreCase(fullName) && movieNames.get(i).equalsIgnoreCase(movieName) && movieYears.get(i) == movieYear) {
                    actorNames.remove(i);
                    movieNames.remove(i);
                    movieYears.remove(i);
                    break;
            }
        }
      
        // Rewrite the file without the deleted entry
        FileOutputStream fileOut = new FileOutputStream("list.txt");
        PrintWriter printWriter = new PrintWriter(fileOut);
  
        for (int i = 0; i < actorNames.size(); i++) {
            if (i == actorNames.size() - 1) {
                printWriter.print(actorNames.get(i) + " " + movieNames.get(i) + " " + movieYears.get(i));
            }
            else {
                printWriter.println(actorNames.get(i) + " " + movieNames.get(i) + " " + movieYears.get(i));
            }
        }
  
        printWriter.close();
        fileOut.close();
    }
  
    // Finds and returns the degree of separation (1 or 2) between an actor and Kevin Bacon
    public static int DOF(String actor) {
        if (actor.equalsIgnoreCase("kevin bacon")) {
            return 0;
        }

        ArrayList<String> baconMovies = new ArrayList<>();

        // Get all movies Kevin Bacon was in
        for (int i = 0; i < actorNames.size(); i++) {
            if (actorNames.get(i).equalsIgnoreCase("Kevin Bacon")) {
                baconMovies.add(movieNames.get(i));
            }
        }
        
        // Create a temp movie name ArrayList to allow for case-insensitive comparison
        ArrayList<String> tempLowerCaseBaconMovies = new ArrayList<>();
        for (String tempMovieName : baconMovies) {
            tempLowerCaseBaconMovies.add(tempMovieName.toLowerCase());
        }
  
        // Check for direct connection (Degree 1) 
        for (int i = 0; i < actorNames.size(); i++) {
            if (actorNames.get(i).equalsIgnoreCase(actor) && tempLowerCaseBaconMovies.contains(movieNames.get(i).toLowerCase())) {
                System.out.println("Kevin Bacon acted with " + actor + " in the movie " + movieNames.get(i) + " in the year " + movieYears.get(i));
                return 1;
            }
        }
  
        // Store all actors who have acted with Kevin Bacon (Degree 1 actors)
        ArrayList<String> degree1Actors = new ArrayList<>();
        ArrayList<String> degree1Movies = new ArrayList<>();
        ArrayList<Integer> degree1Years = new ArrayList<>();
        ArrayList<String> tempLowerCaseDegree1Actors = new ArrayList<>();
    
        for (int i = 0; i < actorNames.size(); i++) {
            if (tempLowerCaseBaconMovies.contains(movieNames.get(i).toLowerCase()) && !actorNames.get(i).equalsIgnoreCase("Kevin Bacon")) {
                degree1Actors.add(actorNames.get(i));
                degree1Movies.add(movieNames.get(i));
                tempLowerCaseDegree1Actors.add(actorNames.get(i).toLowerCase());
                degree1Years.add(movieYears.get(i));
            }
        }
  
        // Check if the actor has worked with someone from Degree 1 (Degree 2)
        for (int i = 0; i < actorNames.size(); i++) {
            if (actorNames.get(i).equalsIgnoreCase(actor)) {
                String movie = movieNames.get(i);
                int year = movieYears.get(i);
    
                for (int j = 0; j < actorNames.size(); j++) {
                    if (movieNames.get(j).equalsIgnoreCase(movie) && tempLowerCaseDegree1Actors.contains(actorNames.get(j).toLowerCase()) && !actorNames.get(j).equalsIgnoreCase(actor)) {
                        String betweenActor = actorNames.get(j);
                        String betweenMovie = "";
                        int betweenYear = -1;
        
                        for (int k = 0; k < degree1Actors.size(); k++) {
                            if (degree1Actors.get(k).equalsIgnoreCase(betweenActor)) {
                                betweenMovie = degree1Movies.get(k);
                                betweenYear = degree1Years.get(k);
                                break; // Stop after finding first match
                            }
                        }
    
                        System.out.println(betweenActor + " acted with " + actor + " in the movie " + movie + " in the year " + year);
                        System.out.println("Kevin Bacon acted with " + betweenActor + " in the movie " + betweenMovie + " in the year " + betweenYear);
                        return 2;
                    }
                }
            }
        }
  
        return -1; // Actor is not connected to Kevin Bacon within 2 degrees
    }
  
    public static void main(String[] args) throws Exception {
        int currentYear = java.time.Year.now().getValue();
        Scanner scnr = new Scanner(System.in);
        
        // Load data from list.txt into the ArrayLists
        try {
            FileInputStream fileInput = new FileInputStream("list.txt");
            Scanner fileScanner = new Scanner(fileInput);
        
            // Load entries from list.txt into ArrayLists
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
  
                try {
                    String tempFirstName = lineScanner.next();
                    String tempLastName = lineScanner.next();
                    String tempFullName = tempFirstName + " " + tempLastName;
        
                    ArrayList<String> tokens = new ArrayList<>();
                    while (lineScanner.hasNext()) {
                        tokens.add(lineScanner.next());
                    }
  
                    // The last token is the year
                    int tempMovieYear = Integer.parseInt(tokens.get(tokens.size() - 1));
        
                    // Movie name is the rest of the tokens
                    String tempMovieName = String.join(" ", tokens.subList(0, tokens.size() - 1));
                    
                    actorNames.add(tempFullName);
                    movieNames.add(tempMovieName);
                    movieYears.add(tempMovieYear);
                }
                catch (Exception e) {
                    System.out.println("Invalid line in file: " + line);
                }
  
                lineScanner.close();
            }
    
            if (actorNames.isEmpty()) {
            System.out.println("Warning: No valid entries found in list.txt.\n");
            }
    
            fileScanner.close();
            fileInput.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("list.txt not found. A new file will be created.");
        }
  
        // Loop to display options while user does not enter "q" or "quit"
        String userInput = "";
        while (true) {
            getOptions();
            userInput = scnr.nextLine().trim();

            if (userInput.equalsIgnoreCase("q") || userInput.equalsIgnoreCase("quit")) {
                break;
            }
    
            try {
                switch (userInput) {
                    // 1 means check for Degree of Kevin Bacon
                    case "1":
                        System.out.println("Enter Actor:");
                        String actorName = scnr.nextLine().trim();

                        // Check to see if entry is in the file
                        boolean found = false;
                        for (int i = 0; i < actorNames.size(); i++) {
                            if (actorNames.get(i).equalsIgnoreCase(actorName)) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            throw new NotInFileException();
                        }

                        int degree = DOF(actorName);
                        if (degree == -1) {
                            System.out.println(actorName + " is not connected to Kevin Bacon within 2 degrees.\n");
                        }
                        else if (degree == 0) {
                            System.out.println("Degree 0: Kevin Bacon is himself.\n");
                        }
                        else {
                            System.out.println("\n" + actorName + " has Bacon Number = " + degree + "\n");
                        }
                        break;
                        
                    // 2 means add an entry
                    case "2":
                        System.out.println("Enter Actor (First and Last Names with a space in between):");
                        try {
                            String fullName = scnr.nextLine().trim();
                            if (fullName.matches("\\d+") || fullName == null || fullName.equals("")) {
                                throw new InputMismatchException();
                            }
                        
                            String firstName = fullName.substring(0, fullName.indexOf(" "));
                            String lastName = fullName.substring(fullName.indexOf(" ") + 1);
            
                            System.out.println("Enter Movie:");
                            String movieName = scnr.nextLine().trim();
                            if (movieName.equals("")) {
                                throw new InputMismatchException();
                            }
            
                            System.out.println("Enter Year:");
                            String tempYear = scnr.nextLine().trim();
                            if (tempYear.equals("") || !tempYear.matches("\\d+")) {
                                throw new InputMismatchException();
                            }
                            int movieYear = Integer.parseInt(tempYear);
                            if (movieYear < 1888 || movieYear > currentYear) {
                                throw new IllegalArgumentException("An error occurred: Movie year must be between 1888 and " + currentYear);
                            }
            
                            addEntry(firstName, lastName, movieName, movieYear);
                            System.out.println();
                        }
                        catch (InputMismatchException e) {
                            System.out.println("An error occurred: Invalid input.\n");
                        }
                        break;
                    
                    //3 means delete an entry
                    case "3":
                        try {
                            System.out.println("Enter Actor (First and Last Names with a space in between):");
                            String delFull = scnr.nextLine().trim();
                            String delFirst = delFull.substring(0, delFull.indexOf(" "));
                            String delLast = delFull.substring(delFull.indexOf(" ") + 1);
                            
                            System.out.println("Enter Movie:");
                            String delMovie = scnr.nextLine().trim();
                            if (delMovie.equals("")) {
                                throw new InputMismatchException();
                            }
            
                            System.out.println("Enter Year:");
                            String tempDelYear = scnr.nextLine().trim();
                            if (tempDelYear.equals("") || !tempDelYear.matches("\\d+")) {
                                throw new InputMismatchException();
                            }
                            int delYear = Integer.parseInt(tempDelYear);
                            if (delYear < 1888 || delYear > currentYear) {
                                throw new IllegalArgumentException("An error occurred: Movie year must be between 1888 and " + currentYear);
                            }
            
                            // Check to see if entry to delete is in the file
                            found = false;
                            for (int i = 0; i < actorNames.size(); i++) {
                                if (actorNames.get(i).equalsIgnoreCase(delFull) && movieNames.get(i).equalsIgnoreCase(delMovie) && movieYears.get(i) == delYear) {
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                throw new NotInFileException();
                            }
            
                            deleteEntry(delFirst, delLast, delMovie, delYear);
                            System.out.println();
                        }
                        catch (NumberFormatException e) {
                            System.out.println("An error occurred: Invalid input. Must be an integer.\n");
                        }
                        catch (InputMismatchException e) {
                            System.out.println("An error occurred: Invalid input.\n");
                        }
                        break;
        
                    default:
                        System.out.println("Invaild option. Please enter 1, 2, 3, or Q to quit\n");
                }
            }
            catch (NotInFileException e) {
                System.out.println(e.getMessage() + "\n");
            }   
            catch (StringIndexOutOfBoundsException e) {
                System.out.println("An error occurred: First name, last name, or space is missing.\n");
            }
            catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage() + "\n");
                scnr.nextLine();
            }
        }
        
        scnr.close();
    }
}
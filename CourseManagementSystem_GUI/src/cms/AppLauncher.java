package cms;

import java.util.Scanner;
import javax.swing.SwingUtilities;

import cms.gui.MainGUI;   // ADD THIS IMPORT

/**
 * AppLauncher
 * Single entry point for the application.
 * Allows user to choose between:
 * 1. Text-Based Interface (CLI)
 * 2. Graphical User Interface (GUI)
 *
 * Assignment 4 requirement:
 * "Allow users to choose their preferred mode of interaction – GUI or text based"
 */
public class AppLauncher {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println(" Course Management System");
        System.out.println(" Choose Interaction Mode");
        System.out.println("======================================");
        System.out.println("1. Text-Based Interface (CLI)");
        System.out.println("2. Graphical User Interface (GUI)");
        System.out.print("Enter choice (1 or 2): ");

        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1":
                // Launch existing Assignment 3 CLI
                System.out.println("\nLaunching Text-Based Interface...\n");
                Main.main(new String[]{});   // reuse existing CLI
                break;

            case "2":
                // Launch GUI safely on Event Dispatch Thread
                System.out.println("\nLaunching Graphical User Interface...\n");
                SwingUtilities.invokeLater(() -> {
                    new MainGUI(); // GUI main frame
                });
                break;

            default:
                System.out.println("Invalid choice. Please restart and select 1 or 2.");
        }

        sc.close();
    }
}

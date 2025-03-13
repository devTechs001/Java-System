import gui.Splash_screen;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        System.out.printf("Starting...\n");
        try {
            // Set the Dark FormDev Look and Feel
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");

            // Launch the splash screen
            SwingUtilities.invokeLater(Splash_screen::new);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
        }
    }
}
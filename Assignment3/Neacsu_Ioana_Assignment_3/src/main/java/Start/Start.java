package Start;

import Presetation.OpenMenu;

import javax.swing.*;

/**
 * The main entry point of the application.
 */
public class Start {
    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(OpenMenu::new);
    }
}
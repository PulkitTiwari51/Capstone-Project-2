package ui; // correct - it belongs in 'ui' folder

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Start the application in Event Dispatch Thread (best practice for Swing)
        SwingUtilities.invokeLater(() -> {
            new Sign_in(); // opens the Sign_in screen first
            // (Make sure Sign_in.java is correctly created in the same 'ui' package.)
        });
    }
}

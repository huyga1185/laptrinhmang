package ltm.ntn.views.utils;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog {
    public static void showError(Component parent, String message) {
        JOptionPane pane = new JOptionPane(
                message,
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION
        );

        JDialog dialog = pane.createDialog(parent, "Error");

        // Set cursor hand cho tất cả nút bên trong JOptionPane
        setHandCursorToButtons(pane);

        dialog.setVisible(true);
    }

    private static void setHandCursorToButtons(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else if (c instanceof Container) {
                setHandCursorToButtons((Container) c);
            }
        }
    }
}

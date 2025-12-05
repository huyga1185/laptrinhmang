package ltm.ntn.views.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {
    private boolean selected = false;

    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color HOVER_COLOR = new Color(31, 128, 255);
    private static final Color SELECTED_COLOR = new Color(31, 128, 255);
    private static final Color SELECTED_BG = new Color(255, 255, 255, 25); // nền nhạt

    public SidebarButton(String text) {
        super(text);

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setBackground(new Color(0, 0, 0, 0));

        setForeground(NORMAL_COLOR);
        setHorizontalAlignment(SwingConstants.LEFT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 5));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected)
                    setForeground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected)
                    setForeground(NORMAL_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                doClick();
            }
        });
    }

    public void setSelectedState(boolean value) {
        this.selected = value;

        if (selected) {
            setForeground(SELECTED_COLOR);
            setBackground(SELECTED_BG);
        } else {
            setForeground(NORMAL_COLOR);
            setBackground(new Color(0, 0, 0, 0));
        }
    }

    public boolean isSelectedState() {
        return selected;
    }
}

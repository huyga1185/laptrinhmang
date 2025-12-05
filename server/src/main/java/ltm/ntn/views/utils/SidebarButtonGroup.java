package ltm.ntn.views.utils;

public class SidebarButtonGroup {
    private SidebarButton selectedButton;

    public void add(SidebarButton btn) {
        btn.addActionListener(e -> setSelected(btn));
    }

    public void setSelected(SidebarButton btn) {
        if (selectedButton != null) {
            selectedButton.setSelectedState(false);
        }
        selectedButton = btn;
        selectedButton.setSelectedState(true);
    }
}

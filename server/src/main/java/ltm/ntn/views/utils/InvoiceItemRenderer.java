package ltm.ntn.views.utils;

import ltm.ntn.share.dto.responses.gettings.GetInvoiceResponse;

import javax.swing.*;
import java.awt.*;

public class InvoiceItemRenderer extends JPanel implements ListCellRenderer<GetInvoiceResponse> {

    // MIN-WIDTH CHO CỘT
    private static final int MIN_INVOICE_CODE = 120;
    private static final int MIN_COUPON_CODE = 100;
    private static final int MIN_DISCOUNT_TYPE = 100;
    private static final int MIN_DISCOUNT_AMOUNT = 80;
    private static final int MIN_CREATED_AT = 150;

    private final JLabel lblInvoiceCode = new JLabel();
    private final JLabel lblCouponCode = new JLabel();
    private final JLabel lblDiscountType = new JLabel();
    private final JLabel lblDiscountAmount = new JLabel();
    private final JLabel lblCreatedAt = new JLabel();

    public InvoiceItemRenderer() {
        setOpaque(true);
        setLayout(new GridBagLayout());
        setBorder(null);

        addColumn(lblInvoiceCode, 0, MIN_INVOICE_CODE, 1.2);
        addColumn(lblCouponCode, 1, MIN_COUPON_CODE, 1.0);
        addColumn(lblDiscountType, 2, MIN_DISCOUNT_TYPE, 1.0);
        addColumn(lblDiscountAmount, 3, MIN_DISCOUNT_AMOUNT, 0.6);
        addColumn(lblCreatedAt, 4, MIN_CREATED_AT, 1.4);
    }

    /**
     * Cột với minWidth + weightx để GIÃN theo tỉ lệ và vẫn THẲNG
     */
    private void addColumn(JLabel lbl, int col, int minWidth, double weight) {
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        lbl.setOpaque(false);

        // set MIN SIZE để tránh bị ép quá nhỏ
        lbl.setMinimumSize(new Dimension(minWidth, 30));

        // set PREFERRED để dựa theo MIN WIDTH nhưng vẫn có thể giãn
        lbl.setPreferredSize(new Dimension(minWidth, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = 0;

        gbc.weightx = weight; // GIÃN THEO TỈ LỆ
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);

        add(lbl, gbc);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends GetInvoiceResponse> list,
            GetInvoiceResponse invoice,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (invoice != null) {
            lblInvoiceCode.setText(invoice.getInvoiceCode());
            lblCouponCode.setText(invoice.getCouponCode());
            lblDiscountType.setText(invoice.getDiscountType());
            lblDiscountAmount.setText(String.valueOf(invoice.getDiscountAmount()));
            lblCreatedAt.setText(invoice.getCreatedAt().toString());
        }

        setBackground(isSelected ? new Color(210, 225, 255) : Color.WHITE);

        return this;
    }
}

package ltm.ntn.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ltm.ntn.share.enums.DiscountType;

import java.time.LocalDate;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    private String id;
    private String couponCode;
    private DiscountType discountType;
    private double discountAmount;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private double minimumPurchaseAmount;
    private int maxRedemptions;
    private int redemptions;
}
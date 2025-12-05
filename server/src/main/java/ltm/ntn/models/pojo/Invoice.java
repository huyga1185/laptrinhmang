package ltm.ntn.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private String id;
    private String couponId;
    private String invoiceCode;
    private double totalAmount;
    private LocalDateTime createdAt;
}

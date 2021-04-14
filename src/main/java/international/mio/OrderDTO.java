package international.mio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDTO(String customerNumber, String gln, String reference, LocalDateTime orderDate,
                       LocalDate desiredDeliveryDate, Set<OrderItemDTO> items) {
}

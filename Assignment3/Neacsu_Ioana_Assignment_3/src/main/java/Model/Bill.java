package Model;

import java.time.LocalDateTime;

/**
 * Represents a bill object, containing information about a client's order.
 */
public record Bill(int clientId, String product, int quantity, double totalPrice , LocalDateTime timestamp) {
}

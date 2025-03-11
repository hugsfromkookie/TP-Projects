package DataAccess;

import Model.Bill;
import Connection.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Bill entities.
 */
public class BillDAO {

    /**
     * Inserts a bill into the database.
     *
     * @param bill the bill object to insert
     */
    public void insertBill(Bill bill) {
        String query = "INSERT INTO Log (clientId, product, quantity, totalprice, timeorder) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bill.clientId());
            statement.setString(2, bill.product());
            statement.setInt(3, bill.quantity());
            statement.setDouble(4, bill.totalPrice());
            statement.setTimestamp(5, Timestamp.valueOf(bill.timestamp()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

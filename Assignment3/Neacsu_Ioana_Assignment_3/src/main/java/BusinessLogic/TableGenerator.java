package BusinessLogic;

import DataAccess.AbstractDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The TableGenerator class is responsible for generating a JTable
 * from a list of objects retrieved from the database using an AbstractDAO.
 *
 * @param <T> the type of the objects to be displayed in the table
 */
public class TableGenerator<T> {

    private final AbstractDAO<T> dao;
    private final Class<T> type;

    /**
     * Constructs a TableGenerator with the specified DAO and type.
     *
     * @param dao  the AbstractDAO used to access the database
     * @param type the Class type of the objects to be displayed in the table
     */
    public TableGenerator(AbstractDAO<T> dao, Class<T> type) {
        this.dao = dao;
        this.type = type;
    }

    /**
     * Generates a JTable populated with data retrieved from the database.
     * The table columns correspond to the fields of the objects of type T.
     *
     * @return a JTable displaying the data from the database
     */
    public JTable generateTable() {
        List<T> objects = dao.findAll();
        if (objects == null || objects.isEmpty()) {
            return new JTable(new DefaultTableModel());
        }

        Field[] fields = type.getDeclaredFields();
        String[] columnNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            columnNames[i] = fields[i].getName();
        }

        Object[][] rowData = new Object[objects.size()][fields.length];
        try {
            for (int i = 0; i < objects.size(); i++) {
                T obj = objects.get(i);
                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true);
                    rowData[i][j] = fields[j].get(obj);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        return new JTable(tableModel);
    }
}

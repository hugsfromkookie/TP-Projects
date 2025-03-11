package DataAccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.ConnectionFactory;

/**
 * This class serves as a generic Data Access Object (DAO) providing common CRUD operations for entities.
 * @param <T> The type of entity this DAO operates on.
 * Developed from @Source: https://gitlab.com/utcn_dsrl/pt-reflection-example
 */
public class AbstractDAO<T> {
	protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

	private final Class<T> type;

	/**
	 * Constructs a new AbstractDAO instance.
	 * Determines the entity type based on the generic type parameter.
	 */
	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	}

	/**
	 * Creates a SELECT query based on the provided field.
	 * @param field The field to filter the query by.
	 * @return The generated SELECT query.
	 */
	private String createSelectQuery(String field) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" * ");
		sb.append(" FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE " + field + " =?");
		return sb.toString();
	}

	/**
	 * Retrieves all entities from the database.
	 * @return A list of all entities found in the database.
	 */
	public List<T> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM " + type.getSimpleName();
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			return createObjects(resultSet);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Finds an entity by its ID.
	 * @param id The ID of the entity to find.
	 * @return The found entity, or null if not found.
	 */
	public T findById(int id) {
		return findByField("id", id);
	}

	/**
	 * Finds an entity by its name.
	 * @param name The name of the entity to find.
	 * @return The found entity, or null if not found.
	 */
	public T findByName(String name) {
		return findByField("name", name);
	}

	/**
	 * Finds an entity by a specified field value.
	 * @param fieldName The name of the field to filter by.
	 * @param value The value of the field to match.
	 * @return The found entity, or null if not found.
	 */
	private T findByField(String fieldName, Object value) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery(fieldName);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setObject(1, value);
			resultSet = statement.executeQuery();
			List<T> results = createObjects(resultSet);
			if (!results.isEmpty()) {
				return results.getFirst();
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findByField " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Creates a list of objects of type T from the given ResultSet.
	 * Each row in the ResultSet is used to create a new instance of T,
	 * where each field in T is set with the corresponding value from the ResultSet row.
	 * @param resultSet The ResultSet containing the data to create objects from.
	 * @return A list of objects of type T created from the ResultSet data.
	 */
	private List<T> createObjects(ResultSet resultSet) {
		List<T> list = new ArrayList<T>();
		Constructor[] ctors = type.getDeclaredConstructors();
		Constructor ctor = null;
		for (int i = 0; i < ctors.length; i++) {
			ctor = ctors[i];
			if (ctor.getGenericParameterTypes().length == 0)
				break;
		}
		try {
			while (resultSet.next()) {
				ctor.setAccessible(true);
				T instance = (T)ctor.newInstance();
				for (Field field : type.getDeclaredFields()) {
					String fieldName = field.getName();
					Object value = resultSet.getObject(fieldName);
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
					Method method = propertyDescriptor.getWriteMethod();
					method.invoke(instance, value);
				}
				list.add(instance);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Inserts a new entity into the database.
	 * @param t The entity to insert.
	 * @return The inserted entity, or null if insertion fails.
	 */
	public T insert(T t) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createInsertQuery(t);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setStatementParameters(statement, t);
			statement.executeUpdate();

			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				Field idField = type.getDeclaredField("id");
				idField.setAccessible(true);
				idField.set(t, generatedKeys.getInt(1));
			}

			return t;
		} catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Creates a SELECT query based on the provided field.
	 * @param t The entity to insert.
	 * @return The generated SELECT query.
	 */
	private String createInsertQuery(T t) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(type.getSimpleName());
		sb.append(" (");
		boolean first = true;
		for (Field field : type.getDeclaredFields()) {
			if (!field.getName().equals("id")) {
				if (!first) sb.append(", ");
				sb.append(field.getName());
				first = false;
			}
		}
		sb.append(") VALUES (");
		first = true;
		for (Field field : type.getDeclaredFields()) {
			if (!field.getName().equals("id")) {
				if (!first) sb.append(", ");
				sb.append("?");
				first = false;
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Updates an existing entity in the database.
	 * @param t The entity to update.
	 * @return The updated entity, or null if update fails.
	 */
	public T update(T t) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createUpdateQuery(t);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			setStatementParameters(statement, t);
			statement.setObject(type.getDeclaredFields().length, getFieldValue(t, "id"));
			statement.executeUpdate();

			return t;
		} catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}

	/**
	 * Creates a UPDATE query based on the provided field.
	 * @param t The entity to update.
	 * @return The generated UPDATE query.
	 */
	private String createUpdateQuery(T t) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(type.getSimpleName());
		sb.append(" SET ");
		boolean first = true;
		for (Field field : type.getDeclaredFields()) {
			if (!field.getName().equals("id")) {
				if (!first) sb.append(", ");
				sb.append(field.getName()).append(" = ?");
				first = false;
			}
		}
		sb.append(" WHERE id = ?");
		return sb.toString();
	}

	/**

	 Sets the parameters of the given PreparedStatement based on the fields of the object T.
	 It sets the parameters for all fields except the "id" field, as "id" is usually auto-generated.
	 @param statement The PreparedStatement to set parameters for.
	 @param t The object T containing the values for the parameters.
	 @throws SQLException If a database access error occurs.
	 @throws NoSuchFieldException If a field with the specified name is not found.
	 @throws IllegalAccessException If the specified object is not accessible.
	 */
	private void setStatementParameters(PreparedStatement statement, T t) throws SQLException, NoSuchFieldException, IllegalAccessException {
		Field[] fields = type.getDeclaredFields();
		int index = 1;
		for (Field field : fields) {
			if (!field.getName().equals("id")) {
				field.setAccessible(true);
				statement.setObject(index++, field.get(t));
			}
		}
	}

	/**

	 Retrieves the value of the specified field from the object T.
	 @param t The object T from which to retrieve the field value.
	 @param fieldName The name of the field to retrieve the value for.
	 @return The value of the specified field from the object T.
	 @throws NoSuchFieldException If a field with the specified name is not found.
	 @throws IllegalAccessException If the specified object is not accessible.
	 */
	private Object getFieldValue(T t, String fieldName) throws NoSuchFieldException, IllegalAccessException {
		Field field = type.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(t);
	}

	/**
	 * Deletes an entity from the database by its ID.
	 * @param id The ID of the entity to delete.
	 * @return true if deletion is successful, false otherwise.
	 */
	public boolean delete(int id) {
		Connection connection = null;
		PreparedStatement statement = null;
		String query = createDeleteQuery();
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			int affectedRows = statement.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
			return false;
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
	}

	/**
	 * Creates a DELETE query based on the provided field.
	 * @return The generated DELETE query.
	 */
	private String createDeleteQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE id = ?");
		return sb.toString();
	}

}

package hello.world.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static hello.world.MySQL.LambdaExceptionUtil.rethrowSupplier;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 *
 * @author Huskehhh
 * @author tips48
 * @author Ktar5
 */
public abstract class Database {

    // Connection to the database
    protected Connection connection;

    /**
     * Creates a new Database
     */
    protected Database() {
        this.connection = null;
    }

    /**
     * Checks if a connection is open with the database
     *
     * @return true if the connection is open
     * @throws SQLException if the connection cannot be checked
     */
    private boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * Gets the connection with the database
     *
     * @return Connection with the database, will initialise new connection if dead
     * @throws SQLException if cannot get a connection
     */
    public abstract Connection getConnection() throws SQLException;

    /**
     * Closes the connection with the database
     *
     * @throws SQLException if the connection cannot be closed
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * Executes a SQL Query and returns a ResultSet
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return {@link ResultSet}
     * @throws SQLException If the query cannot be executed
     */
    public ResultSet query(String query) throws SQLException {
        if (!checkConnection()) {
            connection = getConnection();
        }

        PreparedStatement statement = connection.prepareStatement(query);

        return statement.executeQuery();
    }

    /**
     * Executes a SQL Query
     * If the connection is closed, it will be opened
     *
     * @param query    Query to be run
     * @param consumer to pass {@link ResultSet} to
     * @throws SQLException If the query cannot be executed
     */
    public void query(String query, SQLConsumer<ResultSet> consumer) throws SQLException {
        ResultSet resultSet = query(query);

        consumer.accept(resultSet);

        resultSet.close();
        resultSet.getStatement().close();
    }

    /**
     * Executes a SQL Query and returns a {@link CompletableFuture} of a {@link ResultSet}
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return {@link CompletableFuture} containing {@link ResultSet} object
     * @throws {@link SQLException} If the query cannot be executed
     */
    public CompletableFuture<ResultSet> queryAsync(String query) throws SQLException {
        return CompletableFuture.supplyAsync(rethrowSupplier(() -> query(query)));
    }

    /**
     * Executes an Update SQL Update
     * See {@link java.sql.PreparedStatement#executeUpdate()}
     * If the connection is closed, it will be opened
     *
     * @param update Update to be run
     * @return result code, see {@link java.sql.PreparedStatement#executeUpdate()}
     * @throws SQLException If the query cannot be executed
     */
    public int update(String update) throws SQLException {
        if (!checkConnection()) {
            connection = getConnection();
        }

        PreparedStatement statement = connection.prepareStatement(update);
        int result = statement.executeUpdate();

        statement.close();

        return result;
    }

    /**
     * Executes an SQL update asynchronously
     *
     * @param update Update to be run
     * @return {@link CompletableFuture} containing result code, see {@link java.sql.PreparedStatement#executeUpdate()}
     * @throws {@link SQLException}           If the query cannot be executed
     */
    public CompletableFuture<Integer> updateAsync(String update) throws SQLException {
        return CompletableFuture.supplyAsync(rethrowSupplier(() -> update(update)));
    }


    public void putAll(Map<String, String> users) throws SQLException {

        if (!checkConnection()) {
            connection = getConnection();
        }

        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        String sql = "INSERT INTO USERS_DATA ( NAME, AGE) VALUES(?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);

            for (String name : users.keySet()) {
                // preparedStatement.setLong(1, System.nanoTime()+ random.nextInt(1000));
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, users.get(name));
                preparedStatement.addBatch();            
            }
            preparedStatement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
                        
    }
}
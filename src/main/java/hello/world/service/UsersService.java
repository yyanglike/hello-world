package hello.world.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hello.world.Dao.UserDao;
import hello.world.Entity.Users;
import hello.world.utils.Util;

public class UsersService  extends Util implements UserDao{

    private Connection connection = getConnection();

    private Random random = new Random();
    public UsersService() {
        // String sql = "CREATE TABLE USERS_DATA(ID BIGINT PRIMARY KEY auto_increment, NAME VARCHAR(255), AGE VARCHAR(255) ); ";
        // try {
        //     Statement createStatement = connection.createStatement();
        //     createStatement.execute(sql);
        //     System.out.println("Create");
        // } catch (SQLException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

    @Override
    public void add(Users user) throws SQLException {
        PreparedStatement preparedStatement = null;

        String sql = "INSERT INTO USERS_DATA (ID, NAME, AGE) VALUES(?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getAge());
            preparedStatement.executeUpdate();
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

    @Override
    public List<Users> getAll() throws SQLException {
        // TODO Auto-generated method stub

        List<Users> usersList = new ArrayList<>();

        String sql = "SELECT ID, NAME, AGE FROM USERS_DATA";

        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Users address = new Users();
                address.setId(resultSet.getLong("ID"));
                address.setName(resultSet.getString("NAME"));
                address.setAge(resultSet.getString("AGE"));
                usersList.add(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return usersList;        
    }

    @Override
    public void update(Users user) throws SQLException {
        // TODO Auto-generated method stub
        PreparedStatement preparedStatement = null;

        String sql = "UPDATE USERS_DATA SET  NAME=?, AGE=? WHERE ID=?";

        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getAge());
            preparedStatement.setLong(3, user.getId());

            preparedStatement.executeUpdate();
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

    @Override
    public void remove(Users user) throws SQLException {
        // TODO Auto-generated method stub
        PreparedStatement preparedStatement = null;

        String sql = "DELETE FROM USERS_DATA WHERE ID=?";

        try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, user.getId());

            preparedStatement.executeUpdate();
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

    @Override
    public void putAll(Map<String, String> users) throws SQLException {
        // TODO Auto-generated method stub

        if(connection.isClosed()){
            connection = getConnection();
        }

        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        String sql = "INSERT INTO USERS_DATA (ID, NAME, AGE) VALUES(?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(sql);

            for (String name : users.keySet()) {
                preparedStatement.setLong(1, System.nanoTime()+ random.nextInt(1000));
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, users.get(name));
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

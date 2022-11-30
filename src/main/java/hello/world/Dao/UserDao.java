package hello.world.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import hello.world.Entity.Users;

public interface UserDao {
    void add(Users user) throws SQLException;
    List<Users> getAll() throws SQLException;
    void update(Users user) throws SQLException;
    void remove(Users user) throws SQLException;
    void putAll(Map<String,String> users) throws SQLException;
}

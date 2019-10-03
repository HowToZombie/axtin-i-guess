package org.axtin.user;

import org.axtin.container.facade.Container;
import org.axtin.database.Database;
import org.axtin.user.role.DonatorRole;
import org.axtin.user.role.PrisonRole;
import org.axtin.user.role.StaffRole;
import org.bukkit.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserRepository {
    private Map<UUID, User> users = new HashMap<>(1000);
    private Database database = null;

    public UserRepository(Database database) {
        this.database = database;
    }

    public Map<UUID, User> getUsers() {
        return this.users;
    }

    public User getUser(UUID uuid) {
        if (users.containsKey(uuid)) {
            return users.get(uuid);
        }

        return null;
    }

    public boolean contains(UUID uuid) {
        if (users.containsKey(uuid)) {
            return true;
        }

        return false;
    }

    public void add(UUID uuid, User user) {
        users.put(uuid, user);
    }

    public void remove(UUID uuid) {
        users.remove(uuid);
    }

    public synchronized boolean offsetContains(UUID uuid) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = database.getConnection();
            statement = connection.prepareStatement("SELECT * FROM users WHERE uuid = '" + uuid.toString() + "'");
            result = statement.executeQuery();

            if (result.first()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public synchronized User offsetGet(UUID uuid) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            connection = database.getConnection();
            statement = connection.prepareStatement("SELECT * FROM users WHERE uuid = '" + uuid.toString() + "'");
            result = statement.executeQuery();

            if (result.next()) {
                UserData data = new UserData(uuid, PrisonRole.getRole(result.getInt("prisonrole")), DonatorRole.getRole(result.getInt("donatorrole")), StaffRole.getRole(result.getInt("staffrole")), result.getBigDecimal("balance").doubleValue(), result.getInt("tokens"));

                return new User(Container.get(Server.class).getPlayer(uuid), data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // TODO: Make a boolean for when we start checking if users are successfully inserted
    public synchronized void offsetInsert(UUID uuid) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = database.getConnection();
            statement = connection.prepareStatement("INSERT INTO users (uuid, prisonrole, staffrole, donatorrole) VALUES ('" + uuid.toString() + "', " + PrisonRole.A.getIdentifier() + ", " + StaffRole.NONE.getIdentifier() + ", " + DonatorRole.NONE.getIdentifier() + ")");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO: Make a boolean for when we start checking if users are successfully updated
    public synchronized void offsetUpdate(UUID uuid, String key, Object value) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = database.getConnection();
            statement = connection.prepareStatement("UPDATE users SET " + key + " = '" + value + "' WHERE uuid = '" + uuid.toString() + "'");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateSQL(User user){
        Connection connection = null;
        PreparedStatement statement = null;

        UserData data = user.getData();
        PrisonRole prisonRole = data.getPrisonRole();
        DonatorRole donatorRole = data.getDonatorRole();
        StaffRole staffRole = data.getStaffRole();
        int tokens = data.getTokens();
        double balance = data.getBalance();

        try {
            connection = database.getConnection();
            statement = connection.prepareStatement("UPDATE users SET " + "prisonrole" + " = '" + prisonRole.getIdentifier() + "', staffrole = '"+staffRole.getIdentifier() +"', donatorrole = '"+donatorRole.getIdentifier()+"', balance = '" + balance +"', tokens = '"+ tokens+"' WHERE uuid = '" + data.getUniqueId().toString() + "'");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

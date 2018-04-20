package test;

import Models.User;
import org.junit.Test;

import static junit.framework.Assert.*;

public class UserTest {
    @Test
    public void saveAndDeleteUser() {
        User user = new User(0, "new name", "new lastname", "new password", "металлург");

        user.saveToDB();

        User foundUser = User.readUserFromDB("new name", "new lastname");
        assertNotNull("User wasn't saved", foundUser);
        assertEquals("Name is not correctly saved", "new name", foundUser.getName());
        assertEquals("Lastname is not correctly saved", "new lastname", foundUser.getLastname());
        assertEquals("Password is not correctly saved", "new password", foundUser.getPassword());
        assertEquals("Role is not correctly saved", "металлург", foundUser.getRole());

        assertTrue("Existing user not found by userExists()", User.userExists("new name", "new lastname"));

        User loggedInUser = User.loginAndReturnUser("new name", "new lastname", "new password");
        assertNotNull("User cannot loginAndReturnUser", loggedInUser);
        assertEquals("Login name is not correctly received", "new name", loggedInUser.getName());
        assertEquals("Login lastname is not correctly received", "new lastname", loggedInUser.getLastname());
        assertEquals("Login password is not correctly received", "new password", loggedInUser.getPassword());
        assertEquals("Login role is not correctly received", "металлург", loggedInUser.getRole());

        user.deleteFromDB();
        assertNull("User wasn't deleted", User.readUserFromDB("new name", "new lastname"));
        assertFalse("Non-existing user found by userExists()", User.userExists("new name", "new lastname"));
    }

    @Test
    public void defaultAdminCheck() {
        User nonAdmin = new User(0, "new name", "new lastname", "new password", "металлург");
        User admin = new User(0, "Администратор", "Администратор", "Администратор", "администратор");

        assertFalse("Not default admin was marked as default admin", nonAdmin.isDefaultAdmin());
        assertTrue("Default admin wasn't marked as default admin", admin.isDefaultAdmin());
    }

    @Test
    public void nonExistingUserCannotBeLoggedIn() {
        User loggedInUser = User.loginAndReturnUser("bla", "bla", "bla");
        assertNull("Non existing user logged in", loggedInUser);
    }

    @Test
    public void defaultAdminExists() {
        assertNotNull("Default user doesn't exist", User.readUserFromDB("Администратор", "Администратор"));
    }
}

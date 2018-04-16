package test;

import Models.User;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

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

        assertEquals("Existing user not found by userExists()", true, User.userExists("new name", "new lastname"));

        User loggedInUser = User.login("new name", "new lastname", "new password");
        assertNotNull("User cannot login", loggedInUser);
        assertEquals("Login name is not correctly received", "new name", loggedInUser.getName());
        assertEquals("Login lastname is not correctly received", "new lastname", loggedInUser.getLastname());
        assertEquals("Login password is not correctly received", "new password", loggedInUser.getPassword());
        assertEquals("Login role is not correctly received", "металлург", loggedInUser.getRole());

        user.deleteFromDB();
        assertEquals("User wasn't deleted", null, User.readUserFromDB("new name", "new lastname"));
        assertEquals("Non-existing user found by userExists()", false, User.userExists("new name", "new lastname"));
    }

    @Test
    public void defaultAdminCheck() {
        User nonAdmin = new User(0, "new name", "new lastname", "new password", "металлург");
        User admin = new User(0, "Администратор", "Администратор", "Администратор", "администратор");

        assertEquals("Not default admin was marked as default admin", false, nonAdmin.isDefaultAdmin());
        assertEquals("Default admin wasn't marked as default admin", true, admin.isDefaultAdmin());
    }

    @Test
    public void nonExistingUserCannotBeLoggedIn() {
        User loggedInUser = User.login("bla", "bla", "bla");
        assertEquals("Non existing user logged in", null, loggedInUser);
    }
}

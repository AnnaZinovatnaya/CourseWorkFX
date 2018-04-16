package test;

import Models.User;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class MyTest {

    @Test
    public void loginOfNonExistantUserReturnsFalse() {
        User user = new User(8, "a", "aaa", "aaaa", "aaaaa");

        boolean result = user.isDefaultAdmin();
        assertTrue(result == false);

    }
}

package ca.ualberta.cs.cmput402.ghdow;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static ca.ualberta.cs.cmput402.ghdow.Main.getOAuthToken;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mockConstruction;

@RunWith(MockitoJUnitRunner.class) class MyGithubTest {
    @Test
    void getFirstIssueCreateDate() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(100, Calendar.JANUARY, 1, 1, 1, 1);
        Date whenever = calendar.getTime();
        try (MockedConstruction<GHIssueWrapper> ignored = mockConstruction(
                GHIssueWrapper.class,
                (mock, context) -> Mockito.when(mock.getCreatedAt()).thenReturn(whenever)
        )) {

            String token = getOAuthToken();
            MyGithub my = new MyGithub(token);
            System.out.println("Logged in as " + my.getGithubName());

            Date r = my.getFirstIssueCreateDate();
            assertSame(whenever, r);
        }
    }
}


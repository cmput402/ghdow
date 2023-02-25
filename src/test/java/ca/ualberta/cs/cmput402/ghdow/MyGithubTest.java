package ca.ualberta.cs.cmput402.ghdow;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.mockito.MockedConstruction;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class) class MyGithubTest {
    @Test
    void getIssueCreateDates() throws IOException {
        String token = "I am a fake token";
        MyGithub my = new MyGithub(token);
        assertNotNull(my);
        my.gitHub = mock(GitHub.class);
        String fakeRepoName = "fakeRepo";
        GHRepository fakeRepo = mock(GHRepository.class);
        my.myRepos = new HashMap<>();
        my.myRepos.put(fakeRepoName, fakeRepo);

        final int DATES = 30;

        ArrayList<GHIssue> superFakeIssues = new ArrayList<>();
        ArrayList<Date> expectedDates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i < DATES+1; i++) {
            calendar.set(100, Calendar.JANUARY, i, 1, 1, 1);
            expectedDates.add(calendar.getTime());
            superFakeIssues.add(null);
        }

        when(fakeRepo.getIssues(GHIssueState.CLOSED)).thenReturn(superFakeIssues);

        final int[] j = {0}; // everything in Java is a pointer except primitives
        List<Date> actualDates;
        try (MockedConstruction<GHIssueWrapper> ignored = mockConstruction(
                GHIssueWrapper.class,
                (mock, context) -> {
                    if (j[0] < DATES) {
                        when(mock.getCreatedAt()).thenReturn(expectedDates.get(j[0]));
                    } else {
                        when(mock.getCreatedAt()).thenReturn(null);
                    }
                    j[0]++;
                }
        )) {
            actualDates = my.getIssueCreateDates();
        }
        assertEquals(expectedDates.size(), DATES);
        assertEquals(actualDates.size(), DATES);

        for (int i = 1; i < DATES; i++) {
            assertEquals(expectedDates.get(i), actualDates.get(i));
        }
    }
}


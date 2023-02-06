package ca.ualberta.cs.cmput402.ghdow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.kohsuke.github.*;

import java.nio.file.Path;
import java.util.*;

public class MyGithub {
    protected GitHub gitHub;
    protected GHPerson myself;
    protected Map<String, GHRepository> myRepos;
    protected List<GHCommit> myCommits;
    protected String getOAuthToken () throws IOException {
        Path tokenFile = Paths.get(
                System.getProperty("user.home"),
                "githubOAuthToken.txt"
        );
        String token = Files.readString(tokenFile);
        return token.strip();
    }
    public MyGithub() throws IOException {
        String token = getOAuthToken();
        gitHub = new GitHubBuilder().withOAuthToken(token).build();
    }

    protected GHPerson getMyself() throws IOException {
        if (myself == null) {
            myself = gitHub.getMyself();
        }
        return myself;
    }

    public String getGithubName() throws IOException {
        return gitHub.getMyself().getLogin();
    }

    protected List<GHRepository> getRepos() throws IOException {
        if (myRepos == null) {
            myRepos = getMyself().getRepositories();
        }
        return new ArrayList<>(myRepos.values());
    }

    static private int argMax(int[] days) {
        int max = Integer.MIN_VALUE;
        int arg = -1;
        for (int i = 0; i < days.length; i++) {
            if (days[i] > max) {
                max = days[i];
                arg = i;
            }
        }
        return arg;
    }

    static private String intToDay(int day) {
        return switch (day) {
            case Calendar.SUNDAY -> "Sunday";
            case Calendar.MONDAY -> "Monday";
            case Calendar.TUESDAY -> "Tuesday";
            case Calendar.WEDNESDAY -> "Wednesday";
            case Calendar.THURSDAY -> "Thursday";
            case Calendar.FRIDAY -> "Friday";
            case Calendar.SATURDAY -> "Saturday";
            default -> throw new IllegalArgumentException("Not a day: " + day);
        };
    }

    public String getMostPopularDay() throws IOException {
        final int SIZE = 8;
        int[] days = new int[SIZE];
        Calendar cal = Calendar.getInstance();
        for (GHCommit commit: getCommits()) {
            Date date = commit.getCommitDate();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            days[day] += 1;
        }
        return intToDay(argMax(days));
    }

    protected Iterable<? extends GHCommit> getCommits() throws IOException {
        if (myCommits == null) {
            myCommits = new ArrayList<>();
            int count = 0;
            for (GHRepository repo: getRepos()) {
                System.out.println("Loading commits: repo " + repo.getName());
                try {
                    for (GHCommit commit : repo.queryCommits().author(getGithubName()).list()) {
                        myCommits.add(commit);
                        count++;
                        if (count % 100 == 0) {
                            System.out.println("Loading commits: " + count);
                        }
                    }
                } catch (GHException e) {
                    if (!e.getCause().getMessage().contains("Repository is empty")) {
                        throw e;
                    }
                }
            }
        }
        return myCommits;
    }
}
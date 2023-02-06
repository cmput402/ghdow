package cmput402.cs.ualberta.ca;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MyGithub my = new MyGithub();
        System.out.println("Logged in as " + my.getGithubName());
        System.out.println("Most often commits on: " + my.getMostPopularDay());
    }
}
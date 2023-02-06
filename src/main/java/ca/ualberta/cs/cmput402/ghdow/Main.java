package ca.ualberta.cs.cmput402.ghdow;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MyGithub my = new MyGithub();
        System.out.println("Logged in as " + my.getGithubName());
        System.out.println("Most often commits on: " + my.getMostPopularDay());
    }
}
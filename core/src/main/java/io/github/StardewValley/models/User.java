package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Gender;

public class User {
    private static User currentUser;
    private String username;
    public String password;
    private String nickname;
    private String email;
    private Gender gender;
    private SecurityQuestion securityQuestion;
    public String securityAnswer;
    private Player player;
    private int highestGold;
    private int totalGamesPlayed;


    // کانستراکتور اصلی
    public User(String username, String nickname, String email, String password, String question, String answer, String gender, int score) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = parseGender(gender);
        this.securityQuestion = new SecurityQuestion(question, answer);
        this.totalGamesPlayed = 0;
        this.highestGold = score;
    }

    public User(String username, String password, String nickname, String email, Gender gender) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.totalGamesPlayed = 0;
        this.highestGold = 0;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecurityAnswer() {
        return this.securityQuestion != null ? this.securityQuestion.getAnswer() : null;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }
    public void setSecurityQuestion(String question, String answer) {
        this.securityQuestion = new SecurityQuestion(question, answer);
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getHighestGold() {
        return highestGold;
    }
    public void setHighestGold(int highestGold) {
        this.highestGold = highestGold;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }
    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public String getFormattedInfo() {
        return String.format(
            "Username       : %s\n" +
                "Nickname       : %s\n" +
                "Email          : %s\n" +
                "Gender         : %s\n" +
                "Highest Score  : %d\n" +
                "Games Played   : %d",
            username,
            nickname,
            email,
            gender,
            highestGold,
            totalGamesPlayed
        );
    }

    public static Gender parseGender(String text) {
        switch (text.toLowerCase()) {
            case "male":
                return Gender.MAN;
            case "female":
                return Gender.WOMAN;
            case "other":
                return Gender.RATHER_NOT_SAY;
            default:
                throw new IllegalArgumentException("Unknown gender: " + text);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", nickname='" + nickname + '\'' +
            ", email='" + email + '\'' +
            ", gender=" + gender +
            '}';
    }
}

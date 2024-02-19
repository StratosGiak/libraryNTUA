package com.stratos.giak.libraryntua;

public class Rating {
    UserModel user;
    int score;
    String comments;

    public Rating(UserModel user, int score, String comments) {
        this.user = user;
        this.score = score;
        this.comments = comments;
    }

    public UserModel getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 1 || score > 5) throw new IllegalArgumentException("Rating must be in range [1, 5]");
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

package User;

import java.util.HashSet;
import java.util.Set;
import Post.Post;

public class User {
    public String name;
    public Post posts;
    public Set<User> followers;
    public Set<User> follows;
    public int likes;

    public User(String name) {
        this.name = name;
        this.posts = null;
        this.followers = new HashSet<>();
        this.follows = new HashSet<>();
        this.likes = 0;
    }

    public void addFollower(User follower) {
        followers.add(follower);
    }

    public void removeFollower(User follower) {
        followers.remove(follower);
    }

    public void addFollow(User user) {
        follows.add(user);
    }

    public void removeFollow(User user) {
        follows.remove(user);
    }

    public boolean isFollowing(User user) {
        return follows.contains(user);
    }

    public void addLike() {
        likes++;
    }

    public void removeLike() {
        if (likes > 0) {
            likes--;
        }
    }

    public int getLikes() {
        return likes;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public Set<User> getFollows() {
        return follows;
    }
}

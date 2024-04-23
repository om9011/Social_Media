package Post;

public class Post {
    public String post;
    public Post next;

    public Post(String post) {
        this.post = post;
        this.next = null;
    }
}

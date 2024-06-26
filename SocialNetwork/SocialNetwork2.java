package SocialNetwork;

import Exceptions.UserNotFoundException;
import Post.Post;
import User.User;

import java.sql.SQLOutput;
import java.util.*;

class SocialNetwork2 {
    Map<User, List<User>> adjacencyList;

    public SocialNetwork2() {
        adjacencyList = new HashMap<>();
    }

    public void addUser(String name, String password) {
        User user = new User(name, password);
        adjacencyList.put(user, new ArrayList<>());
    }

    public void addFriend(String name1, String name2) throws UserNotFoundException {
        User user1 = getUserByName(name1);
        User user2 = getUserByName(name2);
        if (user1 != null && user2 != null) {
            adjacencyList.get(user1).add(user2);
            adjacencyList.get(user2).add(user1);
        } else {
            throw new UserNotFoundException("Invalid user names. Friend not added.");
        }
    }

    public void addPost(String name, String postContent) throws UserNotFoundException {
        User user = getUserByName(name);
        if (user != null) {
            Post newPost = new Post(postContent);
            if (user.posts == null) {
                user.posts = newPost;
            } else {
                Post lastPost = user.posts;
                while (lastPost.next != null) {
                    lastPost = lastPost.next;
                }
                lastPost.next = newPost;
            }
        } else {
            throw new UserNotFoundException("User.User not found. Post.Post not added.");
        }
    }

    public void showPosts(String name) throws UserNotFoundException {
        User user = getUserByName(name);
        if (user != null) {
            System.out.println("Posts by " + name + ":");
            Post post = user.posts;
            while (post != null) {
                System.out.println(post.post);
                post = post.next;
            }
        } else {
            throw new UserNotFoundException("User.User not found.");
        }
    }

    public void showFriends(String name) throws UserNotFoundException {
        User user = getUserByName(name);
        if (user != null) {
            Set<String> visited = new HashSet<>();
            System.out.println("Direct friends of " + name + ":");
            for (User friend : adjacencyList.get(user)) {
                visited.add(friend.name);
            }
            System.out.println(visited);

        } else {
            throw new UserNotFoundException("User.User not found.");
        }
    }

    public void showMutuals(String name1, String name2) throws UserNotFoundException {
        User user1 = getUserByName(name1);
        User user2 = getUserByName(name2);
        if (user1 != null && user2 != null) {
            bfs(user1, user2);
        } else {
            throw new UserNotFoundException("Invalid user names.");
        }
    }

    private void bfs(User source, User target) {
        Queue<User> queue = new LinkedList<>();
        Set<User> visited = new HashSet<>();
        Map<User, User> parentMap = new HashMap<>();

        queue.offer(source);
        visited.add(source);
        parentMap.put(source, null);

        while (!queue.isEmpty()) {
            User current = queue.poll();
            if (current == target) {
                break;
            }
            for (User neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }

        // Reconstruct path from target to source
        User current = target;
        Set<User> mutuals = new HashSet<>();
        while (current != null) {
            if (current != target && current != source) {
                mutuals.add(current);
            }
            current = parentMap.get(current);
        }

        System.out.println("Mutual friends of " + source.name + " and " + target.name + ":");
        for (User mutual : mutuals) {
            System.out.println(mutual.name);
        }
    }

    public void removeFriend(String name1, String name2) throws UserNotFoundException {
        User user1 = getUserByName(name1);
        User user2 = getUserByName(name2);
        if (user1 != null && user2 != null) {
            adjacencyList.get(user1).remove(user2);
            adjacencyList.get(user2).remove(user1);
        } else {
            throw new UserNotFoundException("Invalid user names. Friend not removed.");
        }
    }

    private User getUserByName(String name) {
        for (User user : adjacencyList.keySet()) {
            if (user.name.equals(name)) {
                return user;
            }
        }
        return null;
    }

    public void suggestFriendsCommonNeighbor(String name) throws UserNotFoundException {
        User user = getUserByName(name);
        if (user != null) {
            Map<User, Integer> commonNeighbors = new HashMap<>();
            for (User friend : adjacencyList.get(user)) {
                for (User mutual : adjacencyList.get(friend)) {
                    if (!adjacencyList.get(user).contains(mutual) && !mutual.equals(user)) {
                        commonNeighbors.put(mutual, commonNeighbors.getOrDefault(mutual, 0) + 1);
                    }
                }
            }

            List<Map.Entry<User, Integer>> sortedCommonNeighbors = new ArrayList<>(commonNeighbors.entrySet());
            sortedCommonNeighbors.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            System.out.println("Suggestions for " + name + " using Common Neighbor Algorithm:");
            for (Map.Entry<User, Integer> entry : sortedCommonNeighbors) {
                System.out.println(entry.getKey().name + " (Mutual friends: " + entry.getValue() + ")");
            }
        } else {
            throw new UserNotFoundException("User.User not found.");
        }
    }


    public void showUserDetails(String name) throws UserNotFoundException {
        User user = getUserByName(name);
        if (user != null) {
            System.out.println("User.User Details for " + name + ":");
            System.out.println("Name: " + user.name);
            System.out.println("Posts:");
            Post post = user.posts;
            while (post != null) {
                System.out.println(post.post);
                post = post.next;
            }
            System.out.println("Followers:");
            for (User follower : user.followers) {
                System.out.println(follower.name);
            }
            System.out.println("Follows:");
            for (User followed : user.follows) {
                System.out.println(followed.name);
            }
            System.out.println("Likes: " + user.likes);
        } else {
            throw new UserNotFoundException("User.User not found.");
        }
    }

    public void likePost(String likerName, String postOwnerName) throws UserNotFoundException {
        User liker = getUserByName(likerName);
        User postOwner = getUserByName(postOwnerName);
        if (liker != null && postOwner != null) {
            postOwner.addLike();
            System.out.println(likerName + " liked " + postOwnerName + "'s post.");
        } else {
            throw new UserNotFoundException("Invalid user names.");
        }
    }

    public void followUser(String followerName, String followedName) throws UserNotFoundException {
        User follower = getUserByName(followerName);
        User followed = getUserByName(followedName);
        if (follower != null && followed != null) {
            follower.addFollow(followed);
            followed.addFollower(follower);
            System.out.println(followerName + " started following " + followedName + ".");
        } else {
            throw new UserNotFoundException("Invalid user names.");
        }
    }

    public void unfollowUser(String followerName, String followedName) throws UserNotFoundException {
        User follower = getUserByName(followerName);
        User followed = getUserByName(followedName);
        if (follower != null && followed != null) {
            follower.removeFollow(followed);
            followed.removeFollower(follower);
            System.out.println(followerName + " unfollowed " + followedName + ".");
        } else {
            throw new UserNotFoundException("Invalid user names.");
        }
    }

    public Map<String, Double> calculateDegreeCentrality() {
        Map<String, Double> degreeCentrality = new HashMap<>();
        int totalUsers = adjacencyList.size();

        for (User user : adjacencyList.keySet()) {
            int degree = adjacencyList.get(user).size();
            degreeCentrality.put(user.name, (double) degree / (totalUsers - 1));
        }

        return degreeCentrality;
    }

    public boolean isAuthenticatedUser(String name, String password) {
        User user = getUserByName(name);

        if (user.password.equals(password)) {
            return true;
        }
        System.out.println("Unauthorized User\n Invalid Credentials");
        return false;
    }

    public Map<String, Double> calculateEigenvectorCentrality() {
        Map<String, Double> eigenvectorCentrality = new HashMap<>();
        int maxIterations = 100;
        double tolerance = 1e-6;

        for (User user : adjacencyList.keySet()) {
            eigenvectorCentrality.put(user.name, 1.0);
        }

        for (int i = 0; i < maxIterations; i++) {
            Map<String, Double> newCentrality = new HashMap<>();
            double sumOfSquares = 0;

            for (User user : adjacencyList.keySet()) {
                double score = 0;
                for (User neighbor : adjacencyList.get(user)) {
                    score += eigenvectorCentrality.get(neighbor.name);
                }
                newCentrality.put(user.name, score);
                sumOfSquares += score * score;
            }

            double maxDifference = 0;
            for (String userName : newCentrality.keySet()) {
                newCentrality.put(userName, newCentrality.get(userName) / Math.sqrt(sumOfSquares));
                maxDifference = Math.max(maxDifference,
                        Math.abs(eigenvectorCentrality.get(userName) - newCentrality.get(userName)));
            }

            boolean converged = maxDifference < tolerance;
            eigenvectorCentrality = newCentrality;

            if (converged) {
                break;
            }
        }

        return eigenvectorCentrality;
    }

    // Convert the network to a Graphviz representation
    public void toGraphviz() {
        StringBuilder graphvizBuilder = new StringBuilder();
        graphvizBuilder.append("graph theConnections {\n"); // 'digraph' denotes a directed graph

        HashSet<String> addedEdges = new HashSet<>(); // Set to track added edges
        System.out.println(adjacencyList);
        // Loop through the adjacency list
        for (User user : adjacencyList.keySet()) {
            List<User> connections = adjacencyList.get(user);

            // Add directed edges to the Graphviz representation
            for (User connection : connections) {
                String edge = user.name + "--" + connection.name; // Define the directed edge
                String temp = connection.name + "--" + user.name;
                // Check if this directed edge has been added to avoid duplicates
                if (!addedEdges.contains(edge)) {
                    graphvizBuilder.append("    ").append(edge).append(";\n"); // Add the directed edge to the graph
                    addedEdges.add(edge); // Mark this directed edge as added
                    addedEdges.add(temp);
                }
            }
        }
        graphvizBuilder.append("}");
        System.out.println(graphvizBuilder);
    }

    public static void main(String[] args) throws UserNotFoundException {
        Scanner scanner = new Scanner(System.in);
        SocialNetwork2 socialNetwork = new SocialNetwork2();
        socialNetwork.addUser("Gore", "123");
        socialNetwork.addUser("Om", "123");
        socialNetwork.addUser("Sanskar", "123");
        socialNetwork.addUser("Asawari", "123");
        socialNetwork.addUser("John", "123");
        socialNetwork.addUser("Alice", "123");
        socialNetwork.addUser("Bob", "123");
        socialNetwork.addUser("Emma", "123");
        socialNetwork.addUser("Ella", "123");

        socialNetwork.addFriend("Gore", "Sanskar");
        socialNetwork.addFriend("Sanskar", "Om");
        socialNetwork.addFriend("Om", "Asawari");
        socialNetwork.addFriend("Asawari", "Gore");
        socialNetwork.addFriend("John", "Gore");
        socialNetwork.addFriend("John", "Alice");
        socialNetwork.addFriend("Alice", "Bob");
        socialNetwork.addFriend("Bob", "Emma");
        socialNetwork.addFriend("Emma", "Ella");

        int choice;
        String name1, name2, postContent, init_password;
        while (true) {
            System.out.println(" =========================================== ");
            System.out.println("|            SOCIAL NETWORK MENU            |");
            System.out.println(" =========================================== ");
            System.out.println("|   1\t| Add User\t\t\t\t\t\t\t|");
            System.out.println("|   2\t| Show Posts\t\t\t\t\t\t|");
            System.out.println("|   3\t| Show Friends\t\t\t\t\t\t|");
            System.out.println("|   4\t| User Functionality\t\t\t\t|");
            System.out.println("|   5\t| Show User Details\t\t\t\t\t|");
            System.out.println("|   6\t| Degree Centrality Users\t\t\t|");
            System.out.println("|   7\t| Print Network\t\t\t\t\t\t|");
            System.out.println("|   8\t| Exit\t\t\t\t\t\t\t\t|");
            System.out.println(" =========================================== ");
            System.out.print("\n\nEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("\nEnter the name of the user & password: ");
                    name1 = scanner.next();
                    init_password = scanner.next();
                    System.out.println();
                    socialNetwork.addUser(name1, init_password);
                    System.out.println("User Created Successfully !!!");
                    break;
                case 2:
                    try {
                        System.out.print("\nEnter the name of the user: ");
                        name1 = scanner.next();
                        System.out.println();
                        socialNetwork.showPosts(name1);
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.print("\nEnter the name of the user: ");
                        name1 = scanner.next();
                        System.out.println();
                        socialNetwork.showFriends(name1);
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    boolean flag11 = true;
                    System.out.print("\nEnter the names of the user : ");
                    name1 = scanner.next();

                    System.out.print("Enter the Password : ");
                    String password = scanner.next();
                    System.out.println();
                    if (socialNetwork.isAuthenticatedUser(name1, password)) {


                        while (flag11) {
                            System.out.println(" =============================================== ");
                            System.out.println("|  Option  |          Description               |");
                            System.out.println(" =============================================== ");
                            System.out.println("|    1     |  Add friend                        |");
                            System.out.println("|    2     |  Add post                          |");
                            System.out.println("|    3     |  Show posts                        |");
                            System.out.println("|    4     |  Show friends                      |");
                            System.out.println("|    5     |  Show mutual                       |");
                            System.out.println("|    6     |  Remove friend                     |");
                            System.out.println("|    7     |  Suggest friends                   |");
                            System.out.println("|    8     |  Like post                         |");
                            System.out.println("|    9     |  Follow user                       |");
                            System.out.println("|   10     |  Unfollow user                     |");
                            System.out.println("|   11     |  Exit                              |");
                            System.out.println("------------------------------------------------");


                            System.out.print("\n\nEnter your choice: ");
                            System.out.println();
                            choice = scanner.nextInt();
                            switch (choice) {
                                case 1:
                                    try {
                                        System.out.print("\nEnter the names of the users to be friends with: ");
                                        name2 = scanner.next();
                                        System.out.println();
                                        socialNetwork.addFriend(name1, name2);
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 2:
                                    try {
                                        System.out.print("\nEnter the post content: ");
                                        scanner.nextLine(); // Consume newline
                                        postContent = scanner.nextLine();
                                        System.out.println();
                                        socialNetwork.addPost(name1, postContent);
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 3:
                                    try {
                                        System.out.println();
                                        socialNetwork.showPosts(name1);
                                        System.out.println();
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 4:
                                    try {
                                        System.out.println();
                                        socialNetwork.showFriends(name1);
                                        System.out.println();
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 5:
                                    try {
                                        System.out.print("\nEnter the names of the user to show mutuals: ");
                                        name2 = scanner.next();
                                        System.out.println();
                                        socialNetwork.showMutuals(name1, name2);
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 6:
                                    try {
                                        System.out.print("\nEnter the names of the user to remove friends: ");
                                        name2 = scanner.next();
                                        System.out.println();
                                        socialNetwork.removeFriend(name1, name2);
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 7:
                                    try {
                                        System.out.println();
                                        socialNetwork.suggestFriendsCommonNeighbor(name1);
                                        System.out.println();
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 8:
                                    try {
                                        System.out.print("Enter the name of the post owner: ");
                                        name2 = scanner.next();
                                        socialNetwork.likePost(name1, name2);
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 9:
                                    try {
                                        System.out.print("Enter the name of the followed: ");
                                        name2 = scanner.next();
                                        socialNetwork.followUser(name1, name2);
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 10:
                                    try {
                                        System.out.print("Enter the name of the followed: ");
                                        name2 = scanner.next();
                                        socialNetwork.unfollowUser(name1, name2);
                                    } catch (UserNotFoundException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 11:
                                    flag11 = false;
                                    break;
                            }
                        }
                    }
                    break;
                case 5:
                    try {
                        System.out.print("Enter the name of the user: ");
                        name1 = scanner.next();
                        socialNetwork.showUserDetails(name1);
                    } catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Degree Centrality:");
                    Map<String, Double> degreeCentrality = socialNetwork.calculateDegreeCentrality();
                    for (Map.Entry<String, Double> entry : degreeCentrality.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    break;
                case 7:
                    socialNetwork.toGraphviz();
                    break;
                case 8:
                    System.exit(0);
            }
        }
    }
}
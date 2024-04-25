package SocialNetwork;

import Exceptions.UserNotFoundException;

public class PredefinedNetwork {
    public static void main(String[] args) throws UserNotFoundException {
        SocialNetwork2 socialNetwork = new SocialNetwork2();

//      Users Created
        socialNetwork.addUser("Om", "123");
        socialNetwork.addUser("Gujarathi", "123");
        socialNetwork.addUser("Sanskar", "123");
        socialNetwork.addUser("Asawari", "123");

        socialNetwork.addFriend("Om", "Sanskar");
        socialNetwork.addFriend("Om", "Gujarathi");

        socialNetwork.showPosts("Om Gore");
    }

}

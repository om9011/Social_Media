package SocialNetwork;

import Exceptions.UserNotFoundException;

public class PredefinedNetwork {
    public static void main(String[] args) throws UserNotFoundException {
        SocialNetwork2 socialNetwork = new SocialNetwork2();

//      Users Created
        socialNetwork.addUser("Om Gore");
        socialNetwork.addUser("Om Gujarathi");
        socialNetwork.addUser("Sanskar Gundecha");
        socialNetwork.addUser("Asawari Jadhav");

        socialNetwork.addFriend("Om Gore", "Sanskar Gundecha");
        socialNetwork.addFriend("Om Gore", "Om Gujarathi");

        socialNetwork.showPosts("Om Gore");
    }

}

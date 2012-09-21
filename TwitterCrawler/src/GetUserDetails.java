/**
 * This invokes two threads for getting User Details(Tweets and Friend list)
 * Creates a User Directory to store two XMLs. One for Friend List, Other for Tweets.
 */
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oauth.signpost.OAuthConsumer;

/**
 * this class invokes two threads for getting tweets and friends for the user
 * @author Transcendence
 */
public class GetUserDetails extends Thread {

    /**
     * constructor
     * @param u 
     */
    public GetUserDetails(User u, OAuthConsumer c) {
        this.usr = u;
        this.consumer = c;
    }

    /**
     * invoke two threads for a user
     * one to get tweets
     * another to get friends
     */
    @Override
    public void run() {
        try {
            /**
             * create a directory for that user
             */
            
            path = "Users/" + usr.getUserID();
            boolean success = (new File(path)).mkdir();
            
                GetUserTweets gut = new GetUserTweets(usr, path,consumer);
                gut.start();
                GetFriendList gfl = new GetFriendList(usr, path,consumer);
                gfl.start();
                
                if (gut.isAlive())
                    gut.join();
                if (gfl.isAlive())
                    gfl.join();
                
        } catch (InterruptedException ex) {
            Logger.getLogger(GetUserDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GetUserDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * private variables
     */
    private User usr;
    private String path;
    private OAuthConsumer consumer;
}

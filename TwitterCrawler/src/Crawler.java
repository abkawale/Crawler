/**
 * This class initializes and invokes the twitter crawler to crawl 
 * tweets and friend list of users in a subject's 'n' hop social graph
 * A subject's 'n' hop social graph consists of subject + all the people 
 * he/she follows + all the people who these followees follow + upto n hops
 */



import java.io.BufferedReader;
import java.io.InputStreamReader;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.signature.SignatureMethod;

public class Crawler {

    public static void main(String[] args) throws Exception {

        OAuthConsumer consumer = new DefaultOAuthConsumer(
                "ErVvNAcF92VSUFxbTuWMYg",
                "fWb4zqUKJX8NmkoxqyFwpyD1taDvz5nOaQUaTUctrQI",
                SignatureMethod.HMAC_SHA1);

        OAuthProvider provider = new DefaultOAuthProvider(consumer,
                "http://twitter.com/oauth/request_token",
                "http://twitter.com/oauth/access_token",
                "http://twitter.com/oauth/authorize");

        System.out.println("Fetching request token from Twitter...");

        // we do not support callbacks, thus pass OOB
        String authUrl = provider.retrieveRequestToken(OAuth.OUT_OF_BAND);

        System.out.println("Request token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        System.out.println("Now visit:\n" + authUrl
                + "\n... and grant this app authorization");
        System.out.println("Enter the PIN code and hit ENTER when you're done:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String pin = br.readLine();

        System.out.println("Fetching access token from Twitter...");

        provider.retrieveAccessToken(pin);

        System.out.println("Access token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());

        TwitterSocialGraph tsg = new TwitterSocialGraph();
        tsg.crawl(consumer);       
    }
}

/**
 * invokes the GetUserDetails class and passes the user ID of subject
 * @author akawale
 */

class TwitterSocialGraph {

    /**
     * @param args the command line arguments
     */
    public void crawl(OAuthConsumer c) throws InterruptedException{
        // TODO code application logic here
        consumer = c;
        User u = new User(130458288,0);
        UserQueue.enQueue(u);
        System.out.println(" size: "+UserQueue.size());
        /**
         * start crawling by doing a breadth first search around the user
         */
        long start = System.currentTimeMillis();
        while (UserQueue.hasUsers()){
            GetUserDetails gud = new GetUserDetails(UserQueue.deQueue(),consumer);
            gud.start();
            if (gud.isAlive())
                gud.join();
        }
        long finish = System.currentTimeMillis();
        System.out.println("the control is outside the while loop and the size of the list is :" + UserQueue.size());
        System.out.println("Time to crawl data = " + (finish - start)/1000 + " seconds");
    }
    private OAuthConsumer consumer;
}

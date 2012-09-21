
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import oauth.signpost.OAuthConsumer;

/**
 * this class extracts the tweets of the user
 * @author Transcendence
 */
public class GetUserTweets extends Thread {

    /*
     * Constructor	
     */
    public GetUserTweets(User u, String path, OAuthConsumer c) throws MalformedURLException {
        this.user = u;
        //uri = new URL("https://api.twitter.com/1/statuses/user_timeline.xml?user_id=" + Long.toString(u.getUserID()) + "&include_rts=true&count=100&page=1");
        this.usrPath = path;
        this.consumer = c;
    }

    @Override
    public void run() {

        URLConnection conn; //Connection 
        BufferedReader in = null; //Reader to read XML from URL
        BufferedWriter xmlFileWrite = null; //Writer to write on to the file
        int count = 0; //Count to maintain connection retries
        boolean flag = false; //Flag to keep track of connection fails

        /*
         * Buffer to read the input data
         */
        char[] buffer = new char[4 * 1024];
        long start = System.currentTimeMillis();
        try {
            int c = 1;
            while (c <= 2) {
                File file = new File(usrPath + "//tweets" + c + ".xml"); // create a new file to store xml data
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        throw new IllegalStateException("Couldn't Create file" + file.toString());
                    }
                    xmlFileWrite = new BufferedWriter(new FileWriter(file, true));
                    uri = new URL("https://api.twitter.com/1/statuses/user_timeline.xml?user_id=" + Long.toString(user.getUserID()) + "&include_rts=true&count=100&page=" + c);
                    conn = uri.openConnection();
                    consumer.sign(conn); // fire the url
					/**
					 * Attemp to read from url
					 */
                    while (count < 10) {
                        try {
                            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            flag = false;
                            break;
                        } catch (Exception ex) {
                            count++;
                            flag = true;
                            //       ex.printStackTrace();
                            Thread.sleep(5000);
                        }
                    }

                    if (flag) {
                        xmlFileWrite.write("<error>Cannot read user tweets</error>");
                        throw new IOException("Cannot read user tweets!! Aborting operation for user :" + Long.toString(user.getUserID()));
                    }

                    int bytesRead = 0;
					// write to the xml
                    while ((bytesRead = in.read(buffer)) != -1) {
                        xmlFileWrite.write(buffer, 0, bytesRead);
                        Thread.sleep(500);
                    }

                    in.close();
                    xmlFileWrite.close();
                }
                c++;
            }
        } catch (Exception ex) {
			//#TODO : Record Exception in the log file
            System.out.println("inside exception for get tweets  :-" + ex.getMessage());
        } finally {
            long finish = System.currentTimeMillis();
            System.out.println("Tweets for user " + Long.toString(user.getUserID()) + " fetched in " + (finish - start) / 1000 + " seconds");

        }
    }
    /**
     * private variables
     */
    private User user;
    private URL uri;
    private String usrPath;
    private OAuthConsumer consumer;
}


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import oauth.signpost.OAuthConsumer;


/**
 * this class gets the friend list of the user
 * @author Transcendence
 */
public class GetFriendList extends Thread{

    /**
     * constructor
     * @param u
     * @throws MalformedURLException 
     */
    public GetFriendList(User u, String path,OAuthConsumer c) throws MalformedURLException {
        usr = u;
        uri = new URL("https://api.twitter.com/1/friends/ids.xml?user_id=" + Long.toString(usr.getUserID()));
        usrPath = path;
        consumer = c;
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

        try {
            long start = System.currentTimeMillis();
            File file = new File(usrPath + "//friends.xml");
            if (!file.exists()) {
                
                if (!file.createNewFile()) {
                    throw new IllegalStateException("Couldn't Create file" + file.toString());
                }

                xmlFileWrite = new BufferedWriter(new FileWriter(file, true));
                conn = uri.openConnection();
                consumer.sign(conn);
                //attempt to read from the URL page
                while (count < 10) {
                    try {
                        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        flag = false;
                        break;
                    } catch (Exception ex) {
                        count++;
                        flag = true;
                        Thread.sleep(5000);
                    }
                }

                if (flag) {
                    xmlFileWrite.write("<error>Cannot read user friends</error>");
                    throw new IOException("Cannot read user friends!! Aborting operation");
                }

                int bytesRead = 0;
                // write the data that was read from the URL to XML
                while ((bytesRead = in.read(buffer)) != -1) {
                    xmlFileWrite.write(buffer, 0, bytesRead);
                    Thread.sleep(500);
                }
                long finish = System.currentTimeMillis();
                in.close();
                xmlFileWrite.close();
                System.out.println("Friend List for user " + Long.toString(usr.getUserID()) + " fetched in " + (finish - start)/1000 +" seconds");

                /**
                 * add the users friends to the queue used to keep track of unvisited
                 * users for BFS.
                 * #TODO: take the hop information as input from the user 
                 * and substitute that for 1 in the if condition 
                 */
                if (usr.getHopInfo() < 1) {
                    System.out.println("trying to add other users");
                    ParseXML pXml = new ParseXML(file);
                    
                    // parse users from friends.xml
                    ArrayList<Long> IDs = pXml.getIDs();
                    System.out.println("size of IDs : - " + IDs.size());
                   
                    // add users to the xml
                    for (int i = 0; i < IDs.size(); i++) {
//                        System.out.println("i : " + i);
                        User u = new User (IDs.get(i), usr.getHopInfo()+1);
                        UserQueue.enQueue(u);  
                    }
                }
            }
        } catch (Exception ex) {
        	//#TODO: Write exception to exception log
            System.out.println("cannot read friends for the user :" + Long.toString(usr.getUserID()));
        } finally {
        }
    }
    /**
     * private variables
     */
    private User usr;
    private URL uri;
    private String usrPath;
    private OAuthConsumer consumer;
}

/**
 * this class defines a user
 * @author Transcendence
 */

public class User {
    
    /**
     * constructor
     * @param ID: User ID
     * @param hi: How far it is from the subject 
     */
    
    public User(long ID, int hi){
       this.userID = ID;
       this.hopInfo = hi;
    }
    
    /**
     * getter functions
     * @return 
     */
    
    public long getUserID(){
        return this.userID;
    }
    
    public int getHopInfo(){
        return this.hopInfo;
    }
    /**
     * private variables
     */
    private final long userID;
    private final int hopInfo; // How far in the network is this user from the network
}

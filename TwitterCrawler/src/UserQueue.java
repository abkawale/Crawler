
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * this class implements a queue to store users
 * @author Transcendence
 */
public class UserQueue {
   
    public synchronized static void enQueue(User u){
		list.add(u);
	}
	
	public synchronized static void enQueueMany(User[] u){
		int size = u.length;
		for(int i=0;i<size;i++){
			enQueue(u[i]);
		}
	}
	
	public synchronized static User deQueue(){
		return list.poll();
	}
	
	public synchronized static boolean hasUsers(){
		return !list.isEmpty();
	}
        public static int size(){
            return list.size();
        }
    /**
     * private variables
     */
    private static ConcurrentLinkedQueue<User> list = new ConcurrentLinkedQueue<User>();
}

/**
 * This class is to Parse the friend list XML and return an ArrayList of user IDs from it
 */
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseXML {
    /**
     * constructor
     * @param f 
     */
    public ParseXML(File f) {
        this.file = f;
    }

    public ArrayList<Long> getIDs() throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.file));
			String re = "(<id>)((?:[0-9][0-9]*))(</id>)";
			Pattern p = Pattern.compile(re);
            String str = null;
            String ID = null;
            ArrayList<Long> IDs = new ArrayList<Long>();
            while ((str = br.readLine()) != null) {
                if ((ID = regularExpTester(str,p)) != null) {
                    IDs.add(Long.parseLong(ID));
                }
            }
            br.close();
            return IDs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * returns the string if it is of type <id>id</id>
     * @param regExp
     * @return 
     * <id>12345</id>
     */
    public String regularExpTester(String regExp, Pattern p) {
        Matcher m = p.matcher(regExp);
        if (m.find()) {
            String var1 = m.group(2);
            return var1;
        }
        return null;
    }
    /**
     * private variables
     */
    private File file;
}

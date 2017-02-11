import java.security.*;
import java.io.*;
/**
 * Created by tommy on 1/16/17.
 */
public class ComputeSHA {
    public static void main(String[] args) throws Exception {
        try {
            
            //FileInputStream fis = new FileInputStream("/Users/tommy/IdeaProjects/ComputeSHA/out/production/ComputeSHA/sample-input.txt");
            // Reading the file into the data byte-array, then update the data into the
            // digest buffer.
			File file = new File(args[0]);
            byte[] data = new byte[(int)file.length()];

			// Initialize the digest object and the fileinputstream;
            MessageDigest md = MessageDigest.getInstance("SHA1");
            FileInputStream fis = new FileInputStream(file);
			
            fis.read(data);
			fis.close();
			
			md.update(data);

            // Call the digest hash function.
            byte[] mdbytes = md.digest();

            // Convert the byte to hex format.
            StringBuffer hexString = new StringBuffer(mdbytes.length * 2);
            for(byte b:mdbytes) {
                hexString.append(String.format("%02x", b & 0xFF));
            }
            System.out.println(hexString.toString());
        }
        catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

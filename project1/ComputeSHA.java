import java.security.*;
import java.io.*;
public class ComputeSHA {
	public static void main(String[] args) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		FileInputStream fis = new FileInputStream(args[0]);
		byte[] dataBytes = new byte[1024];
		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}
		byte[] mdbytes = md.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			hexString.append(Integer.toHexString(0x0F & (mdbytes[i] >> 4)));
			hexString.append(Integer.toHexString(0x0F & mdbytes[i]));
		}
		fis.close();
		System.out.println(hexString.toString());
		//System.out.println(Integer.toHexString(0));
	}

}
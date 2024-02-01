/*
 * The Signature code came from https://gist.github.com/ishikawa/88599?permalink_comment_id=2403543#gistcomment-2403543
 */

import java.io.File;
import java.util.Scanner;
import java.security.SignatureException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Base64HmacSha1 {
    public static void main(String... args) throws Exception {
        System.out.println("This code is used to verify the Fitbit Signature for webhook notifications.");
        String expectedValue = "Oyv+HBziS4dH/fHJ735cToXX6vs=";
        System.out.println();

        String data = null;

        File myObj = new File("notification.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            // ternary syntax for id..else; apprend myReader.nextLine()
            data = (data == null ? "" : data + "\n") + myReader.nextLine();
        }
        System.out.println(data);
        myReader.close();

        String body = data;

        String secret = "123ab4567c890d123e4567f8abcdef9a&";

        System.out.println();

        final Scanner in = new Scanner(System.in);
        System.out.print("Enter your client secret: " + secret);
        //final String secret = in.nextLine();
        System.out.println();

        String base64HmacSha1Value = Signature(body, secret);
        System.out.println("Expected value is: " + expectedValue);
        System.out.println("Base64-HMAC-SHA1 value is: " + base64HmacSha1Value);
    }

    public static String Signature (String xData, String AppKey) throws SignatureException {

        try {
            final Base64.Encoder encoder = Base64.getEncoder();
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(AppKey.getBytes("UTF-8"),"HmacSHA1");

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(xData.getBytes("UTF-8"));
			String result = encoder.encodeToString(rawHmac);
            return result;

        } catch (Exception e) {
            
            throw new SignatureException("Failed to generate HMAC: " + e.getMessage());
        
        }

    }
}

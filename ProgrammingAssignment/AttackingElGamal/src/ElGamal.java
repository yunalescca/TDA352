import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;


public class ElGamal {

    public static String decodeMessage(BigInteger m) {
        return new String(m.toByteArray());
    }

    public static void main(String[] arg) {
        String filename = "input.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            BigInteger p = new BigInteger(br.readLine().split("=")[1]);
            BigInteger g = new BigInteger(br.readLine().split("=")[1]);
            BigInteger y = new BigInteger(br.readLine().split("=")[1]);
            String line = br.readLine().split("=")[1];
            String date = line.split(" ")[0];
            String time = line.split(" ")[1];
            int year  = Integer.parseInt(date.split("-")[0]);
            int month = Integer.parseInt(date.split("-")[1]);
            int day   = Integer.parseInt(date.split("-")[2]);
            int hour   = Integer.parseInt(time.split(":")[0]);
            int minute = Integer.parseInt(time.split(":")[1]);
            int second = Integer.parseInt(time.split(":")[2]);
            BigInteger c1 = new BigInteger(br.readLine().split("=")[1]);
            BigInteger c2 = new BigInteger(br.readLine().split("=")[1]);
            br.close();
            BigInteger m = recoverSecret(p, g, y, year, month, day, hour, minute,
                    second, c1, c2);
            System.out.println("Recovered message: " + m);
            System.out.println("Decoded text: " + decodeMessage(m));
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
    }

    public static BigInteger recoverSecret(BigInteger p, BigInteger g,
                                           BigInteger y, int year, int month, int day, int hour, int minute,
                                           int second, BigInteger c1, BigInteger c2) {

        for(int i=0; i<1000; i++){
            BigInteger r = createRandomNumber(year, month, day, hour, minute, second, i);
            BigInteger tmpC = g.modPow(r, p);

            if(tmpC.equals(c1)){
                //now we know r :)
                BigInteger k = y.modPow(r,p);
                BigInteger m = ((k.modInverse(p)).multiply(c2)).mod(p);
                return m;
            }
        }
        return BigInteger.ZERO;
    }

    public static BigInteger createRandomNumber(int year, int month, int day, int hour, int minute,
                                                int second, int millisecond){
        BigInteger bigYear = BigInteger.valueOf(year).multiply(BigInteger.TEN.pow(10));
        BigInteger bigMonth = BigInteger.valueOf(month).multiply(BigInteger.TEN.pow(8));
        BigInteger bigDay = BigInteger.valueOf(day).multiply(BigInteger.TEN.pow(6));
        BigInteger bigHour = BigInteger.valueOf(hour).multiply(BigInteger.TEN.pow(4));
        BigInteger bigMinute = BigInteger.valueOf(minute).multiply(BigInteger.TEN.pow(2));
        BigInteger bigSecond = BigInteger.valueOf(second);
        BigInteger bigMillisecond = BigInteger.valueOf(millisecond);

        return bigYear.add(bigMonth.add(bigDay.add(bigHour.add(bigMinute.add(bigSecond.add(bigMillisecond))))));
    }

}
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class AttackRSA {

    public static void main(String[] args) {
        String filename = "input.txt";
        BigInteger[] N = new BigInteger[3];
        BigInteger[] e = new BigInteger[3];
        BigInteger[] c = new BigInteger[3];
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            for (int i = 0; i < 3; i++) {
                String line = br.readLine();
                String[] elem = line.split(",");
                N[i] = new BigInteger(elem[0].split("=")[1]);
                e[i] = new BigInteger(elem[1].split("=")[1]);
                c[i] = new BigInteger(elem[2].split("=")[1]);
            }
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
        }
        BigInteger m = recoverMessage(N, e, c);
        System.out.println("Recovered message: " + m);
        System.out.println("Decoded text: " + decodeMessage(m));
    }

    public static String decodeMessage(BigInteger m) {
        return new String(m.toByteArray());
    }

    /**
     * Tries to recover the message based on the three intercepted cipher texts.
     * In each array the same index refers to same receiver. I.e. receiver 0 has
     * modulus N[0], public key d[0] and received message c[0], etc.
     *
     * @param N
     *            The modulus of each receiver.
     * @param e
     *            The public key of each receiver (should all be 3).
     * @param c
     *            The cipher text received by each receiver.
     * @return The same message that was sent to each receiver.
     */
    private static BigInteger recoverMessage(BigInteger[] N, BigInteger[] e,
                                             BigInteger[] c) {

        BigInteger m1 = N[1].multiply(N[2]);
        BigInteger m2 = N[0].multiply(N[2]);
        BigInteger m3 = N[0].multiply(N[1]);

        BigInteger a1 = EEA.EEA(m1, N[0])[1].mod(N[0]);
        BigInteger a2 = EEA.EEA(m2, N[1])[1].mod(N[1]);
        BigInteger a3 = EEA.EEA(m3, N[2])[1].mod(N[2]);

        BigInteger t1 = c[0].multiply(m1).multiply(a1);
        BigInteger t2 = c[1].multiply(m2).multiply(a2);
        BigInteger t3 = c[2].multiply(m3).multiply(a3);

        BigInteger x = t1.add(t2.add(t3)).mod(N[0].multiply(N[1].multiply(N[2])));

        x = CubeRoot.cbrt(x);

        return x;
    }

}
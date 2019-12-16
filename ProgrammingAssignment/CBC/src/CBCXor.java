import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public class CBCXor {

    public static void main(String[] args) {
        String filename = "/Users/josefinulfenborg/Documents/Chalmers/Cryptography/TDA352/ProgrammingAssignment/CBC/src/input.txt";
        byte[] first_block = null;
        byte[] encrypted = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            first_block = br.readLine().getBytes();
            encrypted = DatatypeConverter.parseHexBinary(br.readLine());
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
        String m = recoverMessage(first_block, encrypted);
        System.out.println("Recovered message: " + m);
    }

    /**
     * Recover the encrypted message (CBC encrypted with XOR, block size = 12).
     *
     * @param first_block
     *            We know that this is the value of the first block of plain
     *            text.
     * @param encrypted
     *            The encrypted text, of the form IV | C0 | C1 | ... where each
     *            block is 12 bytes long.
     */
    private static String recoverMessage(byte[] first_block, byte[] encrypted) {
        byte[] decrypted = new byte[encrypted.length];
        byte[][] blocks = new byte[encrypted.length / 12][12];

        for(int i = 0; i < encrypted.length; i += 12) {
            for(int j = 0; j < 12; j++) {
                blocks[i / 12][j] = encrypted[i + j];
            }
        }

        byte[] k = new byte[12];
        for (int i = 0; i < 12; i++) {
            k[i] = (byte) (blocks[1][i] ^ first_block[i] ^ blocks[0][i]);
            decrypted[i] = first_block[i];
        }

        for(int i = 1; i < blocks.length; i++) {
            for(int j = 0; j < 12; j++) {
                decrypted[i * 12 + j] = (byte) (blocks[i][j] ^ k[j] ^ encrypted[(i - 1) * 12 + j]);
            }
        }

        return new String(decrypted);
    }
}
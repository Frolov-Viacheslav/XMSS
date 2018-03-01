package MerkleTree;
import Config.*;
public class SignatureVerification {

    WOTS_PLUS.P_SignatureVerification sv = new WOTS_PLUS.P_SignatureVerification();
    WOTS_PLUS.P_KeyPairGeneration kpg = new WOTS_PLUS.P_KeyPairGeneration();

    public boolean allSignatureVerify(Integer countLayer, String MultiSIGNATURE, Integer s, Integer w, String realRoot, String SIGNATURE, String Message, String Y){
        boolean verify = false;
        if(oneTimeSignatureVerify(SIGNATURE, Message, s, w, Y, MultiSIGNATURE) == false) {
            System.out.println("OneTimeSignature is not valid");
            return verify;
        }
        verify = merkleKeyVerify(Y, countLayer, MultiSIGNATURE, s, realRoot, w);
        return  verify;
    }

    public boolean oneTimeSignatureVerify(String SIGNATURE, String Message, Integer s, Integer w, String Y, String MultiSIGNATURE){
        String r = getR(MultiSIGNATURE, s, w);
        boolean equalSignature;
        equalSignature = sv.verifySignature(SIGNATURE, Message, s, w, Y, r);
        return  equalSignature;
    }

    public boolean merkleKeyVerify(String key, Integer countLayer, String SIGNATURE, Integer s, String realRoot, Integer w){
        String authPath = getAuthPath(SIGNATURE, s, countLayer, w);
        String authPathBit = getAuthPathBit(SIGNATURE, s, countLayer, w);
        String bitMask = getBitMask(SIGNATURE, s, countLayer, w);
        boolean verify = false;
        String root = key;
        for(int i = 0; i < countLayer; i++) {
            String temp = authPath;
            temp = temp.substring(i * s / 4, i * s / 4 + s / 4); // нахождение подстроки с длиной в s символ
            if(temp.compareTo(root) != 0) {
                if(authPathBit.substring(i, i + 1).compareTo("0") == 0) {
                    root = kpg.xor(root, bitMask.substring(2*i*s/4, 2*i*s/4 +  s/4), s/4);
                    temp = kpg.xor(temp, bitMask.substring((2*i+1)*s/4, (2*i+1)*s/4 + s/4), s/4);
                    root += temp;
                }
                else{
                    temp = kpg.xor(temp, bitMask.substring(2*i*s/4, 2*i*s/4 +  s/4), s/4);
                    root = kpg.xor(root, bitMask.substring((2*i+1)*s/4, (2*i+1)*s/4 + s/4), s/4);
                    temp += root;
                    root = temp;
                }
                root = MD5HEX.md5Custom(root);
            }
        }
        if(root.compareTo(realRoot) == 0)
            verify = true;
        System.out.println("Root = " + realRoot);
        System.out.println("Root`= " + root);
        return  verify;
    }

    public String getAuthPath(String SIGNATURE, Integer s, Integer countLayer, Integer w){
        String authPath;
        authPath = SIGNATURE.substring(6 + (w - 1) * s + s/4 + s * WOTS_PLUS.P_KeyPairGeneration.l, 6 + (w - 1) * s + (s/4 + s * WOTS_PLUS.P_KeyPairGeneration.l) + (s/4 * (countLayer)));
        System.out.println("Authentification path = " + authPath);
        return authPath;
    }

    public  String getAuthPathBit(String SIGNATURE, Integer s, Integer countLayer, Integer w){
        String authPathBit;
        authPathBit = SIGNATURE.substring(6 + (w - 1) * s + s/4 + s * WOTS_PLUS.P_KeyPairGeneration.l + (s/4 * (countLayer)), 6 + (w - 1) * s + (s/4 + s * WOTS_PLUS.P_KeyPairGeneration.l) + (s/4 * (countLayer)) + (countLayer));
        //System.out.println("Bit: " + authPathBit);
        return authPathBit;
    }

    public String getR(String SIGNATURE, Integer s, Integer w){
        String r =  SIGNATURE.substring(6 + s/4, 6 + s/4 + (w - 1) * s);
        //System.out.println("R: " + r);
        return  r;
    }

    public String getBitMask(String SIGNATURE, Integer s, Integer countLayer, Integer w){
        String bitMask =  SIGNATURE.substring(6 + s/4 + (w - 1) * s + s * WOTS_PLUS.P_KeyPairGeneration.l + (s/4 * (countLayer + 1)) + countLayer, 6 + s/4 + (w - 1) * s + s * WOTS_PLUS.P_KeyPairGeneration.l + (s/4 * (3 * countLayer + 1)) + countLayer);
        //System.out.println("bitMask = " + bitMask);
        return  bitMask;
    }
}

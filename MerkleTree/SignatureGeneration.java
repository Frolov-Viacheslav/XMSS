package MerkleTree;

public class SignatureGeneration {
    WOTS_PLUS.P_SignatureGeneration sg = new WOTS_PLUS.P_SignatureGeneration();
    public static String OneTimeSignature = "";
    public  String authPathBit = "";

    public String authPathCalculate(String [][] tree, Integer countLayer, Integer keyIndex){
        keyIndex--;
        authPathBit = "";
        String authPath = "";
        for(int i = 0; i < countLayer; i++) {
            if(keyIndex % 2 == 0){
                keyIndex ++;
                authPathBit += 0;
            }
            else {
                keyIndex --;
                authPathBit += 1;
            }
            if(tree[i][keyIndex] != null)
                authPath += tree[i][keyIndex];
            else
                authPath += tree[i][keyIndex-1];
            keyIndex /= 2;
        }
        return authPath;
    }


    public String oneTimeSignatureGeneration(String Message, Integer s, Integer w, String X, String r) {
        sg.generateSignature(Message, s, w, X, r);
        OneTimeSignature = sg.SIGNATURE;
        return sg.SIGNATURE;
    }

    public String convertBitMaskToString(){
        String bitMaskString = "";
        for (int i = 0; i < PublicKeyGeneration.bitMask.length; i++) {
           bitMaskString += PublicKeyGeneration.bitMask[i];
        }
        return bitMaskString;
    }

    public String keyIndexToString(Integer keyIndex){
        String keyIndexString = "";
        for(int i = (int) Math.ceil(Math.log10(keyIndex + 0.5)); i < 6; i++){
            keyIndexString += "0";
        }
        keyIndexString += keyIndex;
        return keyIndexString;
    }

    public String SignatureGeneration(Integer keyIndex, String key, String Message, Integer s, Integer w, String [][] tree, Integer N, Integer countLayer, String root, String X, String r){
        String SIGNATURE = keyIndexToString(keyIndex) +  key + r + oneTimeSignatureGeneration(Message, s, w, X, r) + authPathCalculate(tree, countLayer, keyIndex) + authPathBit + root + convertBitMaskToString();
        return SIGNATURE;// index(6) + oneTimeY(s/4) + oneTimeBitMask((w-1)*s) + oneTimeSig(l*s) + authPath(countLayer*s/4) + authPathBit(countLayer) + root(s/4) + bitMask(2*countLayer*s/4)
    }
}

package MerkleTree;
import Config.*;

public class PublicKeyGeneration {
   PRG prg = new PRG();
   WOTS_PLUS.P_KeyPairGeneration kpg = new WOTS_PLUS.P_KeyPairGeneration();

    public static String [][] keysArray;

    public String [][] creationKeysArray(Integer s, Integer w, Integer N){
        keysArray = new String[3][N];
        for(int i = 0; i < N; i++) {
            kpg.calculateSK(s, w);
            kpg.calculatePK(s, w);
            keysArray[0][i] = kpg.X;
            keysArray[1][i] = MD5HEX.md5Custom(kpg.Y);
            keysArray[2][i] = kpg.r;
        }
        return keysArray;
    }

    public static String root;
    public static String[][] tree;
    public static int countLayer;
    public static String[] bitMask;

    public void treeBuilding(String [][] keysArray, Integer N, Integer s){ // Tree building with L-Tree
        countLayer = (int)Math.ceil(Binarylog.binlog((double) N));
        if(keysArray[1].length % 2 == 0)
            tree = new String[countLayer + 1][N];
        else
            tree = new String[countLayer + 1][N+1];
        for(int i = 0; i < N; i++) {
            tree[0][i] = keysArray[1][i];
        }
        creationBitMask(s);
        calculateRoot(N, s);
        printTree(N);
    }

    public String calculateRoot(Integer N, Integer s){
        int k = 0;
        for (int i = 1; i <= countLayer; i++) {
            for (int j = 0; j < N / (Math.pow(2, i - 1)); j += 2) {
                if(tree[i - 1][j + 1] != null)
                    tree[i][k] = MD5HEX.md5Custom(kpg.xor(tree[i - 1][j], bitMask[(i-1) * 2], s/4)  + kpg.xor(tree[i - 1][j + 1], bitMask[(i-1) * 2 + 1], s/4));
                else
                    tree[i][k] = tree[i - 1][j];
                k++;
            }
            k = 0;
        }
        root = tree[countLayer][0];
        return root;
    }

    public  void printTree(Integer N){
        for (int i = 0; i <= countLayer; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(tree[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void creationBitMask(Integer s){
        bitMask = new String [countLayer * 2];
        for (int i = 0; i < bitMask.length; i++) {
            bitMask[i] = prg.RandomN(s/4);
        }
    }
}
package MerkleTree;
import Config.*;

public class PublicKeyGeneration {
   // PRG prg = new PRG();
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

    public String treeBilding(String [][] keysArray, Integer N){ // Tree building with L-Tree
        countLayer = (int)Math.ceil(Binarylog.binlog((double) N)) + 1;
        if(keysArray[1].length % 2 == 0)
            tree = new String[countLayer][N];
        else
            tree = new String[countLayer][N+1];
        for(int i = 0; i < N; i++) {
            tree[0][i] = keysArray[1][i];
        }
        int k = 0;
        for (int i = 1; i < countLayer; i++) {
            for (int j = 0; j < N / (Math.pow(2, i - 1)); j += 2) {
                if(tree[i - 1][j + 1] != null)
                    tree[i][k] = MD5HEX.md5Custom(tree[i - 1][j] + tree[i - 1][j + 1]);
                else
                    tree[i][k] = tree[i - 1][j];
                k++;
            }
            k = 0;
        }

        for (int i = 0; i < countLayer; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(tree[i][j] + "\t");
            }
            System.out.println();
        }

        root = tree[countLayer-1][0];
        return root;
    }
}

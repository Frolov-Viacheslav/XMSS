import MerkleTree.PublicKeyGeneration;
import MerkleTree.SignatureGeneration;
import Config.*;
import MerkleTree.SignatureVerification;

//import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        PublicKeyGeneration pkg = new PublicKeyGeneration();
        //Parametr w
        //Scanner inW = new Scanner(System.in);
        //System.out.println("Input parametr w for WOTS (power of 2):");
        //int w = Integer.parseInt(inW.nextLine());
        int w = 4;
        //Parametr N
        //Scanner nCount = new Scanner(System.in);
        //System.out.println("Input parametr N:");
        //int N = Integer.parseInt(nCount.nextLine());
        int N = 10;
        int s = 128; // Length of hash function
        //Building tree
        pkg.treeBuilding(pkg.creationKeysArray(s, w, N), N, s);
        //Signinig
        int keyIndex = 0;
        signing(w, N, args, keyIndex);

    }

    public static void signing(Integer w, Integer N, String[] args, Integer keyIndex){
        MD5Binary md5b = new MD5Binary();
        //Parametr s and message
        //Scanner inMessage = new Scanner(System.in);
        //System.out.printf("Input your message:\n");
        //String Message = inMessage.nextLine();
        String Message = "Hello world";
        Message = md5b.md5Custom(Message);
        int s = Message.length();
        //OTS key selection
        int keysResidue = N - (keyIndex + 1);

        String X = PublicKeyGeneration.keysArray[0][keyIndex];
        String Y = PublicKeyGeneration.keysArray[1][keyIndex];
        String r = PublicKeyGeneration.keysArray[2][keyIndex];
        //System.out.println("r = " + r);

        System.out.println("Current index: " + keyIndex + " Remainder of keys: " + keysResidue);
        keyIndex++;
        //Merkle
        //System.out.println("X: " + X);
        MerkleCalculate(keyIndex, Message, Y, X, s, w, N, r);
        //Exit
        if(keysResidue == 0){
            System.out.println("Key limit is reached");
            //System.out.println("Exit or create new tree? (0 - Exit, 1 - Create new tree):");
            //Scanner inExit = new Scanner(System.in);
            //int exitOrNot = Integer.parseInt(inExit.nextLine());
           // if(exitOrNot == 1){
                //Reboot();
             //   main(args);
           // }
           // else System.exit(0);
              System.exit(0);
        }
        //Scanner inExit = new Scanner(System.in);
        //System.out.println("Exit or not? (0 - Exit, 1 - Start):");
        //int exitOrNot = Integer.parseInt(inExit.nextLine());
        //if(exitOrNot == 1){
            //Reboot();
            signing(w, N, args, keyIndex);
        //}
        //else System.exit(0);
    }


    public static void MerkleCalculate(Integer keyIndex, String Message, String Y, String X, Integer s, Integer w, Integer N, String r){
        SignatureGeneration sg = new SignatureGeneration();
        SignatureVerification sv = new SignatureVerification();
        String Signature = sg.SignatureGeneration(keyIndex, Y, Message, s, w, PublicKeyGeneration.tree, N, PublicKeyGeneration.countLayer, PublicKeyGeneration.root, X, r);
        System.out.println("Signature = " + Signature);
        System.out.println("OTS PK  = " + Y);
        boolean verify = sv.allSignatureVerify(PublicKeyGeneration.countLayer, Signature, s, w, PublicKeyGeneration.root, SignatureGeneration.OneTimeSignature, Message, Y);
        if(verify)
            System.out.println("Signature is valid");
        else
            System.out.println("Signature is not valid");
    }
}

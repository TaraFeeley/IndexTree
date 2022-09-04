import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class IndexTree {
    // Nodes hold strings, an int, and a list of integers
    private IndexNode root;

    public IndexTree() {
        this.root = null;
    }

    //wrapper
    public void add(String word, int lineNumber){
        this.root = add(this.root,word,lineNumber);
    }

    // recursive method for add.
    // When you add the word to the index, if it already exists,add it to the IndexNode that
    // already exists otherwise make a new indexNode
    private IndexNode add(IndexNode root, String word, int lineNumber){
        //the tree is empty
        if(root == null) {
            root = new IndexNode(word,lineNumber);
            root.list.add(lineNumber);
            return root;
        }
        //the tree is not empty
        int compare = word.compareTo(root.word);
        if(compare==0){
            root.list.add(lineNumber);
            root.occurrences++;
        }
        else if (compare < 0) {
            root.left = add(root.left, word, lineNumber);
        } else{     //compare > 0
            root.right = add(root.right, word, lineNumber);
        }
        return root;
    }
    // returns true if the word is in the index
    public boolean contains(String word) {
        //if empty
        if (root == null) {
            return false;
        }
        int compare = word.compareTo(root.word);
        if (compare == 0) {
            return true;
        } else if (compare < 0) {
            return contains(root.left.word);
        } else {
            return contains(root.right.word);
        }

    }
    public void delete(String word){
        this.root = delete(this.root, word);
    }
    // recursive case remove the word and all the entries for the word.

    //I followed the delete method from github non-recursive method
    private IndexNode delete(IndexNode root, String word){
        if(root == null) { //tree is empty
            return null;
        }
        int compare=word.compareTo(root.word);
        //tree is not empty
        if (compare < 0) {
            root.left = delete(root.left, word); //remove from left subtree
            return root;
        } else if (compare > 0) {
            root.right = delete(root.right, word); //remove from right subtree
            return root;
        } else { //  (compare==0): want to delete root
            //root has no children: remove a leaf
            if(root.left == null && root.right == null) {
                return null;
            }
            //root has L child
            else if(root.left != null && root.right == null) {
                return root.left; //left child takes root's place, root garbage collected
            }
            //root has R child
            else if(root.left == null && root.right != null) {
                return root.right; //right child takes root's place ,root garbage collected
                //root has 2 kids. want new root to cause the least amount of rearrangement: find next inorder predecessor
            } else {//go to left, then all the way to right
                if(root.left.right == null) { //left child has no right child
                    IndexNode predecessor = root.left;
                    root.word = predecessor.word;
                    root.left = predecessor.left;
                    return root;
                }

                IndexNode parent = root.left;
                IndexNode predecessor= parent.right;
                while(predecessor.right != null) {
                    predecessor = predecessor.right;
                    parent = parent.right;
                }

                root.word = predecessor.word;
                parent.right = predecessor.left;
                return root;
            }

        }
    }
    // prints all the words in the index in inorder order
    public void printIndex(IndexNode root){
        if(root != null) {
            printIndex(root.left);
            System.out.println(root);
            printIndex(root.right);
        }
    }

    public void printIndex(){
        printIndex(this.root);
    }

    //reads file and returns index tree of words from the file.
    public static IndexTree treeFromFile(String fileName){
        IndexTree index = new IndexTree();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            int lineNumber=0;
            while(scanner.hasNextLine()){
                lineNumber++;
                String line = scanner.nextLine().trim();
//                System.out.println(line);
                line = line.replaceAll("[0-9]+","");//remove numbers
                if(line.isEmpty()){ //ignore empty lines
                    continue;
                }
                //if (lineNumber > 20) {
                //  break;
                //}
                String[] words = line.split("\\s+");
                for(String word : words){
                    //word = word.replaceAll("^[\\p{Punct}]+",""); //actually would want to keep punctuation at the beginning of word (example: 'twas)
                    word = word.toLowerCase().replaceAll("[^a-z'-]","");//remove punctuation at end of word
                    index.add(word,lineNumber); //add word to list, all lowercase
                }
            }
            scanner.close();
        } catch (FileNotFoundException e1) {
// TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return index;
    }

    public static void main(String[] args){
        IndexTree index = new IndexTree();
        String fileName = "pg100.txt";
        index = treeFromFile(fileName);

        //System.out.println("root=" + index.root.word + ", occurrences = " + index.root.occurrences);
        index.delete("zounds");
        index.printIndex();

// add all the words to the tree
// print out the index
// test removing a word from the index
    }
}
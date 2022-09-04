
import java.util.ArrayList;
import java.util.List;
public class IndexNode  {
    // The word for this entry
    String word;
    // The number of occurrences for this word
    int occurrences;
    // A list of line numbers for this word.
    List<Integer> list;
    IndexNode left;
    IndexNode right;

    // Constructors
// takes in a word and a line number, initializes the list, and sets occurrences to 1
    public IndexNode(String word, int lineNumber) {
        this.word = word;
        this.occurrences = 1;
        list = new ArrayList<Integer>();
        left = null;
        right = null;
    }

// returns the word, the number of occurrences, and the lines it appears on.
    public String toString(){
        return "word= "+ word +", occurrences= " + occurrences +", lineNumbers= "+ list.toString();
    }
}
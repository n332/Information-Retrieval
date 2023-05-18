import java.io.*;
import java.util.*;


public class InvertedIndex {
    public static void main(String[] args) throws IOException {
    	
        HashMap<String, DictEntry> index = new HashMap<>();
        
        for (int i = 1; i <= 10; i++) {
            String fileName =  i + ".txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                
                for (String word : words) {
                    word = word.toLowerCase();
                    
                    // The cases we have :
                    
                    // 1- if the term is new
                    // 		-> put it in the dictionary with new entry
                    
                    // 2- if the term is not new
                    
                    // 		A- if the term is not new but the same document
                    //			-> increment the document term frequency dtf
                    
                    //		B- if the term is not new but different documents
                    //			-> increment the number of documents that contain the term doc_freq
                    
                    if (!index.containsKey(word)) { //if the word is new -> get new entry for it
                        index.put(word, new DictEntry());
                    }
                    
                    DictEntry entry = index.get(word);	// get the entry of the current word
                    
                    entry.term_freq++;					// increment the term frequency
                    if (entry.pList == null) {			// if the term is new
                        entry.pList = new Posting(i);	//create a new node
                        entry.doc_freq++;				// increment the doc frequency (number of the docs that have that term)	
                    } else {							// if not -> add nodes
                        Posting p = entry.pList;
                        while (p.next != null && p.docId != i) {	// reach to the end of the linked list
                            p = p.next;
                        }
                        if (p.docId == i) {		// the word appeared on the same file
                            p.dtf++;			// increment the document term frequency
                        } else {
                            p.next = new Posting(i);
                            entry.doc_freq++;		// increment the number of documents that contain the term
                        }
                    }
                }
            }
            reader.close();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word: ");
        
        String word = scanner.nextLine().toLowerCase();
        
        if (index.containsKey(word)) {
            DictEntry entry = index.get(word);
            
            System.out.println("Files containing the word '" + word + "':");
            
            Posting p = entry.pList;
            
            while (p != null) {
                System.out.println("file : " + p.docId + ".txt"); 
                p = p.next;
            }
            System.out.println("With frequency:" + entry.term_freq );
        } else {
            System.out.println("The word '" + word + "' was not found in any file.");
        }
        
    }

    public static class Posting {
        public Posting next = null;
        int docId;
        int dtf = 1;

        public Posting(int docId) {
            this.docId = docId;
        }
    }

    public static class DictEntry {
        int doc_freq = 0;
        int term_freq = 0;
        Posting pList = null;
    }
}
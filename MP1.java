import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.HashMap;
import java.util.Collections;
import java.io.FileReader;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        BufferedReader br = new BufferedReader(
        		new InputStreamReader(new FileInputStream(new File(inputFileName)), "UTF-8"));
        String line = null; int lineNo = 0; int curPos = 0;
        int[] indices = toPrimitiveInts(getIndexes());
        Arrays.sort(indices);
        HashMap<String, Integer> wordToOccurrences = new HashMap<String, Integer>();
        while ((line = br.readLine()) != null)
        {       	
        	while (curPos < indices.length && indices[curPos] == lineNo)
        	{
        		StringTokenizer st = new StringTokenizer(line, delimiters);
            		while (st.hasMoreTokens())
            		{
            			String token = st.nextToken();
            			token = token.toLowerCase();
            			token = token.trim();
            			if (Arrays.asList(stopWordsArray).contains(token)) continue;
            			if (wordToOccurrences.containsKey(token))
            			{
            				wordToOccurrences.put(token, wordToOccurrences.get(token) + 1);
            			}
            			else
            			{
            				wordToOccurrences.put(token, 1);
            			}
            		}
            	
            		curPos ++;
        	}
        	
        	lineNo ++;
        }
        
        List<Map.Entry<String, Integer>> sortedList = 
        		new LinkedList<Map.Entry<String, Integer>>(wordToOccurrences.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
			{
				int valueComp = -(o1.getValue().compareTo(o2.getValue()));
				return valueComp == 0 ? o1.getKey().compareTo(o2.getKey()) : valueComp;
			}
		});
        
        ret = getMostFreqntWords(sortedList);

        return ret;
    }
    
    private int[] toPrimitiveInts(Integer[] indices)
    {
    	int[] results = new int[indices.length];
    	for (int i = 0; i < indices.length; i ++)
    	{
    		results[i] = indices[i].intValue();
    	}
    	return results;
    }
    
    private String[] getMostFreqntWords(List<Map.Entry<String, Integer>> list)
    {
    	String[] mostFreqntWords = new String[20]; 
    	int i = 0;
    	for (i = 0; i < 20; i ++)
    	{
    		mostFreqntWords[i] = "";
    	}

    	i = 0;
    	for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			mostFreqntWords[i++] = entry.getKey();
			if (i == 20) break;
		}
    	return mostFreqntWords;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}

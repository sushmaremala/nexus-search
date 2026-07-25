import java.util.*;

/**
 * High-Performance Trie Data Structure for Sub-15ms Autocomplete Suggestions.
 * Used in NexusSearch to provide instant prefix lookups.
 */
public class Trie {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
        int frequency; // Stores search frequency for ranking top suggestions
    }

    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts a word into the Trie with a given search frequency.
     */
    public void insert(String word, int frequency) {
        TrieNode current = root;
        String normalized = word.toLowerCase().trim();

        for (char ch : normalized.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.frequency = frequency;
    }

    /**
     * Searches top N autocomplete suggestions matching the given prefix.
     */
    public List<String> autocomplete(String prefix, int limit) {
        List<String> suggestions = new ArrayList<>();
        TrieNode current = root;
        String normalized = prefix.toLowerCase().trim();

        for (char ch : normalized.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return suggestions; // Return empty list if prefix doesn't exist
            }
            current = current.children.get(ch);
        }

        // DFS to collect all words starting with this prefix
        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> b.frequency - a.frequency);
        collectWords(current, new StringBuilder(normalized), pq);

        while (!pq.isEmpty() && suggestions.size() < limit) {
            suggestions.add(pq.poll().word);
        }

        return suggestions;
    }

    private void collectWords(TrieNode node, StringBuilder currentWord, PriorityQueue<Pair> pq) {
        if (node.isEndOfWord) {
            pq.add(new Pair(currentWord.toString(), node.frequency));
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            currentWord.append(entry.getKey());
            collectWords(entry.getValue(), currentWord, pq);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }

    private static class Pair {
        String word;
        int frequency;

        Pair(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("java", 1500);
        trie.insert("java spring boot", 3000);
        trie.insert("javascript", 2000);
        trie.insert("java microservices", 1200);
        trie.insert("python", 800);

        System.out.println("Autocomplete for 'jav': " + trie.autocomplete("jav", 3));
    }
}

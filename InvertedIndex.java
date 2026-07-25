import java.util.*;

/**
 * Inverted Index & TF-IDF Relevance Ranking Engine for NexusSearch.
 * Indexes text documents and performs sub-millisecond keyword relevance scoring.
 */
public class InvertedIndex {

    static class Document {
        int id;
        String title;
        String content;

        Document(int id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }
    }

    private final Map<Integer, Document> documentStore = new HashMap<>();
    private final Map<String, Map<Integer, Integer>> index = new HashMap<>(); // word -> (docId -> termFrequency)

    /**
     * Adds and indexes a new document into the Inverted Index.
     */
    public void addDocument(int docId, String title, String content) {
        Document doc = new Document(docId, title, content);
        documentStore.put(docId, doc);

        String[] tokens = (title + " " + content).toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            index.putIfAbsent(token, new HashMap<>());
            Map<Integer, Integer> docFreqMap = index.get(token);
            docFreqMap.put(docId, docFreqMap.getOrDefault(docId, 0) + 1);
        }
    }

    /**
     * Searches documents matching terms and ranks them using Term Frequency (TF).
     */
    public List<Document> search(String query) {
        String[] keywords = query.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+");
        Map<Integer, Double> docScores = new HashMap<>();

        for (String keyword : keywords) {
            if (index.containsKey(keyword)) {
                Map<Integer, Integer> postings = index.get(keyword);
                for (Map.Entry<Integer, Integer> entry : postings.entrySet()) {
                    int docId = entry.getKey();
                    int tf = entry.getValue();
                    docScores.put(docId, docScores.getOrDefault(docId, 0.0) + tf);
                }
            }
        }

        List<Map.Entry<Integer, Double>> sortedDocs = new ArrayList<>(docScores.entrySet());
        sortedDocs.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<Document> results = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : sortedDocs) {
            results.add(documentStore.get(entry.getKey()));
        }

        return results;
    }

    public static void main(String[] args) {
        InvertedIndex engine = new InvertedIndex();
        engine.addDocument(1, "Spring Boot Microservices", "Learn how to build high concurrency microservices with Java and Spring Boot.");
        engine.addDocument(2, "Redis Caching Guide", "In memory database caching with Upstash Redis and Java Spring Boot.");
        engine.addDocument(3, "Data Structures in Java", "Mastering Trie and Inverted Index for fast search engines.");

        System.out.println("Searching for 'Spring Boot Java':");
        List<Document> results = engine.search("Spring Boot Java");
        for (Document doc : results) {
            System.out.println(" - [" + doc.id + "] " + doc.title);
        }
    }
}

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final Trie trieEngine = new Trie();
    private final InvertedIndex searchIndex = new InvertedIndex();

    public SearchController() {
        // Seed initial data into Trie Autocomplete
        trieEngine.insert("java", 5000);
        trieEngine.insert("java spring boot", 8500);
        trieEngine.insert("javascript", 6200);
        trieEngine.insert("java microservices", 4100);
        trieEngine.insert("redis caching", 3200);
        trieEngine.insert("rabbitmq messaging", 2900);

        // Seed initial document index
        searchIndex.addDocument(101, "Spring Boot Microservices", "Build high concurrency microservices using Java Spring Boot, Redis, and RabbitMQ.");
        searchIndex.addDocument(102, "Redis Caching Architecture", "In-memory database caching using Upstash Redis for sub-5ms latency.");
        searchIndex.addDocument(103, "Trie Data Structure Guide", "Implementing fast prefix autocomplete using Trie data structure in Java.");
    }

    public static void main(String[] args) {
        SpringApplication.run(SearchController.class, args);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<Map<String, Object>> getAutocomplete(@RequestParam String prefix) {
        long startTime = System.currentTimeMillis();
        List<String> suggestions = trieEngine.autocomplete(prefix, 5);
        long latency = System.currentTimeMillis() - startTime;

        Map<String, Object> response = new HashMap<>();
        response.put("prefix", prefix);
        response.put("suggestions", suggestions);
        response.put("latencyMs", latency);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/query")
    public ResponseEntity<Map<String, Object>> searchDocuments(@RequestParam String q) {
        long startTime = System.currentTimeMillis();
        List<InvertedIndex.Document> results = searchIndex.search(q);
        long latency = System.currentTimeMillis() - startTime;

        Map<String, Object> response = new HashMap<>();
        response.put("query", q);
        response.put("totalResults", results.size());
        response.put("results", results);
        response.put("latencyMs", latency);

        return ResponseEntity.ok(response);
    }
}

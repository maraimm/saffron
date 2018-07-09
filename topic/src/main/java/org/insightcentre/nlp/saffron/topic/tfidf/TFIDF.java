package org.insightcentre.nlp.saffron.topic.tfidf;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import java.util.HashSet;
import java.util.List;
import org.insightcentre.nlp.saffron.data.connections.DocumentTopic;

/**
 *
 * @author John McCrae <john@mccr.ae>
 */
public class TFIDF {
    /**
     * Add the TF-IDF value to an existing set of unique document topic connections
     * @param docTopics The list of values to add TF-IDF scores to
     */
    public static void addTfidf(List<DocumentTopic> docTopics) {
        Object2DoubleMap<String> topicDf = new Object2DoubleOpenHashMap<>();
        HashSet<String> docNames = new HashSet<>();
        for(DocumentTopic dt : docTopics) {
            // We assume there are no duplicates in the DT list 
            topicDf.put(dt.topic_string, topicDf.getDouble(dt.topic_string) + 1.0);
            docNames.add(dt.document_id);
        }
        double n = docNames.size();
        for(DocumentTopic dt : docTopics) {
            dt.tfidf = (double)dt.occurrences * Math.log(n / topicDf.getDouble(dt.topic_string));
        }
    }
}
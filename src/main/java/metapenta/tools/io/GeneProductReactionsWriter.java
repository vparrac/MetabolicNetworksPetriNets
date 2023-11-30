package metapenta.tools.io;

import metapenta.model.Reaction;
import metapenta.model.dto.GeneProductReactionsDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeneProductReactionsWriter implements Writer {
    private GeneProductReactionsDTO geneProductReactions;
    private String fileName;

    public GeneProductReactionsWriter(GeneProductReactionsDTO geneProductReactions, String filename) {
        this.geneProductReactions = geneProductReactions;
        this.fileName = filename;
    }

    private JSONArray getReactionsJsonArray() {
        JSONArray reactionsJsonArray = new JSONArray();

        for(Reaction reaction: geneProductReactions.getReactions()) {
            reactionsJsonArray.add(reaction);
        }

        return reactionsJsonArray;
    }

    public void write() throws IOException {
        JSONObject reactionsObject = new JSONObject();

        JSONArray reactions = getReactionsJsonArray();
        reactionsObject.put(geneProductReactions.getGeneProduct().getName(), reactions);

        Files.write(Paths.get(this.fileName), reactionsObject.toJSONString().getBytes());
    }
}

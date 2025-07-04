package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class TextClassifierService {

    private DoccatModel model;

    @PostConstruct
    public void trainModel() throws IOException {
        String[] trainingData = {
                "spam\tBuy cheap pills now!",
                "spam\tCongratulations, you won a free iPhone!",
                "ham\tLet’s catch up for coffee tomorrow.",
                "ham\tPlease review the attached document."
        };

        ObjectStream<String> lineStream = new PlainTextByLineStream(() ->
                new java.io.ByteArrayInputStream(String.join("\n", trainingData).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, "100");
        params.put(TrainingParameters.CUTOFF_PARAM, "1");

        model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());
    }

    public String classify(String input) {
        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);

        // Tokenize the input string into String[]
        String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(input);

        double[] outcomes = categorizer.categorize(tokens);
        return categorizer.getBestCategory(outcomes).toUpperCase(); // "SPAM" or "HAM"
    }
}

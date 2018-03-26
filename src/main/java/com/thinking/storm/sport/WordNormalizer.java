package com.thinking.storm.sport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

@Slf4j
public class WordNormalizer implements IRichBolt {
    private OutputCollector collector;
    public void cleanup() {}
    /**
     * The bolt will receive the line from the
     * words file and process it to Normalize this line
     *
     * The normalize will be put the words in lower case
     * and split the line to get all words in this
     */
    public void execute(Tuple input) {
        String sentence = input.getString(0);
        String[] words = sentence.split(" ");
        for(String word : words){
            word = word.trim();
            if(!word.isEmpty()){
                word = word.toLowerCase();
//Emit the word
                List a = new ArrayList();
                a.add(input);
                collector.emit(a,new Values(word));
            }
        }
// Acknowledge the tuple
        collector.ack(input);
    }
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.collector = collector;
    }
    /**
     * The bolt will only emit the field "word"
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}

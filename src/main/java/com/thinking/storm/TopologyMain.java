package com.thinking.storm;


import com.thinking.storm.sport.WordCounter;
import com.thinking.storm.sport.WordNormalizer;
import com.thinking.storm.sport.WordReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
@Slf4j
public class TopologyMain {
    public static void main(String[] args) throws InterruptedException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader",new WordReader(), 1);
        builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
        builder.setBolt("word-counter", new WordCounter(),2).fieldsGrouping("word-normalizer",
                new Fields("word"));
        Config conf = new Config();
        conf.put("wordsFile", "src/main/resources/words.txt");
        conf.setDebug(true);
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Getting-Started-Toplogie", conf, builder.createTopology());
//        try {
//            StormSubmitter.submitTopology("Count-Word-Topology-With-Refresh-Cache", conf,
//                    builder.createTopology());
//        } catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
//            log.error("推送异常", e);
//        }
        Thread.sleep(30000);
        cluster.shutdown();
    }
}

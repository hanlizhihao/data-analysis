package com.thinking.storm.sport;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;

import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author hanlizhi
 * @time 2018/3/30
 */
public class JdbcSpout extends BaseRichSpout {
    boolean isDistributed;
    SpoutOutputCollector collector;
    public static final List<Values> rows = Lists.newArrayList(
            new Values(1,"peter",System.currentTimeMillis()),
            new Values(2,"bob",System.currentTimeMillis()),
            new Values(3,"alice",System.currentTimeMillis()));

    public JdbcSpout() {
        this(true);
    }

    public JdbcSpout(boolean isDistributed) {
        this.isDistributed = isDistributed;
    }

    public boolean isDistributed() {
        return this.isDistributed;
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        final Random rand = new Random();
        final Values row = rows.get(rand.nextInt(rows.size() - 1));
        this.collector.emit(row);
        Thread.yield();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("user_id","user_name","create_date"));
    }

}

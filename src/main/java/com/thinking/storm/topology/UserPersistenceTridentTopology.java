package com.thinking.storm.topology;

/**
 * @author hanlizhi
 * @time 2018/3/30
 */
import com.thinking.storm.sport.JdbcSpout;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.tuple.Fields;
import com.google.common.collect.Lists;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.mapper.SimpleJdbcLookupMapper;

import org.apache.storm.jdbc.trident.state.JdbcQuery;
import org.apache.storm.jdbc.trident.state.JdbcState;
import org.apache.storm.jdbc.trident.state.JdbcStateFactory;
import org.apache.storm.jdbc.trident.state.JdbcUpdater;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;

import java.sql.Types;

public class UserPersistenceTridentTopology extends AbstractUserTopology {

    public static void main(String[] args) throws Exception {
        new UserPersistenceTridentTopology().execute(args);
    }

    @Override
    public StormTopology getTopology() {
        TridentTopology topology = new TridentTopology();

        JdbcState.Options options = new JdbcState.Options()
                .withConnectionProvider(connectionProvider)
                .withMapper(this.jdbcMapper)
                .withJdbcLookupMapper(new SimpleJdbcLookupMapper(new Fields("dept_name"), Lists.newArrayList(new Column("user_id", Types.INTEGER))))
                .withTableName(TABLE_NAME)
                .withSelectQuery(SELECT_QUERY);

        JdbcStateFactory jdbcStateFactory = new JdbcStateFactory(options);

        Stream stream = topology.newStream("userSpout", new JdbcSpout());
        TridentState state = topology.newStaticState(jdbcStateFactory);
        stream = stream.stateQuery(state, new Fields("user_id","user_name","create_date"), new JdbcQuery(), new Fields("dept_name"));
        stream.partitionPersist(jdbcStateFactory, new Fields("user_id","user_name","dept_name","create_date"),  new JdbcUpdater(), new Fields());
        return topology.build();
    }
}

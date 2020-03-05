package uk.co.gresearch.nortem.enrichments.storm;

import org.adrianwalker.multilinestring.Multiline;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import uk.co.gresearch.nortem.common.zookeper.ZookeperAttributes;
import uk.co.gresearch.nortem.common.zookeper.ZookeperConnectorFactory;
import uk.co.gresearch.nortem.common.zookeper.ZookeperConnector;
import uk.co.gresearch.nortem.enrichments.storm.common.*;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class EnrichmentEvaluatorBoltTest {
    /**
     * {"a" : "tmp_string", "b" : 1, "is_alert" : "true", "source.type" : "test"}
     **/
    @Multiline
    public static String event;

    /**
     * {
     *   "rules_version": 1,
     *   "rules": [
     *     {
     *       "rule_name": "test_rule",
     *       "rule_version": 1,
     *       "rule_author": "john",
     *       "rule_description": "Test rule",
     *       "source_type": "*",
     *       "matchers": [
     *         {
     *           "matcher_type": "REGEX_MATCH",
     *           "is_negated": false,
     *           "field": "is_alert",
     *           "data": "(?i)true"
     *         }
     *       ],
     *       "table_mapping": {
     *         "table_name": "test_table",
     *         "joining_key": "${a}",
     *         "tags": [
     *           {
     *             "tag_name": "is_test_tag",
     *             "tag_value": "true"
     *           }
     *         ],
     *         "enriching_fields": [
     *           {
     *             "table_field_name": "dns_name",
     *             "event_field_name": "nortem:enrichments:dns"
     *           }
     *         ]
     *       }
     *     }
     *   ]
     *   }
     **/
    @Multiline
    public static String testRules;

    private Tuple tuple;
    private OutputCollector collector;
    private EnrichmentExceptions exceptions;
    private EnrichmentCommands commands;
    EnrichmentEvaluatorBolt enrichmentEvaluatorBolt;
    ZookeperAttributes zookeperAttributes;
    StormEnrichmentAttributes attributes;
    ZookeperConnector zookeperConnector;
    ZookeperConnectorFactory zookeperConnectorFactory;
    ArgumentCaptor<Values> argumentEmitCaptor;

    @Before
    public void setUp() throws Exception {
        zookeperAttributes = new ZookeperAttributes();
        attributes = new StormEnrichmentAttributes();
        attributes.setEnrichingRulesZookeperAttributes(zookeperAttributes);

        exceptions = new EnrichmentExceptions();
        commands = new EnrichmentCommands();
        tuple = Mockito.mock(Tuple.class);
        collector = Mockito.mock(OutputCollector.class);
        argumentEmitCaptor = ArgumentCaptor.forClass(Values.class);
        zookeperConnectorFactory = Mockito.mock(ZookeperConnectorFactory.class);


        zookeperConnector = Mockito.mock(ZookeperConnector.class);
        when(zookeperConnectorFactory.createZookeperConnector(zookeperAttributes)).thenReturn(zookeperConnector);
        when(zookeperConnector.getData()).thenReturn(testRules);

        when(tuple.getStringByField(eq(EnrichmentTuples.EVENT.toString()))).thenReturn(event);
        when(collector.emit(eq(tuple), argumentEmitCaptor.capture())).thenReturn(new ArrayList<>());

        enrichmentEvaluatorBolt = new EnrichmentEvaluatorBolt(attributes, zookeperConnectorFactory);
        enrichmentEvaluatorBolt.prepare(null, null, collector);
    }

    @Test
    public void testMatchRule() {
        enrichmentEvaluatorBolt.execute(tuple);
        Values values = argumentEmitCaptor.getValue();
        Assert.assertNotNull(values);
        Assert.assertEquals(3, values.size());
        Assert.assertTrue(values.get(0) instanceof String);
        Assert.assertTrue(values.get(1) instanceof EnrichmentCommands);
        Assert.assertTrue(values.get(2) instanceof EnrichmentExceptions);
        Assert.assertEquals(event, values.get(0));
        EnrichmentCommands commands = (EnrichmentCommands)values.get(1);
        Assert.assertEquals(1, commands.size());
        Assert.assertEquals("tmp_string", commands.get(0).getKey());
        Assert.assertEquals("tmp_string", commands.get(0).getKey());
        Assert.assertEquals(1, commands.get(0).getTags().size());
        Assert.assertEquals(1, commands.get(0).getEnrichmentFields().size());
        Assert.assertTrue(((EnrichmentExceptions)values.get(2)).isEmpty());
    }

    @Test
    public void testNoMatchRule() {
        when(tuple.getStringByField(eq(EnrichmentTuples.EVENT.toString()))).thenReturn("{}");
        enrichmentEvaluatorBolt.execute(tuple);
        Values values = argumentEmitCaptor.getValue();
        Assert.assertNotNull(values);
        Assert.assertEquals(3, values.size());
        Assert.assertTrue(values.get(0) instanceof String);
        Assert.assertTrue(values.get(1) instanceof EnrichmentCommands);
        Assert.assertTrue(values.get(2) instanceof EnrichmentExceptions);
        Assert.assertTrue(((EnrichmentCommands)values.get(1)).isEmpty());
        Assert.assertTrue(((EnrichmentExceptions)values.get(2)).isEmpty());
    }

    @Test
    public void testExceptionRule() {
        when(tuple.getStringByField(eq(EnrichmentTuples.EVENT.toString()))).thenReturn("INVALID");
        enrichmentEvaluatorBolt.execute(tuple);
        Values values = argumentEmitCaptor.getValue();
        Assert.assertNotNull(values);
        Assert.assertEquals(3, values.size());
        Assert.assertTrue(values.get(0) instanceof String);
        Assert.assertTrue(values.get(1) instanceof EnrichmentCommands);
        Assert.assertTrue(values.get(2) instanceof EnrichmentExceptions);
        Assert.assertTrue(((EnrichmentCommands)values.get(1)).isEmpty());
        Assert.assertFalse(((EnrichmentExceptions)values.get(2)).isEmpty());
        Assert.assertEquals(1, ((EnrichmentExceptions)values.get(2)).size());
        Assert.assertTrue(((EnrichmentExceptions)values.get(2)).get(0).contains("JsonParseException"));
    }
}
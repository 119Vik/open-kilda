package org.bitbucket.openkilda.wfm;

import kafka.api.OffsetRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.utils.Utils;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Common utilities for Kafka
 *
 * Reference:
 * (1) Basics - https://kafka.apache.org/quickstart
 * (2) System Tools - https://cwiki.apache.org/confluence/display/KAFKA/System+Tools
 */
public class KafkaUtils {

    public String zookeeperHost = "zookeeper.pendev:2181";
    public String kafkaHosts = "kafka.pendev:9092";
    public Long offset = OffsetRequest.EarliestTime();

    public KafkaUtils () {}
    public KafkaUtils withZookeeperHost(String zookeeperHost){
        this.zookeeperHost = zookeeperHost;
        return this;
    }
    public KafkaUtils withKafkaHosts(String kafkaHosts){
        this.kafkaHosts = kafkaHosts;
        return this;
    }
    /** @param offset either OffsetRequest.EarliestTime() or OffsetRequest.LatestTime()  */
    public KafkaUtils withOffset(Long offset){
        this.offset = offset;
        return this;
    }

    /**
     * @return a Basic Kafka Producer where both key/value are strings.
     */
    public KafkaProducer<String, String> createStringsProducer(){
        Properties kprops = new Properties();
        kprops.put("bootstrap.servers", kafkaHosts);
        kprops.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kprops.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kprops.put("request.required.acks", "1");
        return createStringsProducer(kprops);
    }

    /**
     * @param kprops the properties to use to build the KafkaProducer
     */
    public KafkaProducer<String, String> createStringsProducer(Properties kprops){
        return new KafkaProducer<>(kprops);
    }

    /**
     * @return The list of messages from the topic.
     */
    public List<String> getMessagesFromTopic(String topic){
        List<String> results = new ArrayList<>();

//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        props.put("group.id", "kilda.consumer."+topic);
//        props.put("key.deserializer", StringDeserializer.class.getName());
//        props.put("value.deserializer", StringDeserializer.class.getName());
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Arrays.asList(topic));


        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "15000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        topic = "speaker.info.switch.updown";
        consumer.subscribe(Arrays.asList(topic));

        System.out.println("");
        for (int i = 0; i < 10; i++) {
            ConsumerRecords<String, String> records = consumer.poll(500);
            System.out.println(".");
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
                System.out.print("$");
                results.add(record.value());
            }
            Utils.sleep(1000);
        }
        System.out.println("");
        consumer.close();

        return results;
    }


    /**
     * Creates a basic Kafka Spout.
     *
     * @param topic the topic to listen on
     * @return a KafkaSpout that can be used in a topology
     */
    public KafkaSpout createKafkaSpout(String topic){
        String spoutID = topic + "_" + System.currentTimeMillis();
        String zkRoot = "/" + topic; // used to store offset information.
        ZkHosts hosts = new ZkHosts(zookeeperHost);
        SpoutConfig cfg = new SpoutConfig(hosts, topic, zkRoot, spoutID);
        cfg.startOffsetTime = offset;
        cfg.scheme = new SchemeAsMultiScheme(new StringScheme());
        cfg.bufferSizeBytes = 1024 * 1024 * 4;
        cfg.fetchSizeBytes = 1024 * 1024 * 4;
        return new KafkaSpout(cfg);
    }

}
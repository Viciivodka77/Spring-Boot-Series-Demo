package com.sou7h.kafkareceiveserver.config;

/**
 * @author sou7h
 * @description Kafka配置类
 * @date 2022年10月25日 12:29 下午
 */
//@Configuration
//@EnableKafka
public class KafkaConfig {

    //@Bean
    //public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaListenerContainerFactory() {
    //    ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    //    factory.setConsumerFactory(consumerFactory());
    //    factory.setConcurrency(3);
    //    factory.getContainerProperties().setPollTimeout(3000);
    //    return factory;
    //}
    //
    //@Bean
    //public ConsumerFactory<Integer, String> consumerFactory() {
    //    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    //}
    //
    //@Bean
    //public Map<String, Object> consumerConfigs() {
    //    Map<String, Object> props = new HashMap<>();
    //    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    //    props.put("key.deserializer", StringDeserializer.class);
    //    props.put("value.deserializer", StringDeserializer.class);
    //    return props;
    //}


}

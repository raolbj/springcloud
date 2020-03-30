package com.rao.hello.component;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;


    /**
     * 发送消息到kafka
     */
    public RecordMetadata sendChannelMess(String topic, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic,message);
        RecordMetadata recordMetadata = null;
        try {
            recordMetadata = future.get().getRecordMetadata();
        } catch (InterruptedException| ExecutionException e) {
            e.printStackTrace();
            System.out.println("发送失败");
        }
        System.out.println("发送成功");
        System.out.println("partition:"+recordMetadata.partition());
        System.out.println("offset:"+recordMetadata.offset());
        System.out.println("topic:"+recordMetadata.topic());

        return recordMetadata;
    }

    @Scheduled(cron = "0 33 21 * * ? ")
    public void task(){
        for (int i=0;i<50;i++){
            sendChannelMess("test1","hello-"+i);
        }
    }
}

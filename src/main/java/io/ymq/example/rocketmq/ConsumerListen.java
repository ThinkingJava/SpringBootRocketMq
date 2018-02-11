package io.ymq.example.rocketmq;


import io.ymq.example.mybatis.dao.YmqOneBaseDao;
import io.ymq.example.mybatis.dao.YmqTwoBaseDao;
import io.ymq.example.mybatis.po.TestOnePo;
import io.ymq.example.mybatis.po.TestTwoPo;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by foresee on 2018/2/11.
 */
@Component
public class ConsumerListen {

    @Autowired
    private YmqOneBaseDao ymqOneBaseDao;

    @Autowired
    private YmqTwoBaseDao ymqTwoBaseDao;

    /**
     * 消费者的组名
     */
    @Value("${apache.rocketmq.consumer.PushConsumer}")
    private String consumerGroup;

    /**
     * NameServer地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @PostConstruct
    public void defaultMQPushConsumer() {

        //消费者的组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        //指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            //订阅PushTopic下Tag为push的消息
            consumer.subscribe("TopicTest", "push");

            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            //如果非第一次启动，那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    try {
                        for (MessageExt messageExt : list) {

                            System.out.println("messageExt: " + messageExt);//输出消息内容

                            String messageBody = new String(messageExt.getBody(), "utf-8");
                            if(!StringUtils.isEmpty(messageBody)){
//                                TestTwoPo temp = new TestTwoPo();
//                                temp.setId(messageBody);
                                TestTwoPo testOnePo = new TestTwoPo();
                                testOnePo.setId(messageBody);
                                testOnePo.setName("1");
                                testOnePo.setXgrq(new Date());
//                                int s=0,count=0;
//                                while(s!=1){
                                ymqOneBaseDao.update(testOnePo);
//                                    count++;

//                                }
//                                System.out.println("更新次数:"+count);
//                                Thread.sleep(100);
                            }
                            System.out.println("消费响应：Msg: " + messageExt.getMsgId() + ",msgBody: " + messageBody);//输出消息内容

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
                }

            });
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

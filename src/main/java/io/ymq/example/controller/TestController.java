package io.ymq.example.controller;

import io.ymq.example.mybatis.dao.YmqOneBaseDao;
import io.ymq.example.mybatis.dao.YmqTwoBaseDao;
import io.ymq.example.mybatis.po.TestOnePo;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * Created by foresee on 2018/2/11.
 */
@RestController
public class TestController {


    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private YmqOneBaseDao ymqOneBaseDao;

//    @Autowired
//    private YmqTwoBaseDao ymqTwoBaseDao;

    /**
     * 生产者的组名
     */
    @Value("${apache.rocketmq.producer.producerGroup}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @RequestMapping("/sendMessage")
    public void index() throws Exception {

        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);

        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);

        try {

            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可
             * 注意：切记不可以在每次发送消息时，都调用start方法
             */
            producer.start();

            for (int i = 0; i < 10; i++) {

                String messageBody = UUID.randomUUID().toString().replaceAll("-","");

                String message = new String(messageBody.getBytes(), "utf-8");

                //构建消息
                Message msg = new Message("TopicTest" /* PushTopic */, "push"/* Tag  */, "key_" + i /* Keys */, message.getBytes());

                //发送消息
                SendResult result = producer.send(msg);

                if(result.getSendStatus()== SendStatus.SEND_OK){
                    TestOnePo testOnePo = new TestOnePo();
                    testOnePo.setId(message);
                    testOnePo.setName("0");
                    testOnePo.setLrrq(new Date());
                    ymqOneBaseDao.insert(testOnePo);
                }

                System.out.println("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }

    }

}

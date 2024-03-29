package shop;

import shop.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class PolicyHandler{

    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_Ship(@Payload Ordered ordered){

        /*if(ordered.isMe()){
            System.out.println("##### listener Ship : " + ordered.toJson());
            Delivery delivery = new Delivery();
            delivery.setOrderId(ordered.getProductId());

            deliveryRepository.save(delivery);
        }*/

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_Ship(@Payload Paid paid){

        if(paid.isMe()) {


            int flag = 0;
            Iterator<Delivery> iterator = deliveryRepository.findAll().iterator();
            while (iterator.hasNext()) {

                Delivery deliveryTmp = iterator.next();
                if ((deliveryTmp.getId() == paid.getId()) && paid.getStatus().equals("ordered")) {
                    Optional<Delivery> DeliveryOptional = deliveryRepository.findById(deliveryTmp.getId());
                    Delivery delivery = DeliveryOptional.get();
                    delivery.setDeliveryStatus("ordered!");
                    deliveryRepository.save(delivery);
                    //flag=1;
                }


                //System.out.println("##### listener Ship : " + paid.toJson());
                //Delivery delivery = new Delivery();
                //delivery.setDeliveryStatus("ordered");
                //System.out.println("Delivery start");

                //deliveryRepository.save(delivery);
            }
        }
    }

}

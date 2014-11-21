/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.officinerobotiche.test;

import it.officinerobotiche.message.*;
import it.officinerobotiche.message.Jmessage.Command;
import it.officinerobotiche.message.standard.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raffaello
 */
public class SerialTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("HELLO WORLD!");

        //SerialBridge.availableSerialPorts();
        ArrayList<Jmessage> list_send = new ArrayList<Jmessage>();
        ArrayList<Jmessage> list_receive;

        Service.NameService[] values = Service.NameService.values();

//        list_send.add( new Service(Service.NameService.VERSION));
//        list_send.add(new Service(Service.NameService.NAME_BOARD));
//        list_send.add(new Service(Service.NameService.AUTHOR));
//        list_send.add(new Service(Service.NameService.DATE_CODE));
//        list_send.add(new MicroProcess(MicroProcess.TypeCommand.TIME));
        SerialMessage serial = new SerialMessage("/dev/tty.usbserial-A400g3iO");
        
//        list_receive = serial.sendSyncMessage(list_send);
//        for (Jmessage message : list_receive) {
//            try {
//                Service mess = (Service) message;
//                System.out.println(mess.getName());
//            } catch (Exception ex) {
//            }
//        }
//        list_send.clear();
        
        list_send.add(new MicroProcess(MicroProcess.TypeCommand.TIME));
        list_receive = serial.sendSyncMessage(list_send);
        for (Jmessage message : list_receive) {
            try {
                System.out.println(message.toString());
            } catch (Exception ex) {
            }
        }
        
        serial.close();
    }

}

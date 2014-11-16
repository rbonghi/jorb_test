/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.officinerobotiche.test;

import it.officinerobotiche.message.Jmessage;
import it.officinerobotiche.message.standard.Service;
import it.officinerobotiche.message.SerialMessage;
import it.officinerobotiche.serial.Packet;
import java.util.ArrayList;
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
        
        Service service = new Service(Service.Type.VERSION.getName());
        list_send.add(service);
        service = new Service(Service.Type.NAME_BOARD.getName());
        list_send.add(service);
        
        
        SerialMessage serial = new SerialMessage("/dev/tty.usbserial-A400g3iO");
        
        try {
            list_receive = serial.sendSyncMessage(list_send);
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Package p = SerialMessage.class.getPackage();
        Class c = Service.class;
        System.out.println("OK");
    }
    
}

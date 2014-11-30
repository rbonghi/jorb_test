/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.officinerobotiche.test;

import it.officinerobotiche.serial.frame.unav.UnavFrame;
import it.officinerobotiche.serial.frame.unav.PID;
import it.officinerobotiche.serial.frame.standard.Service;
import it.officinerobotiche.serial.frame.standard.StandardFrame;
import it.officinerobotiche.serial.frame.standard.MProcess;
import it.officinerobotiche.serial.frame.SerialFrame;
import it.officinerobotiche.serial.frame.AbstractFrame;
import it.officinerobotiche.serial.frame.FrameEvent;
import it.officinerobotiche.serial.frame.ParserListener;
import it.officinerobotiche.serial.frame.standard.ErrorSerial;
import it.officinerobotiche.serial.frame.standard.MProcess.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raffaello
 */
public class SerialTest implements ParserListener {

    @Override
    public void frameEvent(FrameEvent evt) {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //SerialMessage.availableSerialPorts();
        String name = "/dev/tty.usbserial-A400g3iO";
        SerialFrame port = new SerialFrame(name);

        ArrayList<AbstractFrame> list = new ArrayList<>();
        Service service = new Service(Service.NameService.VERSION);
        list.add(service);
        //list.add(new Service(Service.NameService.AUTHOR));
        list.add(new MProcess.Time());
        ErrorSerial err = new ErrorSerial();

        try {
            List<AbstractFrame> sendSyncMessage = port.sendSyncFrame(list);

            for (AbstractFrame m : sendSyncMessage) {
                decode(m);
            }

            Service sendSyncMessage1 = port.sendSyncFrame(new Service(Service.NameService.VERSION));
            decode(sendSyncMessage1);

        } catch (InterruptedException ex) {
            Logger.getLogger(SerialTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        port.close();
    }

    public static void decode(AbstractFrame m) {
        switch (m.getTypeMessage()) {
            case DEFAULT:
                defaultMessage((StandardFrame) m);
                break;
            case UNAV:
                unavMessage((UnavFrame) m);
                break;
        }
    }

    public static void unavMessage(UnavFrame message) {
        switch (message.getCommand()) {
            case PID_L:
                PID.PIDLeft mm = (PID.PIDLeft) message;
                break;
            case PID_R:
                PID.PIDRight mm2 = (PID.PIDRight) message;
                break;
        }
    }

    public static void defaultMessage(StandardFrame message) {
        switch (message.getCommand()) {
            case TIME_PROCESS:
                MProcess.Time time = (MProcess.Time) message;
                int[] processes = time.getProcesses();
                System.out.println(processes.toString());
                break;
            case FRQ_PROCESS:
//                MProcess.Frequency mm = (MProcess.Frequency) message;
//                mm.getFrequency();
                break;
            case PRIORITY_PROCESS:
//                MProcess.Priority mm2 = (MProcess.Priority) message;
                break;
            case SERVICES:
                Service s = (Service) message;
                System.out.println(s.getInformation());
                System.out.println(s.isSync());
                break;
        }
    }
}

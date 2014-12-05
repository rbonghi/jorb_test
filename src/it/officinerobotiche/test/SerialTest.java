/*
 * Copyright (C) 2014 Officine Robotiche.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Raffaello Bonghi - raffaello.bonghi@officinerobotiche.it
 */
package it.officinerobotiche.test;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

import gnu.io.CommPortIdentifier;
/**
 * Import packages for serial communication with unav board
 */
import it.officinerobotiche.serial.frame.*;
import it.officinerobotiche.serial.frame.standard.*;
import it.officinerobotiche.serial.frame.motion.*;
/**
 * Test SerialFrame serial frame communication.
 * @author Raffaello Bonghi
 */
public class SerialTest implements ParserListener {

    /**
     * Start main function to Send and receive frames messages.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /**
         * Uncomment to automatic list all serial port aviable.
         */
        //HashSet<CommPortIdentifier> availableSerialPorts = SerialFrame.availableSerialPorts();
        /**
         * Select serial port to connect.
         */
        String name = "/dev/tty.usbserial-A400g3iO";
        /**
         * Open Serial port for automatic decoding a frames.
         */
        SerialFrame port = new SerialFrame(name);
        /**
         * Create a list to add a Frames to send a unav board. It's important to
         * create a list of AbstractFramem beacuse this abstract class contains
         * all types of frames. An ArrayList is similar to static array, but you
         * can add dynamically element without ri-initializate array.
         */
        ArrayList<AbstractFrame> list = new ArrayList<>();
        /**
         * Now I've create a Service frame message with request to receive a
         * version code on board. Particular details about a type of Service
         * message are in Service documentation.
         */
        Service service = new Service(Service.NameService.VERSION);
        /**
         * Add in list the service message.
         */
        list.add(service);
        /**
         * Create another type of Frame message to send a unav board. This
         * message is for receive time process on unav board.
         */
        MProcess.Time time = new MProcess.Time();
        /**
         * Add time frame on list frame to send.
         */
        list.add(time);

        /**
         * Using try...catch condition to verify the correct receive a message.
         */
        try {
            /**
             * Using a method in SerialMessage to send a sync Frame list and
             * save frames received in a new list.
             */
            List<AbstractFrame> list_receive = port.sendSyncFrame(list);
            /**
             * Now I can read all frame with a loop. This type of loop cycle is
             * similar to: for(int i=0; i < list_receive.size(); i++)
             * decode(list_receive.get(i));
             */
            for (AbstractFrame m : list_receive) {
                /**
                 * Decode a message in the list. See at the bottom this
                 * function.
                 */
                decode(m);
            }
        } catch (InterruptedException ex) {
            /**
             * If you receive an error message, you see this error.
             */
            System.err.println("Error to receive a packet");
        }
        /**
         * This is another type of message to send and receive a single frame
         * from unav board.
         */
        try {
            /**
             * You've a same name function, but you send a single message.
             */
            Service receive = port.sendSyncFrame(service);
            /**
             * Start decoding function, for read the frame received.
             */
            decode(receive);
        } catch (InterruptedException ex) {
            System.err.println("Error to receive a packet");
        }
        /**
         * You see now another techincs to receive a frames from unav board. If
         * you working with object, it'is important that object implements a
         * "ParserListener".
         */
        SerialTest serialTest = new SerialTest();
        /**
         * Add in SerialFrame object the serialTest object listener. Now, if you
         * use the next function, you receive directly a frame in ParserEvent.
         */
        port.addParserEventListener(serialTest);
        /**
         * Send list and use ParserEvent to decode frames.
         */
        port.parseSyncFrame(list);
        /**
         * Close serial port communcation.
         */
        port.close();
    }

    /**
     * This function receive a single frame and start decode. In this function
     * you don't receive ACK, NACK messages.
     *
     * @param frame single frame decoded from serial.
     */
    @Override
    public void parserEvent(AbstractFrame frame) {
        /**
         * Start decode function.
         */
        decode(frame);
    }

    /**
     * This function find a type of message and cast in correct type.
     *
     * @param frame message received.
     */
    public static void decode(AbstractFrame frame) {
        switch (frame.getTypeMessage()) {
            /**
             * For DEFAULT messages.
             */
            case DEFAULT:
                defaultMessage((StandardFrame) frame);
                break;
            /**
             * For specific unav board.
             */
            case MOTION:
                unavMessage((MotionFrame) frame);
                break;
        }
    }

    /**
     * This function decode MOTION frame message.
     *
     * @param message
     */
    public static void unavMessage(MotionFrame message) {
        switch (message.getCommand()) {
            /**
             * This example is to decode a PID_L and PID_R frame message
             */
            case PID_L:
                PID pid_l = (PID) message;
                /**
                 * Now you've a pid_l object and you can use in your program.
                 */
                break;
            case PID_R:
                PID pid_r = (PID) message;
                break;
        }
    }

    /**
     * This function decode default Messages.
     *
     * @param message
     */
    public static void defaultMessage(StandardFrame message) {
        switch (message.getCommand()) {
            /**
             * This example print values on time process frame messages.
             */
            case TIME_PROCESS:
                MProcess.Time time = (MProcess.Time) message;
                int[] processes = time.getProcesses();
                for (int i = 0; i < processes.length; i++) {
                    System.out.println("Process[" + i + "]: " + processes[i]);
                }
                break;
            case FRQ_PROCESS:
                MProcess.Frequency frequency = (MProcess.Frequency) message;
                break;
            case PRIORITY_PROCESS:
                MProcess.Priority priority = (MProcess.Priority) message;
                break;
            /**
             * Decode a Service frame message.
             */
            case SERVICES:
                Service s = (Service) message;
                /**
                 * Print information about service message received.
                 */
                System.out.println(s.getInformation());
                /**
                 * Print type of frame messages received.
                 */
                System.out.println("Is syncronus packet: " + s.isSync());
                break;
        }
    }
}

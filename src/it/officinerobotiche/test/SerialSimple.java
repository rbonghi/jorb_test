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

import it.officinerobotiche.serial.frame.SerialFrame;
import it.officinerobotiche.serial.frame.standard.*;
import it.officinerobotiche.serial.frame.unav.*;
import static it.officinerobotiche.serial.frame.standard.Service.NameService.*;

/**
 *
 * @author Raffaello Bonghi
 */
public class SerialSimple {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * Open Serial port for automatic decoding a frames.
         */
        SerialFrame port = new SerialFrame("/dev/tty.usbserial-A400g3iO");
        /**
         * You've a same name function, but you send a single message. Now I've
         * create a Service frame message with request to receive a version code
         * on board. Particular details about a type of Service message are in
         * Service documentation.
         */
        try {
            Service receive = port.sendSyncFrame(new Service(VERSION));
            /**
             * Print information about service message received.
             */
            System.out.println(receive.getInformation());
            /**
             * Receive something about PID left.
             */
            PID pid_left = port.sendSyncFrame(new PID.PIDLeft());
            
        } catch (InterruptedException ex) {
            System.err.println("Error to receive a packet");
        }
        /**
         * Close serial port communcation.
         */
        port.close();
    }

}

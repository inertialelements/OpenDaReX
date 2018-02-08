    package com.inertialelements.opendarex;
    /*
     * Copyright Cypress Semiconductor Corporation, 2014-2015 All rights reserved.
     *
     * This software, associated documentation and materials ("Software") is
     * owned by Cypress Semiconductor Corporation ("Cypress") and is
     * protected by and subject to worldwide patent protection (UnitedStates and foreign), United States copyright laws and international
     * treaty provisions. Therefore, unless otherwise specified in a separate license agreement between you and Cypress, this Software
     * must be treated like any other copyrighted material. Reproduction,
     * modification, translation, compilation, or representation of this
     * Software in any other form (e.g., paper, magnetic, optical, silicon)
     * is prohibited without Cypress's express written permission.
     *
     * Disclaimer: THIS SOFTWARE IS PROVIDED AS-IS, WITH NO WARRANTY OF ANY
     * KIND, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
     * NONINFRINGEMENT, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
     * FOR A PARTICULAR PURPOSE. Cypress reserves the right to make changes
     * to the Software without notice. Cypress does not assume any liability
     * arising out of the application or use of Software or any product or
     * circuit described in the Software. Cypress does not authorize its
     * products for use as critical components in any products where a
     * malfunction or failure may reasonably be expected to result in
     * significant injury or death ("High Risk Product"). By including
     * Cypress's product in a High Risk Product, the manufacturer of such
     * system or application assumes all risk of such use and in doing so
     * indemnifies Cypress against all liability.
     *
     * Use of this Software may be limited by and subject to the applicable
     * Cypress software license agreement.
     *
     *
     */
    /*
    * * Copyright (C) 2018 GT Silicon Pvt Ltd
     *
     * Licensed under the Creative Commons Attribution 4.0
     * International Public License (the "CCBY4.0 License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     * https://creativecommons.org/licenses/by/4.0/legalcode
     *
     * Note that the CCBY4.0 license is applicable only for the modifications
    made
     * by GT Silicon Pvt Ltd
     *
     * Modifications made by GT Silicon Pvt Ltd are within the following
    comments:
     * // BEGIN - Added by GT Silicon - BEGIN //
     * {Code included or modified by GT Silicon}
     * // END - Added by GT Silicon - END //
    *
    * */
    import android.annotation.TargetApi;
    import android.bluetooth.BluetoothGattCharacteristic;
    import android.os.Build;


    import java.util.Objects;

    /**
     * Parser class for parsing the data related to UART Profile
     */

    // BEGIN - Added by GT Silicon - BEGIN //

    public abstract class UARTParser {
        private static int[] packet_header;

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public static byte[] getSensorsData(BluetoothGattCharacteristic characteristic) {
            int i = 0;
            String TXDATA = null;
            byte[] buffer = characteristic.getValue();

            if (buffer != null && buffer.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(buffer.length);
                for (byte byteChar : buffer)
                    stringBuilder.append(String.format("%02X ", byteChar));
                 TXDATA = String.valueOf(stringBuilder.toString().trim());
            }else {
                return buffer;
            }

            if (TXDATA.startsWith("A0 34 00 D4")){ //Stepwise data
                for(int j=0;j<4;j++)
                {
                    packet_header[j]=buffer[i++]& 0xFF;
                }
                return buffer;
            }
            else if (TXDATA.startsWith("A0 22 00 C2 A0 32 00 D2")){//Stop processing
                for(int j=0;j<18;j++)
                {
                    buffer[j]= (byte) (packet_header[j++]& 0xFF);
                }
                return buffer;
            }
            else if (TXDATA.startsWith("A0 22 00 C2")){ //Stop transmitting data
                for(int j=0;j<18;j++)
                {
                    buffer[j]= (byte) (packet_header[j++]& 0xFF);
                }
                return buffer;
            }
            return  buffer;
        }
    }
    // END - Added by GT Silicon - END //
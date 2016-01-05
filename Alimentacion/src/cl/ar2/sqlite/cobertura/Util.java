/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ar2.sqlite.cobertura;

import cl.a2r.sap.model.Medicion;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Vega Brante
 */
public class Util {

    public static byte[] serializa(Object obj) {
        ObjectOutputStream os =  null;   
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bs);
            os.writeObject(obj);
            os.close();
            bytes = bs.toByteArray();

            //System.out.println("Serializa byte[] " + bytes.length);

        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
            }
        }

        return bytes;
    }

    public static Object desSerializa(byte[] bytes) {
        ObjectInputStream is = null;
        Object obj = null;

        //System.out.println("Desserializa byte[] " + bytes.length);

        try {
            ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
            is = new ObjectInputStream(bs);
            obj = (Object) is.readObject();
            is.close();
        } catch (OptionalDataException ex) {
            System.out.println("OptionalDataException");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException");
        } catch (StreamCorruptedException ex) {
            System.out.println("StreamCorruptedException");
        } catch (IOException ex) {
            System.out.println("IOException");
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
        return obj;
    }

    public static void main(String[] args) {
        Medicion med = new Medicion();
//        med.setId(1);
//        med.setFechaHora( new Date());
        med.setClickInicial(123);
        med.setClickFinal(555);

        byte[] bytes = serializa(med);

        System.out.println(new String(bytes));
    }

}

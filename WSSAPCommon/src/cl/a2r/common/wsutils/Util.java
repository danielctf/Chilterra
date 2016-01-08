/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.common.wsutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author Miguelon
 */
public class Util {

    public static String dateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date stringToDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            
        }
        return null;
    }

    public static Date sqlDateToDate( java.sql.Date fecha ) {
        if (fecha == null) {
            return null;
        }
        Date date = new Date( fecha.getTime() );
        return date;
    };

    public static java.sql.Date dateToSqlDate( Date fecha ) {
        if (fecha == null) {
            return null;
        }
        java.sql.Date date = new java.sql.Date( fecha.getTime() );
        return date;
    };

    public static Date fechaMasDias(Date fch, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( fch );
        cal.add(Calendar.DATE, dias);
        return cal.getTime();
    }

    public static Date julianToDate( Integer julian ) {

        if (julian == null || julian.intValue() == 0)
            return null;

        String fecha = julian.toString();
        int largo = fecha.length();

        String dia = fecha.substring(largo-3, largo);
        String ano = fecha.substring(0, largo-3);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, new Integer(ano).intValue()+1900);
        cal.set(Calendar.DAY_OF_YEAR, new Integer(dia).intValue());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getDate() {
        Date date = new Date();

        return stringToDate(dateToString(date, "yyyyMMdd"), "yyyyMMdd");
    }

    public static boolean compara( Date fec1, Date fec2 ) {
        if ( fec1 == null || fec2 == null ) {
            return false;
        }
        String sFec1 = dateToString(fec1, "yyyyMMdd");
        String sFec2 = dateToString(fec2, "yyyyMMdd");
        
        return sFec1.equals(sFec2);
    }

    public static int randomInt(int max,int min){
        return (int)(Math.random()*(max-min))+min;
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static void saveProperties(String name, Properties properties) {

        String pre = System.getProperty("user.home");
    	File cfg = new File( pre + "/" + name + ".properties" );

    	if( cfg.exists() ) {
            cfg.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(cfg, false);
            properties.store(fos, "Propiedades");
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            
        }

    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static Properties loadProperties( String name ) {

//      String pre = ((MyApplication)ApplicationInstance.getActive()).getRealPath();
//    	File cfg = new File( pre + "/WEB-INF/" + name + ".properties" );
        String pre = System.getProperty("user.home");

    	File cfg = new File( pre + "/" + name + ".properties" );
        Properties prop = null;

        if( cfg.exists() ) {
            prop = new Properties();
            InputStream is = null;

            try {
                is = new FileInputStream(cfg);
                prop.load(is);
                is.close();
                cfg = null;
            } catch(Exception ex) {
                ex.printStackTrace();
                
            }
        }

        return prop;
    }

    public static void delay(long sec) {
       try {
           Thread.sleep(sec*1000); // do nothing for (miliSec) miliseconds
       }
       catch(InterruptedException e) {
       }
    }

    public static String makeProper(String theString) {
        if (theString == null) {
            return null;
        }
        java.io.StringReader in = new java.io.StringReader(theString.toLowerCase());
        boolean precededBySpace = true;
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        StringBuffer properCase = new StringBuffer();
        while(true) {
            int i;
            try {
                i = in.read();
                if (i == -1)  break;
                char c = (char)i;
                if (c == ' ' || c == '"' || c == '(' || c == '.' || c == '/' || c == '\\' || c == ',') {
                    properCase.append(c);
                    precededBySpace = true;
                } else {
                    if (precededBySpace) {
                        properCase.append(Character.toUpperCase(c));
                    } else {
                        properCase.append(c);
                    }
                    precededBySpace = false;
                }
            } catch (IOException ex) {
                return theString;
            }
        }
        return properCase.toString();
    }

    public static String getFileExt(String archivo) {
        String fileExt = "";
        if ( archivo.indexOf(".") != -1)
            fileExt = archivo.substring( archivo.lastIndexOf("."));
        return fileExt;
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static void copyFile(java.io.File source, java.io.File destination ) {
        try {
                java.io.FileInputStream inStream=new java.io.FileInputStream(source);
                java.io.FileOutputStream outStream=new java.io.FileOutputStream(destination);

                int len;
                byte[] buf=new byte[2048];

                while ((len=inStream.read(buf))!=-1) {
                        outStream.write(buf,0,len);
                }
                inStream.close();
                outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static String formatNumer( String str ){
        BigDecimal n = new BigDecimal(str);
        return n.longValue()+"";
    }

    public static String getNumber(String number) {
        double value;
        String numberFormat = "###,###,###,###";
        DecimalFormat formatter = new DecimalFormat(numberFormat);
        try {
        value = Double.parseDouble(number);
        } catch (Throwable t) {
        return null;
        }
        return formatter.format(value);
    }

}

package com.bosch.tmp.integration.util;

/**
 * Constant Values referred in the SIA should be defined here.
 * @author GAA2PAL3
 */
public class ApplicationConstants {


     public static enum ResultQueryTypeForCX5{
        /*Feild CX5 of QPD9 has the codes*/
        QT;
        public String toString(){
        switch(this){
        case QT :
            return "QT";
        }
        return null;
     }
    }

    /*
     * Constant of Query Type  <QPD.9> for Result Service
    */
    public enum ResultQueryCodeForCX1{
        /*Subjective only(Questions and answers and vitals (scheduled and adhoc))*/
        SUBJ,
        /* Objective only (scheduled and adhoc vitals)*/
        OBS,
        /*Notes Only (notes like patient notes and session notes)*/
        NTE,
        /*Subjective and notes which includes (Questions and answers and vitals (scheduled and adhoc) and notes like patient notes and session notes)*/
        SUBJNTE,
        /*Objective and notes (scheduled and adhoc vitals and notes like patient notes and session notes)*/
        OBSNTE;

        public String toString(){
        switch(this){
        case SUBJ :
            return "SUBJ";
        case OBS :
            return "OBS";
        case NTE :
            return "NTE";
        case SUBJNTE :
            return "SUBJNTE";
        case OBSNTE :
            return "OBSNTE";
        }
        return null;
    }
 }









}

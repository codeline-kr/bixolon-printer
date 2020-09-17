package kr.codeline.plugins.bixolon;

import android.content.Context;

import com.bxl.config.editor.BXLConfigLoader;

import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.config.JposEntry;
import jpos.events.DirectIOEvent;
import jpos.events.DirectIOListener;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;

public class BixolonPrinter implements ErrorListener, OutputCompleteListener, StatusUpdateListener, DirectIOListener {
    // ------------------- alignment ------------------- //
    public static int ALIGNMENT_LEFT = 1;
    public static int ALIGNMENT_CENTER = 2;
    public static int ALIGNMENT_RIGHT = 4;

    // ------------------- Text attribute ------------------- //
    public static int ATTRIBUTE_NORMAL = 0;
    public static int ATTRIBUTE_FONT_A = 1;
    public static int ATTRIBUTE_FONT_B = 2;
    public static int ATTRIBUTE_FONT_C = 4;
    public static int ATTRIBUTE_BOLD = 8;
    public static int ATTRIBUTE_UNDERLINE = 16;
    public static int ATTRIBUTE_REVERSE = 32;
    public static int ATTRIBUTE_FONT_D = 64;

    private BXLConfigLoader bxlConfigLoader;
    private POSPrinter posPrinter;

    public BixolonPrinter(Context context) {
        posPrinter = new POSPrinter(context);
        posPrinter.addStatusUpdateListener(this);
        posPrinter.addErrorListener(this);
        posPrinter.addOutputCompleteListener(this);
        posPrinter.addDirectIOListener(this);

        bxlConfigLoader = new BXLConfigLoader(context);
        try {
            bxlConfigLoader.openFile();
        } catch (Exception e) {
            bxlConfigLoader.newFile();
        }
    }

    public boolean printerOpen(int portType, String logicalName, String address, boolean isAsyncMode) {
        if (setTargetDevice(portType, logicalName, address)) {
            try {
                posPrinter.open(BXLConfigLoader.PRODUCT_NAME_SRP_330II);
                posPrinter.claim(10000 * 3);
                posPrinter.setDeviceEnabled(true);
                posPrinter.setAsyncMode(isAsyncMode);
            } catch (JposException e) {
                if( e.getErrorCode()==106 ){
                    return true;
                }

                e.printStackTrace();
                try {
                    posPrinter.close();
                } catch (JposException e1) {
                    e1.printStackTrace();
                }

                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean printerClose() {
        try {
            if (posPrinter.getClaimed()) {
                posPrinter.setDeviceEnabled(false);
                posPrinter.close();
            }
        } catch (JposException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean setTargetDevice(int portType, String logicalName,  String address) {
        try {
            for (Object entry : bxlConfigLoader.getEntries()) {
                JposEntry jposEntry = (JposEntry) entry;
                if (jposEntry.getLogicalName().equals(logicalName)) {
                    bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
                }
            }

            bxlConfigLoader.addEntry(logicalName, BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER, BXLConfigLoader.PRODUCT_NAME_SRP_330II, portType, address);

            bxlConfigLoader.saveFile();
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public boolean printText(String data, int alignment, int attribute, int textSize) {
        boolean ret = true;

        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            String strOption = EscapeSequence.getString(0);

            if ((alignment & ALIGNMENT_LEFT) == ALIGNMENT_LEFT) {
                strOption += EscapeSequence.getString(4);
            }

            if ((alignment & ALIGNMENT_CENTER) == ALIGNMENT_CENTER) {
                strOption += EscapeSequence.getString(5);
            }

            if ((alignment & ALIGNMENT_RIGHT) == ALIGNMENT_RIGHT) {
                strOption += EscapeSequence.getString(6);
            }

            if ((attribute & ATTRIBUTE_FONT_A) == ATTRIBUTE_FONT_A) {
                strOption += EscapeSequence.getString(1);
            }

            if ((attribute & ATTRIBUTE_FONT_B) == ATTRIBUTE_FONT_B) {
                strOption += EscapeSequence.getString(2);
            }

            if ((attribute & ATTRIBUTE_FONT_C) == ATTRIBUTE_FONT_C) {
                strOption += EscapeSequence.getString(3);
            }

            if ((attribute & ATTRIBUTE_FONT_D) == ATTRIBUTE_FONT_D) {
                strOption += EscapeSequence.getString(33);
            }

            if ((attribute & ATTRIBUTE_BOLD) == ATTRIBUTE_BOLD) {
                strOption += EscapeSequence.getString(7);
            }

            if ((attribute & ATTRIBUTE_UNDERLINE) == ATTRIBUTE_UNDERLINE) {
                strOption += EscapeSequence.getString(9);
            }

            if ((attribute & ATTRIBUTE_REVERSE) == ATTRIBUTE_REVERSE) {
                strOption += EscapeSequence.getString(11);
            }

            switch (textSize) {
                case 1:
                    strOption += EscapeSequence.getString(17);
                    strOption += EscapeSequence.getString(26);
                    break;
                case 2:
                    strOption += EscapeSequence.getString(18);
                    strOption += EscapeSequence.getString(25);
                    break;
                case 3:
                    strOption += EscapeSequence.getString(19);
                    strOption += EscapeSequence.getString(27);
                    break;
                case 4:
                    strOption += EscapeSequence.getString(20);
                    strOption += EscapeSequence.getString(28);
                    break;
                case 5:
                    strOption += EscapeSequence.getString(21);
                    strOption += EscapeSequence.getString(29);
                    break;
                case 6:
                    strOption += EscapeSequence.getString(22);
                    strOption += EscapeSequence.getString(30);
                    break;
                case 7:
                    strOption += EscapeSequence.getString(23);
                    strOption += EscapeSequence.getString(31);
                    break;
                case 8:
                    strOption += EscapeSequence.getString(24);
                    strOption += EscapeSequence.getString(32);
                    break;
                default:
                    strOption += EscapeSequence.getString(17);
                    strOption += EscapeSequence.getString(25);
                    break;
            }

            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, strOption + data);
        } catch (JposException e) {
            e.printStackTrace();

            ret = false;
        }

        return ret;
    }

    public boolean cutPaper() {
        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            String cutPaper = EscapeSequence.ESCAPE_CHARACTERS + String.format("%dfP", 100);
            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, cutPaper);
        } catch (JposException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public boolean beginTransactionPrint() {
        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            posPrinter.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);
        } catch (JposException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public boolean endTransactionPrint() {
        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            posPrinter.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);
        } catch (JposException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    @Override
    public void directIOOccurred(DirectIOEvent directIOEvent) {
        /*Fragment fm = MainActivity.getVisibleFragment();
        if (fm != null) {
            if (fm instanceof DirectIOFragment) {
                ((DirectIOFragment) fm).setDeviceLog("DirectIO: " + directIOEvent + "(" + getBatterStatusString(directIOEvent.getData()) + ")");
                if (directIOEvent.getObject() != null) {
                    ((DirectIOFragment) fm).setDeviceLog(new String((byte[]) directIOEvent.getObject()) + "\n");
                }
            }
        }*/
    }

    @Override
    public void errorOccurred(ErrorEvent errorEvent) {
        System.out.println("Error : " + errorEvent);
    }

    @Override
    public void outputCompleteOccurred(OutputCompleteEvent outputCompleteEvent) {
        System.out.println("outputComplete : " + outputCompleteEvent.getOutputID());
    }

    @Override
    public void statusUpdateOccurred(StatusUpdateEvent statusUpdateEvent) {
        System.out.println(getSUEMessage(statusUpdateEvent.getStatus()));
    }

    private String getSUEMessage(int status) {
        switch (status) {
            case JposConst.JPOS_SUE_POWER_ONLINE:
                return "StatusUpdate : Power on";

            case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
                printerClose();
                return "StatusUpdate : Power off";

            case POSPrinterConst.PTR_SUE_COVER_OPEN:
                return "StatusUpdate : Cover Open";

            case POSPrinterConst.PTR_SUE_COVER_OK:
                return "StatusUpdate : Cover OK";

            case POSPrinterConst.PTR_SUE_BAT_LOW:
                return "StatusUpdate : Battery-Low";

            case POSPrinterConst.PTR_SUE_BAT_OK:
                return "StatusUpdate : Battery-OK";

            case POSPrinterConst.PTR_SUE_REC_EMPTY:
                return "StatusUpdate : Receipt Paper Empty";

            case POSPrinterConst.PTR_SUE_REC_NEAREMPTY:
                return "StatusUpdate : Receipt Paper Near Empty";

            case POSPrinterConst.PTR_SUE_REC_PAPEROK:
                return "StatusUpdate : Receipt Paper OK";

            case POSPrinterConst.PTR_SUE_IDLE:
                return "StatusUpdate : Printer Idle";

            case POSPrinterConst.PTR_SUE_OFF_LINE:
                return "StatusUpdate : Printer off line";

            case POSPrinterConst.PTR_SUE_ON_LINE:
                return "StatusUpdate : Printer on line";

            default:
                return "StatusUpdate : Unknown";
        }
    }
}
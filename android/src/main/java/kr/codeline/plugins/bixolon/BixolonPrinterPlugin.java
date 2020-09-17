package kr.codeline.plugins.bixolon;

import com.bxl.config.editor.BXLConfigLoader;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.bxl.config.util.BXLNetwork;

import java.util.HashSet;
import java.util.Set;

import jpos.JposException;

@NativePlugin
public class BixolonPrinterPlugin extends Plugin {
    BixolonPrinter printer = null;

    public BixolonPrinterPlugin() {

        System.out.println("BixolonPrinterPlugin 생성");
    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();

        printer = new BixolonPrinter(this.getContext());
    }

    @Override
    protected void handleOnDestroy() {
        super.handleOnDestroy();

        printer.printerClose();
    }

    @PluginMethod
    public void connect(PluginCall call) {
        String ip = call.getString("ip");

        if (ip == null || ip.isEmpty()) {
            try {
                Set<CharSequence> net = BXLNetwork.getNetworkPrinters(this.getActivity(), BXLNetwork.SEARCH_WIFI_ALWAYS);

                if (net.size() > 0) {
                    ip = "" + net.iterator().next();
                } else {
                    call.error("Could not find printer in your network.");
                }

                System.out.println("net : " + net);
            } catch (NumberFormatException | JposException e) {
                e.printStackTrace();
            }
        }

        boolean open = false;

        if (ip != null && !ip.isEmpty()) {
            open = printer.printerOpen(BXLConfigLoader.DEVICE_BUS_WIFI, "SRP-330II", "192.168.30.231", false);
        }

        if (open) {
            JSObject ret = new JSObject();
            ret.put("ip", ip);
            call.success(ret);
        } else {
            call.error("Could not connect to printer.");
        }
    }

    @PluginMethod
    public void print(PluginCall call) {
        String name = call.getString("name");
        System.out.println("name : " + name);

        printer.beginTransactionPrint();

        String data = "";
        data = "Comp: TICKET\n" +
                "Walton, KT12 3BS\n" +
                "Tel: 01932 901 155\n" +
                "123-456-789\n" +
                "VAT No. 123456789\n\n";
        printer.printText("TICKET\n\n", BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_BOLD | BixolonPrinter.ATTRIBUTE_UNDERLINE, 2);
        printer.printText(data, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_BOLD, 1);
        printer.printText("Sale:       " + "19-05-2017 16:19:43\n", BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.ATTRIBUTE_BOLD, 1);

        data = "Gate:       " + "Xcover kiosk\n" +
                "Operator:   " + "Rob\n" +
                "Order Code: " + "263036991\n";
        printer.printText(data, BixolonPrinter.ALIGNMENT_LEFT, 0, 1);
        printer.printText("Qty Price  Item     Total\n", BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.ATTRIBUTE_UNDERLINE, 1);
        printer.printText(" 1  $8.00  PARKING  $8.00\n", BixolonPrinter.ALIGNMENT_LEFT, 0, 1);

        data = "Total (inc VAT):  " + "  $8.00\n" +
                "VAT amount (20%): " + "  $1.33\n" +
                "CARD payment:     " + "  $8.00\n" +
                "Change due:       " + "  $0.00\n\n";
        printer.printText(data, BixolonPrinter.ALIGNMENT_RIGHT, BixolonPrinter.ATTRIBUTE_NORMAL, 1);

        data = "Thank you for your purchase!\n" +
                "Enjoy the show!\n" +
                "Next year visit\n" +
                "www.bixolon.com\n" +
                "to buy discounted tickets.\n\n\n\n";
        printer.printText(data, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_BOLD, 1);

        printer.cutPaper();

        printer.endTransactionPrint();

        JSObject ret = new JSObject();
        ret.put("result", "success");
        call.success(ret);
    }
}

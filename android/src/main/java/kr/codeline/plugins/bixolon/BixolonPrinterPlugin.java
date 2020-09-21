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
    boolean open = false;

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
    public void is_connected(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("connected", open);
        call.success(ret);
    }

    @PluginMethod
    public void connect(PluginCall call) {
        String ip = call.getString("ip");

        if (ip == null || ip.isEmpty()) {
            try {
                Set<CharSequence> net = BXLNetwork.getNetworkPrinters(this.getActivity(),
                        BXLNetwork.SEARCH_WIFI_ALWAYS);

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
        if (!open) {
            call.error("Printer is not connected.");
            return;
        }

        Integer division = call.getInt("division");
        String pickup_yn = call.getString("pickup_yn");
        String order_item = call.getString("order_item");
        Double item_price = call.getDouble("item_price");
        Double tax = call.getDouble("tax");
        Double total_price = call.getDouble("total_price");
        String status = call.getString("status");
        String order_datetime = call.getString("order_datetime");

        System.out.println("division : " + division);

        printer.beginTransactionPrint();

        printer.printText("ORDER\n\n", BixolonPrinter.ALIGNMENT_CENTER,
                BixolonPrinter.ATTRIBUTE_BOLD | BixolonPrinter.ATTRIBUTE_UNDERLINE, 2);

        String text = String.format("division : %d\npickup_yn : %s\norder_item : %s\nitem_price : %d\ntax : %d\ntotal_price : %d", division, pickup_yn, order_item, item_price, tax, total_price);
        printer.printText(text, BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.ATTRIBUTE_BOLD, 1);

        text = "Thank you for your purchase!\n" + "Enjoy the show!\n" + "Next year visit\n" + "www.bixolon.com\n"
                + "to buy discounted tickets.\n\n\n\n";
        printer.printText(text, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_BOLD, 1);

        printer.cutPaper();

        printer.endTransactionPrint();

        JSObject ret = new JSObject();
        ret.put("result", "success");
        call.success(ret);
    }

    @PluginMethod
    public void disconnect(PluginCall call) {
        printer.printerClose();
        open = false;

        JSObject ret = new JSObject();
        ret.put("result", "ok");
        call.success(ret);
    }
}

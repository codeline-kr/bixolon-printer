package kr.codeline.plugins.bixolon;

import android.text.TextUtils;

import com.bxl.config.editor.BXLConfigLoader;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.bxl.config.util.BXLNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @PluginMethod
    public void scan_printer(PluginCall call) {
        JSObject ret = new JSObject();

        //saveCall(call);
        //pluginRequestPermission();

        try {
            Set<CharSequence> net = BXLNetwork.getNetworkPrinters(this.getActivity(),
                    BXLNetwork.SEARCH_WIFI_ALWAYS);

            if (net.size() > 0) {
                JSArray ip_list = new JSArray(net);
                ret.put("results", ip_list);

                call.success(ret);
            } else {
                call.error("Could not find printer in your network.");
            }

            System.out.println("ret : " + ret);
        } catch (NumberFormatException | JposException e) {
            e.printStackTrace();
        }
    }

    @PluginMethod
    public void connect(PluginCall call) {
        String ip = call.getString("ip");
        String mode = call.getString("mode");

        if (ip != null && !ip.isEmpty()) {
            boolean connect = printer.printerOpen(BXLConfigLoader.DEVICE_BUS_WIFI, mode, ip, false);

            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

            if (connect) {
                JSObject ret = new JSObject();
                ret.put("connect", true);
                call.success(ret);
            } else {
                call.error("Could not connect to printer.");
            }
        }else{
            call.error("Printer IP required.");
        }
    }

    @PluginMethod
    public void print(PluginCall call) {
        String ip = call.getString("ip");
        String mode = call.getString("mode");
        Integer division = call.getInt("division");
        String pickup_yn = call.getString("pickup_yn");
        String order_item = call.getString("order_item");
        Double item_price = call.getDouble("item_price");
        Double tax = call.getDouble("tax");
        Double total_price = call.getDouble("total_price");
        Double request = call.getDouble("request");
        String status = call.getString("status");
        String order_datetime = call.getString("order_datetime");

        System.out.println("division : " + division);

        boolean open = printer.printerOpen(BXLConfigLoader.DEVICE_BUS_WIFI, mode, ip, false);

        if (open) {
            printer.beginTransactionPrint();

            // 영수증 프린터
            if( "RECEIPT".equals(mode) ){
                printer.printText("ORDER\n\n", BixolonPrinter.ALIGNMENT_CENTER,
                        BixolonPrinter.ATTRIBUTE_BOLD | BixolonPrinter.ATTRIBUTE_UNDERLINE, 2);

                String text = "영수증 프린터\ndivision : " + division + "\npickup_yn : " + pickup_yn + "\norder_item : " + order_item + "\nitem_price : " + item_price + "\ntax : " + tax + "\ntotal_price : " + total_price + "\n";
                printer.printText(text, BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.ATTRIBUTE_BOLD, 1);

                text = "Thank you for your purchase!\n" + "Enjoy the show!\n" + "Next year visit\n" + "www.bixolon.com\n"
                        + "to buy discounted tickets.\n\n\n\n";
                printer.printText(text, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_BOLD, 1);
            }
            // 주방 프린터
            else if( "KITCHEN".equals(mode)){
                printer.printText("ORDER\n\n", BixolonPrinter.ALIGNMENT_CENTER,
                        BixolonPrinter.ATTRIBUTE_BOLD | BixolonPrinter.ATTRIBUTE_UNDERLINE, 2);

                String text = "주방 프린터\ndivision : " + division + "\npickup_yn : " + pickup_yn + "\norder_item : " + order_item + "\nitem_price : " + item_price + "\ntax : " + tax + "\ntotal_price : " + total_price + "\n";
                printer.printText(text, BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.ATTRIBUTE_BOLD, 1);

                text = "Thank you for your purchase!\n" + "Enjoy the show!\n" + "Next year visit\n" + "www.bixolon.com\n"
                        + "to buy discounted tickets.\n\n\n\n";
                printer.printText(text, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_BOLD, 1);
            }

            printer.cutPaper();
            printer.endTransactionPrint();
            printer.printerClose();

            JSObject ret = new JSObject();
            ret.put("result", true);
            call.success(ret);
        } else {
            call.error("Could not connect to printer.");
        }
    }
}

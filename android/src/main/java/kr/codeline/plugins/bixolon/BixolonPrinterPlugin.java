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

    public BixolonPrinterPlugin(){

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

        if( ip==null || ip.isEmpty()){
            try {
                Set<CharSequence> net = BXLNetwork.getNetworkPrinters(this.getActivity(), BXLNetwork.SEARCH_WIFI_ALWAYS);

                if( net.size()>0 ){
                    ip = "" + net.iterator().next();
                }else{
                    call.error("Could not find printer in your network.");
                }

                System.out.println("net : " + net);
            } catch (NumberFormatException | JposException e) {
                e.printStackTrace();
            }
        }

        boolean open = printer.printerOpen(BXLConfigLoader.DEVICE_BUS_WIFI, "SRP-330II", "192.168.30.231", false);

        if( open ) {
            JSObject ret = new JSObject();
            ret.put("ip", ip);
            call.success(ret);
        }else{
            call.error("Could not connect to printer.");
        }
    }
}

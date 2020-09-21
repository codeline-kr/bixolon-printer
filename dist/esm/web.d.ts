import { WebPlugin } from '@capacitor/core';
import { BixolonPrinterPluginPlugin, PrintItem } from './definitions';
export declare class BixolonPrinterPluginWeb extends WebPlugin implements BixolonPrinterPluginPlugin {
    constructor();
    is_connected(): Promise<{
        connected: boolean;
    }>;
    connect(options: {
        ip?: string;
    }): Promise<{
        ip: string;
    }>;
    print(options: PrintItem): Promise<{
        result: string;
    }>;
    disconnect(): Promise<{
        result: string;
    }>;
}
declare const BixolonPrinterPlugin: BixolonPrinterPluginWeb;
export { BixolonPrinterPlugin };

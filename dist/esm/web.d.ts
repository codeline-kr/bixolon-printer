import { WebPlugin } from '@capacitor/core';
import { BixolonPrinterPluginPlugin, PrintItem, PrintMode } from './definitions';
export declare class BixolonPrinterPluginWeb extends WebPlugin implements BixolonPrinterPluginPlugin {
    constructor();
    scan_printer(): Promise<{
        results: string[];
    }>;
    connect(options: {
        ip?: string;
        mode: PrintMode;
    }): Promise<{
        connect: boolean;
    }>;
    print(options: PrintItem): Promise<{
        result: boolean;
    }>;
}
declare const BixolonPrinterPlugin: BixolonPrinterPluginWeb;
export { BixolonPrinterPlugin };

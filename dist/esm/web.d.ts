import { WebPlugin } from '@capacitor/core';
import { BixolonPrinterPluginPlugin } from './definitions';
export declare class BixolonPrinterPluginWeb extends WebPlugin implements BixolonPrinterPluginPlugin {
    constructor();
    connect(options: {
        ip?: string;
    }): Promise<{
        ip: string;
    }>;
    print(options: {
        name: string;
    }): Promise<{
        result: string;
    }>;
}
declare const BixolonPrinterPlugin: BixolonPrinterPluginWeb;
export { BixolonPrinterPlugin };

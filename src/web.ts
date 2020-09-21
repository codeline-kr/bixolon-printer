import { WebPlugin } from '@capacitor/core';
import { BixolonPrinterPluginPlugin, PrintItem } from './definitions';

export class BixolonPrinterPluginWeb
  extends WebPlugin
  implements BixolonPrinterPluginPlugin {
  constructor() {
    super({
      name: 'BixolonPrinterPlugin',
      platforms: ['web'],
    });
  }
  async is_connected(): Promise<{ connected: boolean }> {
    return { connected: true };
  }

  async connect(options: { ip?: string }): Promise<{ ip: string }> {
    console.log(options);
    return {
      ip: 'ip',
    };
  }

  async print(options: PrintItem): Promise<{ result: string }> {
    console.log(options);
    return { result: 'ok' };
  }

  async disconnect(): Promise<{ result: string }> {
    return {
      result: 'ok',
    };
  }
}

const BixolonPrinterPlugin = new BixolonPrinterPluginWeb();

export { BixolonPrinterPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BixolonPrinterPlugin);

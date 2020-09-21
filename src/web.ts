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
    try {
      console.log(options);

      return {
        ip: 'ip',
      };
    } catch (err) {
      throw err;
    }
  }

  async print(options: PrintItem): Promise<{ result: string }> {
    try {
      console.log(options);
      return { result: 'ok' };
    } catch (err) {
      throw err;
    }
  }

  async disconnect(): Promise<{ result: string }> {
    try {
      return { result: 'ok' };
    } catch (err) {
      throw err;
    }
  }
}

const BixolonPrinterPlugin = new BixolonPrinterPluginWeb();

export { BixolonPrinterPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BixolonPrinterPlugin);

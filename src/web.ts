import { WebPlugin } from '@capacitor/core';
import {
  BixolonPrinterPluginPlugin,
  PrintItem,
  PrintMode,
} from './definitions';

export class BixolonPrinterPluginWeb
  extends WebPlugin
  implements BixolonPrinterPluginPlugin {
  constructor() {
    super({
      name: 'BixolonPrinterPlugin',
      platforms: ['web'],
    });
  }

  async scan_printer(): Promise<{ results: string[] }> {
    try {
      return { results: [] };
    } catch (err) {
      throw err;
    }
  }

  async connect(options: {
    ip?: string;
    mode: PrintMode;
  }): Promise<{ connect: boolean }> {
    try {
      console.log(options);

      return {
        connect: false,
      };
    } catch (err) {
      throw err;
    }
  }

  async print(options: PrintItem): Promise<{ result: boolean }> {
    try {
      console.log(options);
      return { result: true };
    } catch (err) {
      throw err;
    }
  }
}

const BixolonPrinterPlugin = new BixolonPrinterPluginWeb();

export { BixolonPrinterPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BixolonPrinterPlugin);

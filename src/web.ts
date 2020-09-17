import { WebPlugin } from '@capacitor/core';
import { BixolonPrinterPluginPlugin } from './definitions';

export class BixolonPrinterPluginWeb
  extends WebPlugin
  implements BixolonPrinterPluginPlugin {
  constructor() {
    super({
      name: 'BixolonPrinterPlugin',
      platforms: ['web'],
    });
  }

  async connect(options: { ip?: string }): Promise<{ ip: string }> {
    console.log(options);
    return {
      ip: 'ip',
    };
  }

  async print(options: { name: string }): Promise<{ result: string }> {
    console.log(options);
    return { result: 'hi' };
  }
}

const BixolonPrinterPlugin = new BixolonPrinterPluginWeb();

export { BixolonPrinterPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BixolonPrinterPlugin);

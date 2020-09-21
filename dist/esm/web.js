var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { WebPlugin } from '@capacitor/core';
export class BixolonPrinterPluginWeb extends WebPlugin {
    constructor() {
        super({
            name: 'BixolonPrinterPlugin',
            platforms: ['web'],
        });
    }
    is_connected() {
        return __awaiter(this, void 0, void 0, function* () {
            return { connected: true };
        });
    }
    connect(options) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log(options);
            return {
                ip: 'ip',
            };
        });
    }
    print(options) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log(options);
            return { result: 'ok' };
        });
    }
    disconnect() {
        return __awaiter(this, void 0, void 0, function* () {
            return {
                result: 'ok',
            };
        });
    }
}
const BixolonPrinterPlugin = new BixolonPrinterPluginWeb();
export { BixolonPrinterPlugin };
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BixolonPrinterPlugin);
//# sourceMappingURL=web.js.map
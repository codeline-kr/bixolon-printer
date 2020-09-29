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
    scan_printer() {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                return { results: [] };
            }
            catch (err) {
                throw err;
            }
        });
    }
    connect(options) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                console.log(options);
                return {
                    connect: false,
                };
            }
            catch (err) {
                throw err;
            }
        });
    }
    print(options) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                console.log(options);
                return { result: true };
            }
            catch (err) {
                throw err;
            }
        });
    }
}
const BixolonPrinterPlugin = new BixolonPrinterPluginWeb();
export { BixolonPrinterPlugin };
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BixolonPrinterPlugin);
//# sourceMappingURL=web.js.map
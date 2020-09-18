var capacitorPlugin = (function (exports, core) {
    'use strict';

    var __awaiter = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class BixolonPrinterPluginWeb extends core.WebPlugin {
        constructor() {
            super({
                name: 'BixolonPrinterPlugin',
                platforms: ['web'],
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
                return { result: 'hi' };
            });
        }
    }
    const BixolonPrinterPlugin = new BixolonPrinterPluginWeb();
    core.registerWebPlugin(BixolonPrinterPlugin);

    exports.BixolonPrinterPlugin = BixolonPrinterPlugin;
    exports.BixolonPrinterPluginWeb = BixolonPrinterPluginWeb;

    return exports;

}({}, capacitorExports));
//# sourceMappingURL=plugin.js.map

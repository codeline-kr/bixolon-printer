declare module '@capacitor/core' {
  interface PluginRegistry {
    BixolonPrinterPlugin: BixolonPrinterPluginPlugin;
  }
}

export interface BixolonPrinterPluginPlugin {
  connect(options: { ip?: string }): Promise<{ ip: string }>;
  print(options: { name: string }): Promise<{ result: string }>;
}

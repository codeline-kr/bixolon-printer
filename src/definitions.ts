declare module '@capacitor/core' {
  interface PluginRegistry {
    BixolonPrinterPlugin: BixolonPrinterPluginPlugin;
  }
}

export interface PrintItem {
  division: number;
  pickup_yn?: string;
  order_item?: string;
  item_price?: number;
  tax?: number;
  total_price?: number;
  status?: string;
  order_datetime?: string;
}

export interface BixolonPrinterPluginPlugin {
  is_connected(): Promise<{ connected: boolean }>;
  connect(options: { ip?: string }): Promise<{ ip: string }>;
  print(options: PrintItem): Promise<{ result: string }>;
  disconnect(): Promise<{ result: string }>;
}

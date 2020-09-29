declare module '@capacitor/core' {
  interface PluginRegistry {
    BixolonPrinterPlugin: BixolonPrinterPluginPlugin;
  }
}

export enum PrintMode {
  RECEIPT = 'RECEIPT',
  KITCHEN = 'KITCHEN',
}

export interface PrintItem {
  printer_ip: string;
  mode: PrintMode;
  division: number;
  pickup_yn?: string;
  order_item?: string;
  item_price?: number;
  tax?: number;
  total_price?: number;
  request?: string;
  status?: string;
  order_datetime?: string;
}

export interface BixolonPrinterPluginPlugin {
  scan_printer(): Promise<{ results: string[] }>;
  connect(options: {
    ip?: string;
    mode: PrintMode;
  }): Promise<{ connect: boolean }>;
  print(options: PrintItem): Promise<{ result: boolean }>;
}

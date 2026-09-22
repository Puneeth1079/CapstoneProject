export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  sku: string;
  category: string;
  imageUrl: string;
  status: string;
  availableStock: number;
  createdAt: string;
  updatedAt: string;
}

export interface Customer {
  id: number;
  name: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  country: string;
  createdAt: string;
}

export interface OrderItem {
  id?: number;
  productId: number;
  productName?: string;
  productSku?: string;
  quantity: number;
  unitPrice?: number;
  subtotal?: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  customerId: number;
  customerName: string;
  customerEmail: string;
  totalAmount: number;
  status: string;
  items: OrderItem[];
  createdAt: string;
  updatedAt: string;
}

export interface InventoryItem {
  id: number;
  productId: number;
  productName: string;
  productSku: string;
  quantity: number;
  reservedQuantity: number;
  availableQuantity: number;
  minThreshold: number;
  lowStock: boolean;
  updatedAt: string;
}

export interface DashboardSummary {
  totalProducts: number;
  totalCustomers: number;
  totalOrders: number;
  pendingOrders: number;
  completedOrders: number;
  totalRevenue: number;
  lowStockCount: number;
  activeFaultMode: string;
  recentOrders: Order[];
}

export interface FaultModeState {
  activeMode: string;
  errorRate: number;
  latencyMs: number;
  active: boolean;
  description: string;
  updatedTime: string;
}

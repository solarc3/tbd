import { defineStore } from 'pinia';

interface CartItem {
  idProducto: number;
  nombreProducto: string;
  precio: number;
  cantidad: number;
  requiereReceta: boolean;
  image?: string;
}

export const useCartStore = defineStore('cart', {
    state: () => ({
      items: [] as CartItem[]
    }),

    getters: {
      cartItems: (state) => state.items,
      
      itemCount: (state) => 
        state.items.reduce((count, item) => count + item.cantidad, 0),
      
      totalAmount: (state) =>
        state.items.reduce((total, item) => total + (item.precio * item.cantidad), 0),
  
      itemQuantity: (state) => (itemId: number) => {
        const item = state.items.find(item => item.idProducto === itemId);
        return item ? item.cantidad : 0;
      }
    },
    
    actions: {
      addItem(product: Omit<CartItem, 'cantidad'>) {
        const existingItem = this.items.find(item => item.idProducto === product.idProducto);
        
        if (existingItem) {
          existingItem.cantidad += 1;
        } else {
          this.items.push({ ...product, cantidad: 1 });
        }
      },
      
      removeItem(itemId: number) {
        const index = this.items.findIndex(item => item.idProducto === itemId);
        if (index !== -1) {
          this.items.splice(index, 1);
        }
      },
      
      // incrementar cantidad en 1
      incrementQuantity(itemId: number) {
        const item = this.items.find(item => item.idProducto === itemId);
        if (item) {
          item.cantidad += 1;
        }
      },
      
      // decrementar cantidad en 1 pero no menos de 1
      decrementQuantity(itemId: number) {
        const item = this.items.find(item => item.idProducto === itemId);
        if (item) {
          item.cantidad = Math.max(1, item.cantidad - 1);
        }
      },
      
      clearCart() {
        this.items = [];
      }
    },
    
    persist: {
      key: 'shopping-cart',
      storage: localStorage
    }
  });
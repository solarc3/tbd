import { defineStore } from 'pinia'

export const useOpiniones = defineStore('opinionesPopup', {
  state: () => ({
    visible: false,
    productos: [] as { idProducto: number; nombre: string }[]
  }),
  actions: {
    mostrar(productos: { idProducto: number; nombre: string }[]) {
      this.productos = productos
      this.visible = true
    },
    cerrar() {
      this.visible = false
      this.productos = []
      localStorage.setItem('opinionesPopupClosed', '1')
    }
  }
})
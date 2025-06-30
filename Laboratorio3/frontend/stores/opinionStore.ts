import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOpinionStore = defineStore('opinion', () => {
    const showOpinionPopup = ref(false)
    const currentPedidoId = ref<number | null>(null)

    const mostrarOpinion = (idPedido: number) => {
        currentPedidoId.value = idPedido
        showOpinionPopup.value = true
    }

    const ocultarOpinion = () => {
        showOpinionPopup.value = false
        currentPedidoId.value = null
    }

    return {
        showOpinionPopup,
        currentPedidoId,
        mostrarOpinion,
        ocultarOpinion
    }
})
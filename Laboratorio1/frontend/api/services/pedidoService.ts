import apiClient from '@/api/axios'

interface MedioPagoUrgente {
  metodoPago: string
  cantidad: number
}

class PedidoService {
  async getMediosPagoUrgentes(): Promise<MedioPagoUrgente[]> {
    const { data } = await apiClient.get<MedioPagoUrgente[]>('/pedido/pagourgente')
    return data
  }
}

export default new PedidoService()
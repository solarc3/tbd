import apiClient from '@/api/axios'

interface MedioPagoUrgente {
  metodoPago: string
  cantidad: number
}

interface Pedido {
  idPedido: number
  monto: number
  fechaPedido: string
  esUrgente: boolean
  estadoPedido: string
  idCliente: number
  idFarmacia: number
}

class PedidoService {
  async getMediosPagoUrgentes(): Promise<MedioPagoUrgente[]> {
    const { data } = await apiClient.get<MedioPagoUrgente[]>('/pedido/pagourgente')
    return data
  }

  async getPedidosByCliente(idCliente: number): Promise<Pedido[]> {
  const { data } = await apiClient.get<Pedido[]>(`/pedido/cliente/${idCliente}`)
  return data
}
}

export default new PedidoService()
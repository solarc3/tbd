import apiClient from '@/api/axios'
import type { RegistrarPedidoRequest, PedidoCruzaZonas } from '@/api/models'

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

interface EstadoPedidoRequest {
  idPedido: number
  nuevoEstado: string
}

interface DetallePedidoRequest {
  idRepartidor: number
  metodoPago: string
}

interface DetallePedido {
  idDetalle: number
  idPedido: number
  idRepartidor: number
  metodoPago: string
  fechaEntrega: string
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

  async completarPedido(request: RegistrarPedidoRequest): Promise<void> {
    await apiClient.post("/pedido/registrarcompleto", request)  
  }

  async cambiarEstadoPedido(idPedido: number, nuevoEstado: string): Promise<boolean> {
    try {
      const request: EstadoPedidoRequest = {
        idPedido,
        nuevoEstado
      }
      const { data } = await apiClient.post('/pedido/cambiarestado', request)
      return true
    } catch (error) {
      console.error('Error al cambiar estado del pedido:', error)
      return false
    }
  }

  async entregarPedido(idPedido: number, request: DetallePedidoRequest): Promise<DetallePedido> {
    try {
      const { data } = await apiClient.post<DetallePedido>(
        `/pedido/${idPedido}/entregar`, 
        request
      )
      return data
    } catch (error) {
      console.error('Error al entregar el pedido:', error)
      throw error
    }
  }

  async getPedidosCruzaZonas(sectorAmount: number): Promise<PedidoCruzaZonas[]> {
    const { data } = await apiClient.get<PedidoCruzaZonas[]>(`/pedido/cruza-zonas-reparto/${sectorAmount}`)
    return data
  }

  async getPedidosCambiosRapidos(): Promise<any> {
    const { data } = await apiClient.get('/pedido/logs/cambios-rapidos');
    return data;
  }
}

export default new PedidoService()
import apiClient from '@/api/axios'

interface RepartidorRendimiento {
  idRepartidor: number
  nombreRepartidor: string
  pedidos: number
  puntuacionPromedio: number
  indiceRendimiento: number
}

class RepartidorService {
  async getTopRepartidores(): Promise<RepartidorRendimiento[]> {
    const { data } = await apiClient.get<RepartidorRendimiento[]>('/repartidor/rendimiento')
    return data
  }
}

export default new RepartidorService()
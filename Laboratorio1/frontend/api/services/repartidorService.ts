import apiClient from '@/api/axios'

interface RepartidorRendimiento {
  idRepartidor: number
  nombreRepartidor: string
  pedidos: number
  puntuacionPromedio: number
  indiceRendimiento: number
}

interface RepartidorInfo {
  nombre: string
  cantPaquetesEntregados: number
}

class RepartidorService {
  async getTopRepartidores(): Promise<RepartidorRendimiento[]> {
    const { data } = await apiClient.get<RepartidorRendimiento[]>('/repartidor/rendimiento')
    return data
  }
  
  async getAllRepartidores(): Promise<RepartidorInfo[]> {
  const { data } = await apiClient.get<RepartidorInfo[]>('/repartidor/info')
  return data
}
}


export default new RepartidorService()
import apiClient from '@/api/axios'

export interface FarmaciaFallada {
  nombreFarmacia: string
  cantidad: number
}

class FarmaciaService {
  async getFarmaciasFalladas(): Promise<FarmaciaFallada[]> {
    try {
      const { data } = await apiClient.get<FarmaciaFallada[]>('/farmacia/pedidofallado')
      console.log('Farmacia data received:', data)
      return data
    } catch (error) {
      console.error('Error fetching farmacias falladas:', error)
      throw error
    }
  }
}

export default new FarmaciaService()
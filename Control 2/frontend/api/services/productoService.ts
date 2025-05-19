import apiClient from "@/api/axios";
import type { Product } from "@/api/models";

class ProductoService {
	async getAllProducts(): Promise<Product[]> {
		try {
			const { data } = await apiClient.get<Product[]>("/productos/all");
			console.log("Products retrieved:", data);
			return data;
		} catch (error) {
			console.error("Error fetching products:", error);
			throw error;
		}
	}

	async getProductById(id: number): Promise<Product> {
		try {
			const { data } = await apiClient.get<Product>(`/productos/${id}`);
			return data;
		} catch (error) {
			console.error(`Error fetching product with id ${id}:`, error);
			throw error;
		}
	}

	async getProductsByCategory(category: string): Promise<Product[]> {
		try {
			const allProducts = await this.getAllProducts();
			return allProducts.filter(
				(product) =>
					product.categoria.toLowerCase() === category.toLowerCase(),
			);
		} catch (error) {
			console.error(
				`Error fetching products for category ${category}:`,
				error,
			);
			throw error;
		}
	}

	async getTopProductsByCategory(): Promise<any[]> {
		try {
			const { data } = await apiClient.get<any[]>(
				"/productos/topcategoria",
			);
			return data;
		} catch (error) {
			console.error("Error fetching top products by category:", error);
			throw error;
		}
	}
}

export default new ProductoService();

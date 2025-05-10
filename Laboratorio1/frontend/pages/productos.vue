<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { ShoppingCart } from "lucide-vue-next";
import { productoService } from "@/api/services";
import type { Product } from "@/api/models";

// State for products
const products = ref<Product[]>([]);
const loading = ref(true);
const error = ref("");

// Categories
const categories = ref([
	"dermatologia",
	"higiene",
	"medicamentos",
	"suplementos",
	"cosmeticos",
]);
const selectedCategory = ref("");

// Computed property for filtered products
const filteredProducts = computed(() => {
	if (!selectedCategory.value) return products.value;
	return products.value.filter(
		(product) =>
			product.categoria.toLowerCase() ===
			selectedCategory.value.toLowerCase(),
	);
});

// Fetch products from API
const fetchProducts = async () => {
	try {
		loading.value = true;
		error.value = "";
		products.value = await productoService.getAllProducts();
	} catch (err) {
		console.error("Error fetching products:", err);
		error.value =
			"Error al cargar productos. Por favor, intente de nuevo más tarde.";
	} finally {
		loading.value = false;
	}
};

// Format price - utility function
const formatPrice = (price: number) => {
	return price.toLocaleString("es-CL");
};

const route = useRoute();
onMounted(async () => {
	// Get category from URL query parameter
	const categoryFromQuery = route.query.category;
	if (
		categoryFromQuery &&
		categories.value.includes(categoryFromQuery as string)
	) {
		selectedCategory.value = categoryFromQuery as string;
	}

	// Fetch products
	await fetchProducts();
});
</script>

<template>
	<div class="container mx-auto px-4 py-8 flex">
		<!-- Sidebar categories -->
		<aside class="w-64 mr-6">
			<h2 class="text-lg font-bold text-gray-800 mb-4">Categorías</h2>
			<ul class="space-y-2">
				<li
					v-for="category in categories"
					:key="category"
					class="cursor-pointer text-gray-700 hover:text-blue-500"
					:class="{
						'font-bold text-blue-500':
							selectedCategory === category,
					}"
					@click="selectedCategory = category"
				>
					{{ category.charAt(0).toUpperCase() + category.slice(1) }}
				</li>
				<li
					class="cursor-pointer text-gray-700 hover:text-blue-500"
					:class="{
						'font-bold text-blue-500': selectedCategory === '',
					}"
					@click="selectedCategory = ''"
				>
					Todos
				</li>
			</ul>
		</aside>

		<div class="flex-1">
			<h1 class="text-2xl font-bold text-gray-800 mb-6 text-center">
				Productos disponibles
			</h1>

			<!-- Loading state -->
			<div v-if="loading" class="flex justify-center items-center py-12">
				<div
					class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"
				></div>
			</div>

			<!-- Error state -->
			<div v-else-if="error" class="text-center py-12">
				<p class="text-red-500">{{ error }}</p>
				<button
					@click="fetchProducts"
					class="mt-4 text-blue-500 hover:underline"
				>
					Intentar de nuevo
				</button>
			</div>

			<!-- Empty state -->
			<div
				v-else-if="filteredProducts.length === 0"
				class="text-center py-12"
			>
				<p class="text-gray-500">
					No hay productos disponibles en esta categoría.
				</p>
			</div>

			<!-- Products grid -->
			<div
				v-else
				class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6"
			>
				<div
					v-for="product in filteredProducts"
					:key="product.idProducto"
					class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow flex flex-col h-full"
				>
					<!-- Image with fallback -->
					<img
						:src="
							product.imageUrl ||
							'https://via.placeholder.com/150'
						"
						:alt="product.nombreProducto"
						class="w-full h-48 object-contain bg-white-100"
						@error="
							(e) =>
								(e.target.src =
									'https://via.placeholder.com/150')
						"
					/>
					<div class="p-4 flex flex-col flex-grow">
						<div class="flex-grow">
							<h2 class="text-lg font-semibold text-gray-800">
								{{ product.nombreProducto }}
							</h2>
							<p
								v-if="product.requiereReceta"
								class="text-sm text-red-600 mt-1"
							>
								Requiere receta
							</p>
							<p class="text-lg font-bold text-blue-500 mt-2">
								${{ formatPrice(product.precio) }}
							</p>
						</div>
						<button
							class="btn-custom mt-4 w-full flex items-center justify-center space-x-2"
						>
							<ShoppingCart class="w-5 h-5" />
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
.container {
	background-color: #f9fafb;
	min-height: 100vh;
}
</style>

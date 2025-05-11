<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import {
	ShoppingCart,
	Search,
	ChevronLeft,
	ChevronRight,
} from "lucide-vue-next";
import { productoService } from "@/api/services";
import farmaciaService from "@/api/services/farmaciaService";
import type { Product, FarmaciaEntity } from "@/api/models";
import { useCartStore } from '@/stores/cartStore';
import { toast } from 'vue-sonner';

// Cart store
const cartStore = useCartStore();

// Add function to handle adding product to cart
const addToCart = (product: Product) => {
	console.log("Adding to cart:", product);
	cartStore.addItem({
		idProducto: product.idProducto,
		nombreProducto: product.nombreProducto,
		precio: product.precio,
		requiereReceta: product.requiereReceta,
		image: product.imageUrl
	});

	// Show toast notification using Sonner
    toast.info('Producto agregado', {
        description: `${product.nombreProducto}`,
        action: {
            label: 'Deshacer',
            onClick: () => cartStore.removeItem(product.idProducto),
        }
    });
}


// State for products
const products = ref<Product[]>([]);
const loading = ref(true);
const error = ref("");

// Farmacias
const farmacias = ref<FarmaciaEntity[]>([]);
const selectedFarmacia = ref<number | null>(null);

// Search functionality
const searchQuery = ref("");

// Categories
const categories = ref([
  "dermatologia",
  "higiene",
  "medicamentos",
  "suplementos",
  "cosméticos",
]);
const selectedCategory = ref("");

// Pagination
const currentPage = ref(1);
const itemsPerPage = ref(8);

// Computed property for filtered products
const filteredProducts = computed(() => {

	if (!Array.isArray(products.value)) {
		console.log(products.value);
		return [];
  	}

	let result = products.value;

	// Filter by category if selected
	if (selectedCategory.value) {
		result = result.filter(
			(product) =>
				product.categoria.toLowerCase() ===
				selectedCategory.value.toLowerCase(),
		);
	}

	// Filter by search query if provided
	if (searchQuery.value.trim()) {
		const query = searchQuery.value.toLowerCase().trim();
		result = result.filter((product) =>
			product.nombreProducto.toLowerCase().includes(query),
		);
	}

	return result;
});

// Computed property for paginated products
const paginatedProducts = computed(() => {
	const startIndex = (currentPage.value - 1) * itemsPerPage.value;
	const endIndex = startIndex + itemsPerPage.value;
	return filteredProducts.value.slice(startIndex, endIndex);
});

// Computed property for total pages
const totalPages = computed(() => {
	return Math.max(
		1,
		Math.ceil(filteredProducts.value.length / itemsPerPage.value),
	);
});

// Fetch products
const fetchProducts = async () => {
	try {
		loading.value = true;
		error.value = "";

		if (selectedFarmacia.value) {
			// Si hay una farmacia seleccionada, obtenemos los productos de esa farmacia
			products.value = await farmaciaService.getProductosByFarmaciaId(
				selectedFarmacia.value,
			);
		} else {
			// Si no hay farmacia seleccionada, obtenemos todos los productos
			products.value = await productoService.getAllProducts();
		}
	} catch (err) {
		console.error("Error fetching products:", err);
		error.value =
			"Error al cargar productos. Por favor, intente de nuevo más tarde.";
	} finally {
		loading.value = false;
	}
};

// Fetch farmacias
const fetchFarmacias = async () => {
	try {
		farmacias.value = await farmaciaService.getAllFarmacias();
	} catch (err) {
		console.error("Error fetching farmacias:", err);
	}
};

// Format price - utility function
const formatPrice = (price: number) => {
	return price.toLocaleString("es-CL");
};

// Pagination methods
const nextPage = () => {
	if (currentPage.value < totalPages.value) {
		currentPage.value++;
		window.scrollTo({ top: 0, behavior: "smooth" });
	}
};

const prevPage = () => {
	if (currentPage.value > 1) {
		currentPage.value--;
		window.scrollTo({ top: 0, behavior: "smooth" });
	}
};

// Reset to first page when filters change
watch([selectedCategory, searchQuery], () => {
	currentPage.value = 1;
});

// When farmacia changes, fetch products for that farmacia
watch(selectedFarmacia, async () => {
	currentPage.value = 1;
	await fetchProducts();
});

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

	// Fetch farmacias
	await fetchFarmacias();

	// Fetch products
	await fetchProducts();
});
</script>

<template>
	<div class="products-page-container">
		<div class="container mx-auto px-4 py-8">
			<div class="grid grid-cols-1 md:grid-cols-12 gap-8">
				<!-- Sidebar categories -->
				<aside class="md:col-span-3 lg:col-span-2">
					<div class="sticky-sidebar">
						<!-- Farmacias section -->
						<h2 class="text-lg font-bold text-gray-800 mb-4">
							Farmacias
						</h2>
						<div class="farmacia-selector mb-6">
							<select
								v-model="selectedFarmacia"
								class="block w-full p-2 border border-gray-300 rounded-md bg-white focus:border-blue-500 focus:ring focus:ring-blue-200"
							>
								<option :value="null">
									Todas las farmacias
								</option>
								<option
									v-for="farmacia in farmacias"
									:key="farmacia.idFarmacia"
									:value="farmacia.idFarmacia"
								>
									{{ farmacia.nombreFarmacia }}
								</option>
							</select>
						</div>

						<!-- Categories section -->
						<h2 class="text-lg font-bold text-gray-800 mb-4">
							Categorías
						</h2>
						<ul class="space-y-2">
							<li
								v-for="category in categories"
								:key="category"
								class="cursor-pointer text-gray-700 hover:text-blue-500 category-item"
								:class="{
									'font-bold text-blue-500':
										selectedCategory === category,
								}"
								@click="selectedCategory = category"
							>
								{{
									category.charAt(0).toUpperCase() +
									category.slice(1)
								}}
							</li>
							<li
								class="cursor-pointer text-gray-700 hover:text-blue-500 category-item"
								:class="{
									'font-bold text-blue-500':
										selectedCategory === '',
								}"
								@click="selectedCategory = ''"
							>
								Todos
							</li>
						</ul>
					</div>
				</aside>

				<!-- Main content -->
				<main class="md:col-span-9 lg:col-span-10">
					<h1
						class="text-2xl font-bold text-gray-800 mb-6 text-center"
					>
						Productos disponibles
					</h1>

					<!-- Search bar -->
					<div class="mb-8">
						<div class="relative">
							<div
								class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none"
							>
								<Search class="w-5 h-5 text-gray-500" />
							</div>
							<input
								type="text"
								v-model="searchQuery"
								class="block w-full p-4 pl-10 text-sm text-gray-900 border border-gray-300 rounded-lg bg-white focus:ring-blue-500 focus:border-blue-500"
								placeholder="Buscar remedios..."
							/>
						</div>
					</div>

					<!-- States container with fixed height -->
					<div class="products-display-area">
						<!-- Loading state -->
						<div
							v-if="loading"
							class="flex justify-center items-center py-20"
						>
							<div
								class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"
							></div>
						</div>

						<!-- Error state -->
						<div v-else-if="error" class="text-center py-20">
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
							class="text-center py-20"
						>
							<p class="text-gray-500">
								No se encontraron productos con los criterios
								seleccionados.
							</p>
						</div>

						<!-- Products grid - fixed layout with equal-sized cards -->
						<div v-else>
							<div
								class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6"
							>
								<div
									v-for="product in paginatedProducts"
									:key="product.idProducto"
									class="product-card"
								>
									<div
										class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow h-full"
									>
										<!-- Image container with white background -->
										<div class="product-image-wrapper">
											<img
												:src="
													product.imageUrl ||
													'https://via.placeholder.com/150'
												"
												:alt="product.nombreProducto"
												class="product-image"
												@error="
													(e) =>
														(e.target.src =
															'https://via.placeholder.com/150')
												"
											/>
										</div>

										<!-- Product details with fixed heights -->
										<div class="p-4 product-content">
											<h2 class="product-name">
												{{ product.nombreProducto }}
											</h2>

											<!-- Prescription notice area - always reserved with same height -->
											<div class="prescription-area">
												<p
													v-if="
														product.requiereReceta
													"
													class="text-sm text-red-600"
												>
													Requiere receta
												</p>
												<p
													v-else
													class="text-sm opacity-0"
												>
													&nbsp;
													<!-- Invisible placeholder -->
												</p>
											</div>

											<!-- Category - fixed height -->
											<div
												class="product-category-container"
											>
												<p
													class="text-sm text-gray-600 product-category"
												>
													Categoría:
													{{
														product.categoria
															.charAt(0)
															.toUpperCase() +
														product.categoria.slice(
															1,
														)
													}}
												</p>
											</div>

											<!-- Stock info - only show if a farmacia is selected -->
											<div class="stock-container">
												<p
													v-if="selectedFarmacia"
													class="text-sm font-medium text-green-600"
												>
													Stock disponible
												</p>
												<p
													v-else
													class="text-sm opacity-0"
												>
													&nbsp;
												</p>
											</div>

											<!-- Price - fixed position -->
											<div
												class="product-price-container"
											>
												<p
													class="text-lg font-bold text-blue-500"
												>
													${{
														formatPrice(
															product.precio,
														)
													}}
												</p>
											</div>

											<!-- Add to cart button -->
											<button
												@click="addToCart(product)"
												class="btn-custom mt-3 w-full flex items-center justify-center space-x-2"
											>
												<ShoppingCart class="w-5 h-5" />
												<span>Agregar</span>
											</button>
										</div>
									</div>
								</div>
							</div>

							<!-- Always show placeholder rows to maintain height if needed -->
							<div
								v-if="paginatedProducts.length < itemsPerPage"
								class="placeholder-rows"
								:style="`height: ${(itemsPerPage - paginatedProducts.length) * 150}px`"
							></div>
						</div>
					</div>

					<!-- Pagination controls -->
					<div
						class="flex justify-center mt-8 items-center pagination-controls"
					>
						<button
							@click="prevPage"
							class="p-2 rounded-full hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed"
							:disabled="currentPage === 1"
						>
							<ChevronLeft class="w-6 h-6" />
						</button>

						<div class="mx-4 text-gray-700">
							Página {{ currentPage }} de {{ totalPages }}
						</div>

						<button
							@click="nextPage"
							class="p-2 rounded-full hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed"
							:disabled="currentPage === totalPages"
						>
							<ChevronRight class="w-6 h-6" />
						</button>
					</div>
				</main>
			</div>
		</div>
	</div>
</template>

<style scoped>
.products-page-container {
	background-color: #f9fafb;
	min-height: 100vh;
}

.sticky-sidebar {
	position: sticky;
	top: 2rem;
}

.category-item {
	padding: 0.5rem 0;
	transition: all 0.2s ease-in-out;
}

.products-display-area {
	min-height: 700px; /* Fixed height for the display area */
}

.product-card > .bg-white {
	display: flex;
	flex-direction: column;
	height: 100%;
}

.product-image-wrapper {
	width: 100%;
	height: 180px; /* ajústalo a lo que necesites */
	background-color: white;
	display: flex;
	align-items: center;
	justify-content: center;
	overflow: hidden;
}

.product-image {
	max-width: 100%;
	max-height: 100%;
	object-fit: contain;
}

.product-content {
	display: flex;
	flex-direction: column;
	flex: 1; /* ocupa todo el espacio restante */
	padding: 1rem; /* tu p-4 */
}
.product-content .btn-custom {
	margin-top: auto;
}

.product-name {
	font-size: 1rem;
	font-weight: 600;
	color: #374151;
	height: 3rem; /* sólo 2 líneas */
	margin-bottom: 0.5rem;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
	text-overflow: ellipsis;

	/* rompe palabras largas */
	overflow-wrap: anywhere;
	word-break: break-word;
}

.prescription-area {
	height: 1.5rem; /* Fixed height for prescription area */
	margin-bottom: 0.5rem;
}

.product-category-container {
	height: 1.5rem; /* Fixed height for category */
	margin-bottom: 0.5rem;
}

.product-category {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.stock-container {
	height: 1.5rem; /* Fixed height for stock info */
	margin-bottom: 0.5rem;
}

.product-price-container {
	height: 2rem; /* Fixed height for price */
	margin-bottom: 0.5rem;
}

.pagination-controls {
	min-height: 60px;
	margin-top: 2rem;
}

.btn-custom {
	background-color: #3b82f6;
	color: white;
	padding: 0.5rem 1rem;
	border-radius: 0.375rem;
	transition: background-color 0.2s;
	font-weight: 500;
}

.btn-custom:hover:not(:disabled) {
	background-color: #2563eb;
}

/* Responsive adjustments */
@media (max-width: 768px) {
	.products-display-area {
		min-height: 600px;
	}

	.sticky-sidebar {
		position: relative;
		top: 0;
	}
}
.products-display-area > div.grid {
	grid-auto-rows: 1fr;
}
</style>

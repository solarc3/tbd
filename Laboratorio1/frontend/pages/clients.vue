<template>
	<div class="container mx-auto p-4">
		<h1 class="text-3xl font-bold mb-6">Listado de Clientes</h1>

		<div v-if="loading" class="flex justify-center items-center py-8">
			<div
				class="animate-spin h-8 w-8 border-4 border-primary rounded-full border-t-transparent"
			></div>
		</div>

		<div
			v-else-if="error"
			class="bg-destructive/15 p-4 rounded-md text-destructive mb-4"
		>
			<div class="flex gap-2 items-center">
				<svg
					xmlns="http://www.w3.org/2000/svg"
					class="h-6 w-6 flex-shrink-0"
					fill="none"
					viewBox="0 0 24 24"
					stroke="currentColor"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
					/>
				</svg>
				<span>{{ error }}</span>
			</div>
		</div>

		<div
			v-else-if="clientes.length === 0"
			class="bg-primary/15 p-4 rounded-md text-primary mb-4"
		>
			<div class="flex gap-2 items-center">
				<svg
					xmlns="http://www.w3.org/2000/svg"
					class="h-6 w-6 flex-shrink-0"
					fill="none"
					viewBox="0 0 24 24"
					stroke="currentColor"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
					/>
				</svg>
				<span>No hay clientes registrados.</span>
			</div>
		</div>

		<div v-else>
			<Card v-for="cliente in clientes" :key="cliente.id" class="mb-4">
				<CardHeader>
					<CardTitle class="flex justify-between items-center">
						<span>{{ cliente.username }}</span>
						<Badge>$ {{ cliente.totalGastado }}</Badge>
					</CardTitle>
					<CardDescription>{{ cliente.email }}</CardDescription>
				</CardHeader>

				<CardContent>
					<Dialog>
						<DialogTrigger as-child>
							<Button variant="outline" class="w-full"
								>Ver pedidos</Button
							>
						</DialogTrigger>
						<DialogContent class="sm:max-w-[600px]">
							<DialogHeader>
								<DialogTitle
									>Pedidos de
									{{ cliente.username }}</DialogTitle
								>
								<DialogDescription
									>Lista de todos los pedidos realizados por
									este cliente</DialogDescription
								>
							</DialogHeader>

							<div
								v-if="
									loadingPedidos &&
									selectedClientId === cliente.id
								"
								class="flex justify-center py-4"
							>
								<div
									class="animate-spin h-6 w-6 border-4 border-primary rounded-full border-t-transparent"
								></div>
							</div>

							<div
								v-else-if="
									errorPedidos &&
									selectedClientId === cliente.id
								"
								class="bg-destructive/15 p-4 rounded-md text-destructive my-4"
							>
								<div class="flex gap-2 items-center">
									<svg
										xmlns="http://www.w3.org/2000/svg"
										class="h-5 w-5 flex-shrink-0"
										fill="none"
										viewBox="0 0 24 24"
										stroke="currentColor"
									>
										<path
											stroke-linecap="round"
											stroke-linejoin="round"
											stroke-width="2"
											d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
										/>
									</svg>
									<span>{{ errorPedidos }}</span>
								</div>
							</div>

							<div
								v-else-if="
									pedidos.length === 0 &&
									selectedClientId === cliente.id
								"
								class="bg-primary/15 p-4 rounded-md text-primary my-4"
							>
								<div class="flex gap-2 items-center">
									<svg
										xmlns="http://www.w3.org/2000/svg"
										class="h-5 w-5 flex-shrink-0"
										fill="none"
										viewBox="0 0 24 24"
										stroke="currentColor"
									>
										<path
											stroke-linecap="round"
											stroke-linejoin="round"
											stroke-width="2"
											d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
										/>
									</svg>
									<span>Este cliente no tiene pedidos.</span>
								</div>
							</div>

							<div
								v-else-if="selectedClientId === cliente.id"
								class="overflow-hidden rounded-md border"
							>
								<table class="w-full caption-bottom text-sm">
									<thead class="border-b bg-muted/50">
										<tr>
											<th
												class="h-10 px-4 text-left align-middle font-medium"
											>
												ID
											</th>
											<th
												class="h-10 px-4 text-left align-middle font-medium"
											>
												Fecha
											</th>
											<th
												class="h-10 px-4 text-left align-middle font-medium"
											>
												Monto
											</th>
											<th
												class="h-10 px-4 text-left align-middle font-medium"
											>
												Estado
											</th>
											<th
												class="h-10 px-4 text-left align-middle font-medium"
											>
												Acción
											</th>
										</tr>
									</thead>
									<tbody>
										<tr
											v-for="pedido in pedidos"
											:key="pedido.idPedido"
											class="border-b transition-colors hover:bg-muted/50"
										>
											<td class="p-4 align-middle">
												{{ pedido.idPedido }}
											</td>
											<td class="p-4 align-middle">
												{{
													formatDate(
														pedido.fechaPedido,
													)
												}}
											</td>
											<td class="p-4 align-middle">
												$ {{ pedido.monto }}
											</td>
											<td class="p-4 align-middle">
												<Badge
													:variant="
														getEstadoVariant(
															pedido.estadoPedido,
														)
													"
												>
													{{ pedido.estadoPedido }}
												</Badge>
											</td>
											<td class="p-4 align-middle">
												<Button
													variant="outline"
													size="sm"
													@click="
														verDetallePedido(pedido)
													"
												>
													Detalles
												</Button>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</DialogContent>
					</Dialog>
				</CardContent>
			</Card>
		</div>

		<!-- Modal para el detalle de un pedido específico -->
		<Dialog v-model:open="showDetailModal">
			<DialogContent class="sm:max-w-[500px]">
				<DialogHeader>
					<DialogTitle
						>Detalle del Pedido #{{
							pedidoSeleccionado?.idPedido
						}}</DialogTitle
					>
					<DialogDescription
						>Información detallada del pedido</DialogDescription
					>
				</DialogHeader>

				<div class="grid gap-4 py-4">
					<div class="grid grid-cols-4 items-center gap-4">
						<Label class="text-right">Fecha:</Label>
						<div class="col-span-3">
							{{
								formatDate(
									pedidoSeleccionado?.fechaPedido || "",
								)
							}}
						</div>
					</div>
					<div class="grid grid-cols-4 items-center gap-4">
						<Label class="text-right">Monto:</Label>
						<div class="col-span-3 font-medium">
							$ {{ pedidoSeleccionado?.monto }}
						</div>
					</div>
					<div class="grid grid-cols-4 items-center gap-4">
						<Label class="text-right">Estado:</Label>
						<div class="col-span-3">
							<Badge
								:variant="
									getEstadoVariant(
										pedidoSeleccionado?.estadoPedido || '',
									)
								"
							>
								{{ pedidoSeleccionado?.estadoPedido }}
							</Badge>
						</div>
					</div>
					<div class="grid grid-cols-4 items-center gap-4">
						<Label class="text-right">Urgente:</Label>
						<div class="col-span-3">
							{{ pedidoSeleccionado?.esUrgente ? "Sí" : "No" }}
						</div>
					</div>
				</div>

				<DialogFooter>
					<Button @click="showDetailModal = false">Cerrar</Button>
				</DialogFooter>
			</DialogContent>
		</Dialog>
	</div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { ClienteGasto } from "~/api/models";
import { userService } from "~/api/services/userService";
import pedidoService from "~/api/services/pedidoService";

// Import Shadcn components
import { Button } from "~/components/ui/button";
import {
	Card,
	CardContent,
	CardDescription,
	CardFooter,
	CardHeader,
	CardTitle,
} from "~/components/ui/card";
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from "~/components/ui/dialog";
import { Badge } from "~/components/ui/badge"; // Assuming you have this component
import { Label } from "~/components/ui/label";

const clientes = ref<ClienteGasto[]>([]);
const loading = ref(true);
const error = ref("");

const showDetailModal = ref(false);
const selectedClientId = ref<number | null>(null);
const pedidoSeleccionado = ref<any | null>(null);
const pedidos = ref<any[]>([]);
const loadingPedidos = ref(false);
const errorPedidos = ref("");

onMounted(async () => {
	try {
		loading.value = true;
		clientes.value = await userService.getAllClientsWithSpending();
	} catch (err) {
		error.value = "Error al cargar la lista de clientes";
		console.error(err);
	} finally {
		loading.value = false;
	}
});

const verDetalles = async (clienteId: number) => {
	selectedClientId.value = clienteId;
	loadingPedidos.value = true;
	errorPedidos.value = "";

	try {
		const response = await pedidoService.getPedidosByCliente(clienteId);
		pedidos.value = Array.isArray(response) ? response : [];
	} catch (err) {
		errorPedidos.value = "Error al cargar los pedidos";
		console.error(err);
	} finally {
		loadingPedidos.value = false;
	}
};

const verDetallePedido = (pedido: any) => {
	pedidoSeleccionado.value = pedido;
	showDetailModal.value = true;
};

const formatDate = (dateString: string) => {
	if (!dateString) return "";
	const date = new Date(dateString);
	return date.toLocaleDateString() + " " + date.toLocaleTimeString();
};

const getEstadoVariant = (estado: string) => {
	switch (estado) {
		case "ENTREGADO":
			return "success";
		case "CONFIRMADO":
			return "info";
		case "POR_CONFIRMAR":
			return "warning";
		case "CANCELADO":
			return "destructive";
		default:
			return "default";
	}
};
</script>

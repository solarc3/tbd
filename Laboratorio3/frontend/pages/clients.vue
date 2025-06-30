<template>
  <div class="container mx-auto p-4">
    <h1 class="text-3xl font-bold mb-6">Gestión de Clientes</h1>

    <Tabs v-model="activeTab" default-value="listado" class="w-full">
      <TabsList class="grid w-full grid-cols-2 h-10 bg-muted p-1 rounded-md">
        <TabsTrigger value="listado" class="rounded-sm">Listado de Clientes</TabsTrigger>
        <TabsTrigger value="navegacion" class="rounded-sm">Sin Compras</TabsTrigger>
      </TabsList>

      <TabsContent value="listado" class="mt-6">
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
              <Button
                  variant="outline"
                  class="w-full"
                  @click="mostrarPedidosCliente(cliente.id)"
              >
                Ver pedidos
              </Button>
            </CardContent>
          </Card>
        </div>
      </TabsContent>

      <TabsContent value="navegacion" class="mt-6">
        <div class="space-y-4">
          <div class="flex justify-between items-center">
            <h2 class="text-xl font-semibold">Clientes que Navegaron sin Comprar</h2>
            <Button
                @click="cargarNavegacionSinCompras"
                :disabled="loadingNavegacion"
                variant="outline"
            >
              <svg
                  v-if="loadingNavegacion"
                  class="animate-spin h-4 w-4 mr-2"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
              >
                <circle
                    class="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    stroke-width="4"
                ></circle>
                <path
                    class="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                ></path>
              </svg>
              Actualizar
            </Button>
          </div>

          <div v-if="loadingNavegacion" class="flex justify-center items-center py-8">
            <div
                class="animate-spin h-8 w-8 border-4 border-primary rounded-full border-t-transparent"
            ></div>
          </div>

          <div
              v-else-if="errorNavegacion"
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
              <span>{{ errorNavegacion }}</span>
            </div>
          </div>

          <div
              v-else-if="navegacionSinCompras.length === 0"
              class="bg-green-50 border border-green-200 p-4 rounded-md"
          >
            <div class="flex gap-2 items-center">
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-6 w-6 flex-shrink-0 text-green-600"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
              <span class="text-green-700">¡Excelente! Todos los clientes que navegaron realizaron compras.</span>
            </div>
          </div>

          <div v-else class="space-y-4">
            <div class="bg-yellow-50 border border-yellow-200 p-4 rounded-md mb-4">
              <div class="flex gap-2 items-center">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-5 w-5 text-yellow-600"
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
                <span class="text-yellow-700 font-medium">
									{{ navegacionSinCompras.length }} cliente{{ navegacionSinCompras.length !== 1 ? 's' : '' }}
									navegaron sin realizar compras
								</span>
              </div>
            </div>

            <Card v-for="cliente in navegacionSinCompras" :key="cliente.idCliente" class="mb-4">
              <CardHeader>
                <CardTitle class="flex justify-between items-center">
                  <span>{{ cliente.nombreCliente || `Cliente ID: ${cliente.idCliente}` }}</span>
                  <Badge variant="destructive">Sin compra</Badge>
                </CardTitle>
                <CardDescription>
                  {{ cliente.historial.length }} acción{{ cliente.historial.length !== 1 ? 'es' : '' }}
                  de navegación registrada{{ cliente.historial.length !== 1 ? 's' : '' }}
                </CardDescription>
              </CardHeader>

              <CardContent>
                <Button
                    variant="outline"
                    class="w-full"
                    @click="verHistorialNavegacion(cliente)"
                >
                  Ver historial de navegación
                </Button>
              </CardContent>
            </Card>
          </div>
        </div>
      </TabsContent>
    </Tabs>

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

    <Dialog v-model:open="showNavegacionModal">
      <DialogContent class="sm:max-w-[700px] max-h-[80vh] overflow-y-auto bg-white border shadow-lg">
        <DialogHeader class="bg-white border-b pb-4">
          <DialogTitle class="text-foreground">
            Historial de Navegación - {{ clienteNavegacionSeleccionado?.nombreCliente || `Cliente ID: ${clienteNavegacionSeleccionado?.idCliente}` }}
          </DialogTitle>
          <DialogDescription class="text-muted-foreground">
            Registro detallado de las acciones de navegación sin compra
          </DialogDescription>
        </DialogHeader>

        <div class="space-y-3 py-4 bg-white">
          <div
              v-for="(log, index) in clienteNavegacionSeleccionado?.historial || []"
              :key="log.id"
              class="p-4 border rounded-lg bg-slate-50 shadow-sm"
          >
            <div class="flex justify-between items-start mb-2">
              <Badge variant="outline" class="text-xs bg-white">
                Acción {{ index + 1 }}
              </Badge>
              <span class="text-xs text-muted-foreground">
								{{ formatDate(log.timestamp) }}
							</span>
            </div>
            <div class="grid grid-cols-2 gap-4 text-sm">
              <div>
                <Label class="text-xs font-medium text-muted-foreground">Desde:</Label>
                <p class="font-mono text-xs bg-white text-foreground p-2 rounded border mt-1 shadow-sm">
                  {{ log.fromUrl || 'N/A' }}
                </p>
              </div>
              <div>
                <Label class="text-xs font-medium text-muted-foreground">Hacia:</Label>
                <p class="font-mono text-xs bg-white text-foreground p-2 rounded border mt-1 shadow-sm">
                  {{ log.toUrl || 'N/A' }}
                </p>
              </div>
            </div>
          </div>

          <div v-if="!clienteNavegacionSeleccionado?.historial?.length" class="text-center py-8 bg-white">
            <p class="text-muted-foreground">No hay historial de navegación disponible</p>
          </div>
        </div>

        <DialogFooter class="bg-white border-t pt-4">
          <Button @click="showNavegacionModal = false" variant="outline">Cerrar</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>

    <PedidosCliente ref="pedidosClienteRef" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { ClienteGasto, NavegacionSinCompraResponse } from "~/api/models";
import { userService } from "~/api/services/userService";
import pedidoService from "~/api/services/pedidoService";
import navegacionService from "~/api/services/navegacionService";
import PedidosCliente from "~/components/PedidosCliente.vue";

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
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from "~/components/ui/tabs";
import { Badge } from "~/components/ui/badge";
import { Label } from "~/components/ui/label";

// Estados existentes
const clientes = ref<ClienteGasto[]>([]);
const loading = ref(true);
const error = ref("");

const showDetailModal = ref(false);
const selectedClientId = ref<number | null>(null);
const pedidoSeleccionado = ref<any | null>(null);
const pedidos = ref<any[]>([]);
const loadingPedidos = ref(false);
const errorPedidos = ref("");

const pedidosClienteRef = ref();

// Nuevos estados para navegación
const activeTab = ref("listado");
const navegacionSinCompras = ref<NavegacionSinCompraResponse[]>([]);
const loadingNavegacion = ref(false);
const errorNavegacion = ref("");
const showNavegacionModal = ref(false);
const clienteNavegacionSeleccionado = ref<NavegacionSinCompraResponse | null>(null);

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

const mostrarPedidosCliente = (clienteId: number) => {
  if (pedidosClienteRef.value) {
    const cliente = clientes.value.find(c => c.id === clienteId);
    const username = cliente?.username || `Cliente ${clienteId}`;

    pedidosClienteRef.value.setClienteId(clienteId, username);
    pedidosClienteRef.value.toggleModal();
  }
};

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

const cargarNavegacionSinCompras = async () => {
  loadingNavegacion.value = true;
  errorNavegacion.value = "";

  try {
    console.log('Iniciando carga de navegación sin compras...');
    navegacionSinCompras.value = await navegacionService.getNavegacionSinCompras();
    console.log('Datos cargados:', navegacionSinCompras.value);
  } catch (err: any) {
    console.error('Error completo:', err);
    errorNavegacion.value = err.message || "Error al cargar datos de navegación sin compras";
  } finally {
    loadingNavegacion.value = false;
  }
};

const verHistorialNavegacion = (cliente: NavegacionSinCompraResponse) => {
  clienteNavegacionSeleccionado.value = cliente;
  showNavegacionModal.value = true;
};

const formatDate = (dateValue: string | number) => {
  if (!dateValue) return "";
  const timestampInMs = typeof dateValue === 'number' ? dateValue * 1000 : dateValue;
  const date = new Date(timestampInMs);
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

onMounted(() => {
  if (activeTab.value === "navegacion") {
    cargarNavegacionSinCompras();
  }
});
</script>

<style scoped>
:deep([data-radix-dialog-content]) {
  background-color: white !important;
  border: 1px solid #e2e8f0 !important;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05) !important;
}

:deep([data-radix-dialog-overlay]) {
  background-color: rgba(0, 0, 0, 0.5) !important;
}

:deep(.space-y-3) {
  background-color: white !important;
}

:deep(.bg-slate-50) {
  background-color: #f8fafc !important;
}

:deep(.bg-white) {
  background-color: white !important;
}
</style>
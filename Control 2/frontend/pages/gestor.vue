<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import TareaService from "@/api/services/tareaService";
import type { Tarea } from "@/api/services/tareaService";
import CrearTarea from "@/components/CrearTarea.vue";

interface Task {
  id: number;
  title: string;
  description: string;
  dueDate: string;
  sector: string;
  estado: string;
}

const tasks = ref<Tarea[]>([]);
const searchQuery = ref("");
const isModalOpen = ref(false);
const isEditMode = ref(false);
const selectedTask = ref<Task | undefined>(undefined);

const fetchTasks = async () => {
  try {
    tasks.value = await TareaService.getAllTareas();
  } catch (error) {
    console.error("Error al cargar las tareas:", error);
  }
};

const openEditModal = (task: Tarea) => {
  selectedTask.value = {
    id: task.id,
    title: task.titulo || "",
    description: task.descripcion || "",
    dueDate: task.fechaVencimiento || "",
    sector: task.idSector ? task.idSector.toString() : "1",
    estado: task.estado || "PENDIENTE",
  };
  isEditMode.value = true;
  isModalOpen.value = true;
};

const openCreateModal = () => {
  selectedTask.value = {
    id: 0,
    title: "",
    description: "",
    dueDate: "",
    sector: "",
    estado: "PENDIENTE",
  };
  isEditMode.value = false;
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const saveTask = async (task: Partial<Task>) => {
  try {
    if (isEditMode.value && task.id) {
      const tareaToUpdate = {
        id: task.id,
        titulo: task.title || "",
        descripcion: task.description || "",
        fechaVencimiento: task.dueDate || "",
        idSector: task.sector ? parseInt(task.sector) : 1,
        estado: task.estado || "PENDIENTE",
      };

      await TareaService.updateTarea(task.id, tareaToUpdate);
    } else {
      const nuevaTarea = {
        titulo: task.title || "",
        descripcion: task.description || "",
        fechaVencimiento: task.dueDate || "",
        idSector: task.sector ? parseInt(task.sector) : 1,
        estado: "PENDIENTE",
      };

      await TareaService.createTarea(nuevaTarea);
    }

    fetchTasks();
    closeModal();
  } catch (error) {
    console.error("Error al guardar la tarea:", error);
  }
};

const deleteTask = async (id: number) => {
  try {
    await TareaService.deleteTarea(id);
    fetchTasks();
  } catch (error) {
    console.error("Error al eliminar la tarea:", error);
  }
};

const toggleComplete = async (task: Tarea) => {
  try {
    let newEstado = "";

    if (task.estado === "PENDIENTE") {
      newEstado = "EN_PROGRESO";
    } else if (task.estado === "EN_PROGRESO") {
      newEstado = "COMPLETADA";
    } else {
      console.warn(
        "El estado COMPLETADA no puede ser modificado desde la casilla.",
      );
      return;
    }

    await TareaService.updateTarea(task.id, {
      id: task.id,
      titulo: task.titulo || "",
      descripcion: task.descripcion || "",
      fechaVencimiento: task.fechaVencimiento || "",
      idUsuario: task.idUsuario || 1,
      idSector: task.idSector || 1,
      estado: newEstado,
    });

    task.estado = newEstado;
  } catch (error) {
    console.error("Error al actualizar el estado de la tarea:", error);
  }
};

const pendientes = computed(() =>
  tasks.value.filter(
    (task) =>
      task.estado === "PENDIENTE" &&
      (task.titulo.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        task.descripcion
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())),
  ),
);

const enProgreso = computed(() =>
  tasks.value.filter(
    (task) =>
      task.estado === "EN_PROGRESO" &&
      (task.titulo.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        task.descripcion
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())),
  ),
);

const completadas = computed(() =>
  tasks.value.filter(
    (task) =>
      task.estado === "COMPLETADA" &&
      (task.titulo.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        task.descripcion
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())),
  ),
);

onMounted(() => {
  fetchTasks();
});
</script>

<template>
  <div class="px-6 py-8">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Gestor de Tareas</h1>
      <button
        class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition"
        @click="openCreateModal"
      >
        Crear Tarea
      </button>
    </div>

    <!-- Barra de búsqueda -->
    <div class="flex gap-4 mb-6">
      <input
        v-model="searchQuery"
        type="text"
        placeholder="Buscar por palabra clave..."
        class="border border-gray-300 rounded-md px-4 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500">
    </div>

    <!-- Tareas pendientes -->
    <div v-if="pendientes.length" class="mb-6">
      <h2 class="text-xl font-semibold text-red-600 mb-4">Tareas Pendientes</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 auto-rows-fr">
        <div
          v-for="task in pendientes"
          :key="task.id"
          class="p-4 border rounded-lg bg-red-50 shadow-sm hover:shadow-md transition flex flex-col h-full"
        >
          <div class="flex-grow">
            <h2 class="text-lg font-semibold">{{ task.titulo }}</h2>
            <p class="text-sm text-gray-600">{{ task.descripcion }}</p>
            <p class="text-sm text-gray-500 mt-2">
              Vence: {{ task.fechaVencimiento }}
            </p>
            <p class="text-sm text-gray-500">Sector: {{ task.idSector }}</p>
          </div>
          <div class="flex justify-between items-center mt-4 pt-3 border-t">
            <div>
              <input
                type="checkbox"
                class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                :checked="task.estado === 'EN_PROGRESO'"
                :disabled="task.estado === 'COMPLETADA'"
                @change="toggleComplete(task)">
              <span class="ml-2 text-sm">En Progreso</span>
            </div>
            <div class="flex gap-2">
              <button
                class="px-3 py-1 bg-blue-100 text-blue-700 rounded hover:bg-blue-200 transition text-sm"
                @click="openEditModal(task)"
              >
                Editar
              </button>
              <button
                class="px-3 py-1 bg-red-100 text-red-700 rounded hover:bg-red-200 transition text-sm"
                @click="deleteTask(task.id)"
              >
                Eliminar
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tareas en progreso -->
    <div v-if="enProgreso.length" class="mb-6">
      <h2 class="text-xl font-semibold text-blue-600 mb-4">
        Tareas en Progreso
      </h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 auto-rows-fr">
        <div
          v-for="task in enProgreso"
          :key="task.id"
          class="p-4 border rounded-lg bg-blue-50 shadow-sm hover:shadow-md transition flex flex-col h-full"
        >
          <div class="flex-grow">
            <h2 class="text-lg font-semibold">{{ task.titulo }}</h2>
            <p class="text-sm text-gray-600">{{ task.descripcion }}</p>
            <p class="text-sm text-gray-500 mt-2">
              Vence: {{ task.fechaVencimiento }}
            </p>
            <p class="text-sm text-gray-500">Sector: {{ task.idSector }}</p>
          </div>
          <div class="flex justify-between items-center mt-4 pt-3 border-t">
            <div>
              <input
                type="checkbox"
                class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                :checked="task.estado === 'COMPLETADA'"
                @change="toggleComplete(task)">
              <span class="ml-2 text-sm">Completada</span>
            </div>
            <div class="flex gap-2">
              <button
                class="px-3 py-1 bg-blue-100 text-blue-700 rounded hover:bg-blue-200 transition text-sm"
                @click="openEditModal(task)"
              >
                Editar
              </button>
              <button
                class="px-3 py-1 bg-red-100 text-red-700 rounded hover:bg-red-200 transition text-sm"
                @click="deleteTask(task.id)"
              >
                Eliminar
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tareas completadas -->
    <div v-if="completadas.length" class="mb-6">
      <h2 class="text-xl font-semibold text-green-600 mb-4">
        Tareas Completadas
      </h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 auto-rows-fr">
        <div
          v-for="task in completadas"
          :key="task.id"
          class="p-4 border rounded-lg bg-green-50 shadow-sm hover:shadow-md transition flex flex-col h-full"
        >
          <div class="flex-grow">
            <h2 class="text-lg font-semibold">{{ task.titulo }}</h2>
            <p class="text-sm text-gray-600">{{ task.descripcion }}</p>
            <p class="text-sm text-gray-500 mt-2">
              Vence: {{ task.fechaVencimiento }}
            </p>
            <p class="text-sm text-gray-500">Sector: {{ task.idSector }}</p>
          </div>
          <div class="flex justify-between items-center mt-4 pt-3 border-t">
            <div>
              <input
                type="checkbox"
                class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                :checked="task.estado === 'COMPLETADA'"
                :disabled="task.estado === 'COMPLETADA'">
              <span class="ml-2 text-sm">Completada</span>
            </div>
            <div class="flex gap-2">
              <button
                class="px-3 py-1 bg-blue-100 text-blue-700 rounded hover:bg-blue-200 transition text-sm"
                @click="openEditModal(task)"
              >
                Editar
              </button>
              <button
                class="px-3 py-1 bg-red-100 text-red-700 rounded hover:bg-red-200 transition text-sm"
                @click="deleteTask(task.id)"
              >
                Eliminar
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Using Teleport for modal with transparent background -->
    <Teleport to="body">
      <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
        <div class="modal-content">
          <CrearTarea
            :task="selectedTask"
            :is-edit="isEditMode"
            @close="closeModal"
            @save="saveTask"
          />
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 1rem;
}

.modal-content {
  background: white;
  border-radius: 0.5rem;
  width: 100%;
  max-width: 28rem;
  box-shadow:
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);
  max-height: 90vh;
  overflow-y: auto;
}

input[type="checkbox"] {
  width: 1rem;
  height: 1rem;
}
</style>

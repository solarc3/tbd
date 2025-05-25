<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Gestor de Tareas</h1>
    </div>

    <!-- barra busqueda -->
    <div class="flex gap-4 mb-6">
      <input
          v-model="searchQuery"
          type="text"
          placeholder="Buscar por palabra clave..."
          class="input input-bordered w-full"
      />
    </div>

    <!-- tareas pendientes -->
    <div v-if="pendientes.length" class="mb-6">
      <h2 class="text-xl font-semibold text-red-600 mb-4">Tareas Pendientes</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
            v-for="task in pendientes"
            :key="task.id"
            class="p-4 border rounded-lg bg-red-100"
        >
          <h2 class="text-lg font-semibold">{{ task.titulo }}</h2>
          <p class="text-sm text-gray-600">{{ task.descripcion }}</p>
          <p class="text-sm text-gray-500">Vence: {{ task.fechaVencimiento }}</p>
          <p class="text-sm text-gray-500">Sector: {{ task.idSector }}</p>
          <div class="flex justify-between items-center mt-4">
            <div>
              <input
                  type="checkbox"
                  :checked="task.estado === 'EN_PROGRESO'"
                  @change="toggleComplete(task)"
              />
              <span class="ml-2">En Progreso</span>
            </div>
            <div class="flex gap-2">
              <button @click="openEditModal(task)" class="btn btn-sm btn-secondary">Editar</button>
              <button @click="deleteTask(task.id)" class="btn btn-sm btn-error">Eliminar</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- tareas en progreso -->
    <div v-if="enProgreso.length" class="mb-6">
      <h2 class="text-xl font-semibold text-blue-600 mb-4">Tareas en Progreso</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
            v-for="task in enProgreso"
            :key="task.id"
            class="p-4 border rounded-lg bg-blue-100"
        >
          <h2 class="text-lg font-semibold">{{ task.titulo }}</h2>
          <p class="text-sm text-gray-600">{{ task.descripcion }}</p>
          <p class="text-sm text-gray-500">Vence: {{ task.fechaVencimiento }}</p>
          <p class="text-sm text-gray-500">Sector: {{ task.idSector }}</p>
          <div class="flex justify-between items-center mt-4">
            <div>
              <input
                  type="checkbox"
                  :checked="task.estado === 'COMPLETADA'"
                  @change="toggleComplete(task)"
              />
              <span class="ml-2">Completada</span>
            </div>
            <div class="flex gap-2">
              <button @click="openEditModal(task)" class="btn btn-sm btn-secondary">Editar</button>
              <button @click="deleteTask(task.id)" class="btn btn-sm btn-error">Eliminar</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- tareas completadas -->
    <div v-if="completadas.length" class="mb-6">
      <h2 class="text-xl font-semibold text-green-600 mb-4">Tareas Completadas</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
            v-for="task in completadas"
            :key="task.id"
            class="p-4 border rounded-lg bg-green-100"
        >
          <h2 class="text-lg font-semibold">{{ task.titulo }}</h2>
          <p class="text-sm text-gray-600">{{ task.descripcion }}</p>
          <p class="text-sm text-gray-500">Vence: {{ task.fechaVencimiento }}</p>
          <p class="text-sm text-gray-500">Sector: {{ task.idSector }}</p>
          <div class="flex justify-between items-center mt-4">
            <div>
              <input
                  type="checkbox"
                  :checked="task.estado === 'COMPLETADA'"
                  @change="toggleComplete(task)"
              />
              <span class="ml-2">Completada</span>
            </div>
            <div class="flex gap-2">
              <button @click="openEditModal(task)" class="btn btn-sm btn-secondary">Editar</button>
              <button @click="deleteTask(task.id)" class="btn btn-sm btn-error">Eliminar</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from "vue";
import TareaService from "@/api/services/tareaService";
import type { Tarea } from "@/api/services/tareaService";

export default defineComponent({
  components: {  },
  setup() {
    const tasks = ref<Tarea[]>([]);
    const searchQuery = ref("");
    const filterStatus = ref("");
    const isModalOpen = ref(false);
    const isEditMode = ref(false);
    const selectedTask = ref<Tarea | undefined>(undefined);

    const fetchTasks = async () => {
      try {
        tasks.value = await TareaService.getAllTareas();
      } catch (error) {
        console.error("Error al cargar las tareas:", error);
      }
    };

    const openEditModal = (task: Tarea) => {
      selectedTask.value = { ...task };
      isEditMode.value = true;
      isModalOpen.value = true;
    };

    const saveTask = async (task: Partial<Tarea>) => {
      try {
        if (isEditMode.value && task.id) {
          await TareaService.updateTarea(task.id, task);
        } else {
          await TareaService.createTarea(task);
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

    // funcion principal que maneja el filtrado de tareas, junto con las casillas de completado y en progreso!
    // esta hecho para que el flujo sea PENDIENTE -> EN_PROGRESO -> COMPLETAD, y si se desmarca completada,
    // vuelve a estar en progreso
    const toggleComplete = async (task: Tarea) => {
      try {
        // dependiendo del estado actual, se define el siguiente
        let newEstado = "";
        if (task.estado === "PENDIENTE") {
          newEstado = "EN_PROGRESO";
        } else if (task.estado === "EN_PROGRESO") {
          newEstado = "COMPLETADA";
        } else if (task.estado === "COMPLETADA") {
          newEstado = "EN_PROGRESO";
        }

        // PUT backend
        await TareaService.updateTarea(task.id, {
          ...task,
          estado: newEstado,
        });

        // y se actualiza para mostrarlo en el front filtrado!
        task.estado = newEstado;
      } catch (error) {
        console.error("Error al actualizar el estado de la tarea:", error);
      }
    };

    const filteredTasks = computed(() => {
      return tasks.value.filter((task) => {
        const matchesStatus =
          !filterStatus.value || task.estado === filterStatus.value;

        const matchesSearch =
          !searchQuery.value ||
          task.titulo.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          task.descripcion
            .toLowerCase()
            .includes(searchQuery.value.toLowerCase());

        return matchesStatus && matchesSearch;
      });
    });

    const openCreateModal = () => {
      selectedTask.value = undefined;
      isEditMode.value = false;
      isModalOpen.value = true;
    };

    const closeModal = () => {
      isModalOpen.value = false;
    };

    const pendientes = computed(() =>
        tasks.value.filter(
            (task) =>
                task.estado === "PENDIENTE" &&
                (task.titulo.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                    task.descripcion
                        .toLowerCase()
                        .includes(searchQuery.value.toLowerCase()))
        )
    );

    const enProgreso = computed(() =>
        tasks.value.filter(
            (task) =>
                task.estado === "EN_PROGRESO" &&
                (task.titulo.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                    task.descripcion
                        .toLowerCase()
                        .includes(searchQuery.value.toLowerCase()))
        )
    );

    const completadas = computed(() =>
        tasks.value.filter(
            (task) =>
                task.estado === "COMPLETADA" &&
                (task.titulo.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                    task.descripcion
                        .toLowerCase()
                        .includes(searchQuery.value.toLowerCase()))
        )
    );

    onMounted(() => {
      fetchTasks();
    });

    return {
      tasks,
      searchQuery,
      filterStatus,
      filteredTasks,
      isModalOpen,
      isEditMode,
      selectedTask,
      pendientes,
      enProgreso,
      completadas,
      openCreateModal,
      openEditModal,
      closeModal,
      saveTask,
      deleteTask,
      toggleComplete,
    };
  },
});
</script>
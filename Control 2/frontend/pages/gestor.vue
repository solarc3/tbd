<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">Gestor de Tareas</h1>
      <button @click="openCreateModal" class="btn btn-primary">Crear Tarea</button>
    </div>

    <!-- barra busqueda -->
    <div class="flex gap-4 mb-6">
      <input
          v-model="searchQuery"
          type="text"
          placeholder="Buscar por palabra clave..."
          class="input input-bordered w-full"
      />
      <select v-model="filterStatus" class="select select-bordered">
        <option value="">Todos</option>
        <option value="pending">Pendientes</option>
        <option value="completed">Completadas</option>
      </select>
    </div>

    <!-- listado tareas -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div
        v-for="task in filteredTasks"
        :key="task.id"
        :class="['p-4 border rounded-lg', task.completed ? 'bg-gray-200' : 'bg-white']"
      >
        <h2 class="text-lg font-semibold">{{ task.title }}</h2>
        <p class="text-sm text-gray-600">{{ task.description }}</p>
        <p class="text-sm text-gray-500">Vence: {{ task.dueDate }}</p>
        <p class="text-sm text-gray-500">Sector: {{ task.sector }}</p>
        <div class="flex justify-between items-center mt-4">
          <div>
            <input
              type="checkbox"
              :checked="task.completed"
              @change="toggleComplete(task.id)"
              class="checkbox"
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

    <!-- modales nse cmo sacar este error T-T -->
    <TaskModal
      v-if="isModalOpen"
      :task="selectedTask"
      :is-edit="isEditMode"
      @close="closeModal"
      @save="saveTask"
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from "vue";
import TareaService from "@/api/services/tareaService";
import type { Tarea } from "@/api/services/tareaService";
import TaskModal from "../components/gestorTareas.vue";

export default defineComponent({
  components: { TaskModal },
  setup() {
    const tasks = ref<Task[]>([]);
    const searchQuery = ref("");
    const filterStatus = ref("");
    const isModalOpen = ref(false);
    const isEditMode = ref(false);
    const selectedTask = ref<Tarea | undefined>(undefined);

    const fetchTasks = async () => {
      try {
        const rawTasks = await TareaService.getAllTareas();
        tasks.value = rawTasks.map((t: Tarea) => ({
          id: t.id,
          title: t.titulo,
          description: t.descripcion,
          dueDate: t.fechaVencimiento,
          sector: t.idSector.toString(),
          completed: t.estado === "completed",
        }));
      } catch (error) {
        console.error("Error al cargar las tareas:", error);
      }
    };

    const openEditModal = (task: Task) => {
      selectedTask.value = { ...task };
      isEditMode.value = true;
      isModalOpen.value = true;
    };

    const saveTask = async (task: Partial<Task>) => {
      try {
        if (isEditMode.value && task.id) {
          await TareaService.updateTarea(task.id, {
            titulo: task.title,
            descripcion: task.description,
            fechaVencimiento: task.dueDate,
            idSector: parseInt(task.sector),
            estado: task.completed ? "completed" : "pending",
          });
        } else {
          await TareaService.createTarea({
            titulo: task.title,
            descripcion: task.description,
            fechaVencimiento: task.dueDate,
            idSector: parseInt(task.sector),
            estado: "pending",
          });
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

    const toggleComplete = async (id: number) => {
      const task = tasks.value.find((t) => t.id === id);
      if (task) {
        try {
          await TareaService.updateTarea(id, {
            ...task,
            estado: task.estado === "completed" ? "pending" : "completed",
          });
          fetchTasks();
        } catch (error) {
          console.error("Error al actualizar el estado de la tarea:", error);
        }
      }
    };

    const filteredTasks = computed(() => {
      return tasks.value.filter((task) => {
        const matchesStatus =
          !filterStatus.value ||
          (filterStatus.value === "completed" && task.completed) ||
          (filterStatus.value === "pending" && !task.completed);

        const matchesSearch =
          !searchQuery.value ||
          (task.title && task.title.toLowerCase().includes(searchQuery.value.toLowerCase())) ||
          (task.description && task.description.toLowerCase().includes(searchQuery.value.toLowerCase()));

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
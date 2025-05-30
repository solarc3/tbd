<template>
  <div>
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg relative">
      <h2 class="text-xl font-bold mb-4 text-gray-800 border-b pb-2">{{ isEdit ? "Editar Tarea" : "Crear Tarea" }}</h2>
      <form @submit.prevent="handleSubmit">
        <div v-if="!isEdit">
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1 text-gray-700">Título</label>
            <input v-model="localTask.title" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1 text-gray-700">Descripción</label>
            <textarea v-model="localTask.description" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 min-h-[100px]" required />
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1 text-gray-700">Fecha de Vencimiento</label>
              <input v-model="localTask.dueDate" type="date" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
            </div>
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1 text-gray-700">Sector</label>
              <select v-model="localTask.sector" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
                <option value="" disabled>Seleccione un sector</option>
                <option value="1">Sector 1</option>
                <option value="2">Sector 2</option>
                <option value="3">Sector 3</option>
              </select>
            </div>
          </div>
        </div>
        <div v-else>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1 text-gray-700">Estado</label>
            <select v-model="localTask.estado" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
              <option value="PENDIENTE">Pendiente</option>
              <option value="EN_PROGRESO">En Progreso</option>
              <option value="COMPLETADA">Completada</option>
            </select>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1 text-gray-700">Título</label>
            <input v-model="localTask.title" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" required>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1 text-gray-700">Descripción</label>
            <textarea v-model="localTask.description" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 min-h-[100px]" required />
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6 pt-4 border-t">
          <button class="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 transition font-medium" type="button" @click="$emit('close')">Cancelar</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition font-medium">Guardar</button>
        </div>
      </form>
    </div>
  </div>
</template>


<script lang="ts">
import { defineComponent, ref, watch } from "vue";
import type { PropType } from "vue";

interface Task {
  id: number;
  title: string;
  description: string;
  dueDate: string;
  sector: string;
  estado: string;
}

export default defineComponent({
  props: {
    task: {
      type: Object as PropType<Task>,
      default: () => ({
        id: 0,
        title: "",
        description: "",
        dueDate: "",
        sector: "",
        estado: "PENDIENTE",
      }),
    },
    isEdit: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["close", "save"],
  setup(props, { emit }) {
    const localTask = ref<Task>({
      id: props.task.id || 0,
      title: props.task.title || "",
      description: props.task.description || "",
      dueDate: props.task.dueDate || "",
      sector: props.task.sector || "",
      estado: props.task.estado || "PENDIENTE",
    });

    // Watch for changes to props.task and update localTask accordingly
    watch(() => props.task, (newTask) => {
      localTask.value = {
        id: newTask.id || 0,
        title: newTask.title || "",
        description: newTask.description || "",
        dueDate: newTask.dueDate || "",
        sector: newTask.sector || "",
        estado: newTask.estado || "PENDIENTE",
      };
    }, { deep: true });

    const handleSubmit = () => {
      // Ensure we're sending a valid object
      if (!localTask.value.title || !localTask.value.description) {
        alert('Por favor, complete todos los campos requeridos.');
        return;
      }

      emit("save", localTask.value);
    };

    return { localTask, handleSubmit };
  },
});
</script>

<style scoped>
textarea {
  resize: vertical;
}


@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>

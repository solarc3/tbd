<template>
  <div class="fixed inset-0 bg-gray-800 bg-opacity-30 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg relative">
      <h2 class="text-xl font-bold mb-4">{{ isEdit ? "Editar Tarea" : "Crear Tarea" }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1">Título</label>
          <input v-model="localTask.title" type="text" class="input input-bordered w-full" required />
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1">Descripción</label>
          <textarea v-model="localTask.description" class="textarea textarea-bordered w-full" required></textarea>
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1">Fecha de Vencimiento</label>
          <input v-model="localTask.dueDate" type="date" class="input input-bordered w-full" required />
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1">Sector</label>
          <input v-model="localTask.sector" type="text" class="input input-bordered w-full" required />
        </div>
        <div class="flex justify-end gap-2">
          <button type="button" @click="$emit('close')" class="btn btn-secondary">Cancelar</button>
          <button type="submit" class="btn btn-primary">Guardar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import type { PropType } from "vue";

interface Task {
  id: number;
  title: string;
  description: string;
  dueDate: string;
  sector: string;
  completed: boolean;
}

export default defineComponent({
  props: {
    task: {
      type: Object as PropType<Task>,
      default: () => ({
        title: "",
        description: "",
        dueDate: "",
        sector: "",
      }),
    },
    isEdit: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["close", "save"],
  setup(props, { emit }) {
    const localTask = ref({ ...props.task });

    const handleSubmit = () => {
      emit("save", localTask.value);
    };

    return { localTask, handleSubmit };
  },
});
</script>

<style scoped>
/* para generar como el borroso en el forms */
.fixed {
  backdrop-filter: blur(4px);
  background-color: rgba(0, 0, 0, 0.3);
}
</style>
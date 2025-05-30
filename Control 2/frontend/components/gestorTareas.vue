<template>
  <div>
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg relative">
      <h2 class="text-xl font-bold mb-4">{{ isEdit ? "Editar Estado de Tarea" : "Crear Tarea" }}</h2>
      <form @submit.prevent="handleSubmit">
        <div v-if="!isEdit">
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Título</label>
            <input v-model="localTask.title" type="text" class="input input-bordered w-full" required>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Descripción</label>
            <textarea v-model="localTask.description" class="textarea textarea-bordered w-full" required />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Fecha de Vencimiento</label>
            <input v-model="localTask.dueDate" type="date" class="input input-bordered w-full" required>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Sector</label>
            <input v-model="localTask.sector" type="text" class="input input-bordered w-full" required>
          </div>
        </div>
        <div v-else>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Estado</label>
            <select v-model="localTask.estado" class="select select-bordered w-full" required>
              <option value="PENDIENTE">Pendiente</option>
              <option value="EN_PROGRESO">En Progreso</option>
              <option value="COMPLETADA">Completada</option>
            </select>
          </div>
        </div>
        <div class="flex justify-end gap-2">
          <button class="btn btn-secondary" type="button" @click="$emit('close')">Cancelar</button>
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
  estado: string;
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
    const localTask = ref({ ...props.task });

    const handleSubmit = () => {
      emit("save", localTask.value);
    };

    return { localTask, handleSubmit };
  },
});
</script>

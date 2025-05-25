<template>
  <div class="p-6">
    <h2 class="text-xl font-bold mb-4">Crear Nueva Tarea</h2>
    <form @submit.prevent="handleSubmit">
      <div class="mb-4">
        <label class="block text-sm font-medium mb-1">Título</label>
        <input v-model="task.titulo" type="text" class="input input-bordered w-full" required />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium mb-1">Descripción</label>
        <textarea v-model="task.descripcion" class="textarea textarea-bordered w-full" required></textarea>
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium mb-1">Fecha de Vencimiento</label>
        <input v-model="task.fechaVencimiento" type="datetime-local" class="input input-bordered w-full" required />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium mb-1">Sector</label>
        <select v-model="task.idSector" class="select select-bordered w-full" required>
          <option v-for="sector in sectores" :key="sector.id" :value="sector.id">
            {{ sector.nombreSector }}
          </option>
        </select>
      </div>
      <div class="flex justify-end gap-2">
        <button type="button" @click="$emit('close')" class="btn btn-secondary">Cancelar</button>
        <button type="submit" class="btn btn-primary">Guardar</button>
      </div>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from "vue";
import SectorService from "@/api/services/sectorService";
import TareaService from "@/api/services/tareaService";
import { useAuthStore } from "@/stores/auth";

export default defineComponent({
  emits: ["close", "save"],
  setup(_, { emit }) {
    const authStore = useAuthStore();
    const sectores = ref<{ id: number; nombreSector: string }[]>([]);
    const task = ref({
      titulo: "",
      descripcion: "",
      fechaVencimiento: "",
      idSector: null,
      idUsuario: authStore.currentUser?.id || null,
      estado: "PENDIENTE",
    });

    const fetchSectores = async () => {
      try {
        sectores.value = await SectorService.getAllSectores();
      } catch (error) {
        console.error("Error al cargar los sectores:", error);
      }
    };

    const handleSubmit = async () => {
      try {
        if (!task.value.idUsuario) {
          console.error("El idUsuario no está definido.");
          return;
        }
        if (!task.value.idSector) {
          console.error("El idSector no está definido.");
          return;
        }

        await TareaService.createTarea({
          ...task.value,
          idUsuario: task.value.idUsuario,
          idSector: task.value.idSector,
        });
        emit("save", task.value);
      } catch (error) {
        console.error("Error al crear la tarea:", error);
      }
    };

    onMounted(() => {
      fetchSectores();
    });

    return { task, sectores, handleSubmit };
  },
});
</script>
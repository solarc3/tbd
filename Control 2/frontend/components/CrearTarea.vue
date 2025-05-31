<template>
  <div>
    <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg relative">
      <h2 class="text-xl font-bold mb-4 text-gray-800 border-b pb-2">{{ isEdit ? "Editar Tarea" : "Crear Nueva Tarea" }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1 text-gray-700">Título</label>
          <input 
            v-model="localTask.titulo" 
            type="text" 
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
            required
          >
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1 text-gray-700">Descripción</label>
          <textarea 
            v-model="localTask.descripcion" 
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 min-h-[100px]" 
            required
          />
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1 text-gray-700">Fecha de Vencimiento</label>
            <input 
              v-model="localTask.fechaVencimiento" 
              type="datetime-local" 
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
              required
            >
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1 text-gray-700">Sector</label>
            <select 
              v-model="localTask.idSector" 
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
              required
            >
              <option value="" disabled selected>Seleccione un sector</option>
              <option v-for="sector in sectores" :key="sector.id" :value="sector.id">
                {{ sector.nombreSector }}
              </option>
            </select>
          </div>
        </div>
        
        <!-- Campo de estado solo visible en modo edición -->
        <div v-if="isEdit" class="mb-4">
          <label class="block text-sm font-medium mb-1 text-gray-700">Estado</label>
          <select 
            v-model="localTask.estado" 
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
            required
          >
            <option value="PENDIENTE">Pendiente</option>
            <option value="EN_PROGRESO">En Progreso</option>
            <option value="COMPLETADA">Completada</option>
          </select>
        </div>
        
        <div class="flex justify-end gap-3 mt-6 pt-4 border-t">
          <button 
            class="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 transition font-medium" 
            type="button" 
            @click="$emit('close')"
          >
            Cancelar
          </button>
          <button 
            type="submit" 
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition font-medium"
          >
            Guardar
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, watch } from "vue";
import type { PropType } from "vue";
import SectorService from "@/api/services/sectorService";
import { useAuthStore } from "@/stores/auth";

interface TaskInput {
  id?: number;
  titulo: string;
  descripcion: string;
  fechaVencimiento: string;
  idSector: number | null;
  idUsuario: number | null;
  estado: string;
}

export default defineComponent({
  props: {
    task: {
      type: Object as PropType<Record<string, unknown>>,
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
    const authStore = useAuthStore();
    const sectores = ref<{ id: number; nombreSector: string }[]>([]);
    
    // Initialize local task with default values
    const defaultTask = {
      id: 0,
      titulo: "",
      descripcion: "",
      fechaVencimiento: "",
      idSector: null as number | null,
      idUsuario: authStore.currentUser?.id || null,
      estado: "PENDIENTE",
    };
    
    const localTask = ref<TaskInput>({...defaultTask});
    
    const mapTaskToLocalFormat = (inputTask: Record<string, unknown>) => {
      if (props.isEdit) {
        return {
          id: inputTask.id || 0,
          titulo: inputTask.title || "",
          descripcion: inputTask.description || "",
          fechaVencimiento: inputTask.dueDate || "",
          idSector: inputTask.sector ? parseInt(inputTask.sector) : null,
          idUsuario: authStore.currentUser?.id || null,
          estado: inputTask.estado || "PENDIENTE",
        };
      } else {
        return {...defaultTask};
      }
    };
    
    // Initialize local task based on props
    localTask.value = mapTaskToLocalFormat(props.task);
    
    // Watch for changes to props.task and update localTask accordingly
    watch(() => props.task, (newTask) => {
      localTask.value = mapTaskToLocalFormat(newTask);
    }, { deep: true });

    const fetchSectores = async () => {
      try {
        sectores.value = await SectorService.getAllSectores();
      } catch (error) {
        console.error("Error al cargar los sectores:", error);
      }
    };

    const handleSubmit = async () => {
      try {
        // Validación de campos requeridos
        if (!localTask.value.titulo || !localTask.value.descripcion) {
          alert('Por favor, complete todos los campos requeridos.');
          return;
        }

        if (!localTask.value.idSector) {
          alert('Por favor, seleccione un sector.');
          return;
        }
        
        // Map back to the format expected by the parent component
        const taskToEmit = props.isEdit ? {
          id: localTask.value.id,
          title: localTask.value.titulo,
          description: localTask.value.descripcion,
          dueDate: localTask.value.fechaVencimiento,
          sector: localTask.value.idSector?.toString(),
          estado: localTask.value.estado
        } : localTask.value;
        
        // Emit save event with the task data
        emit("save", taskToEmit);
      } catch (error) {
        console.error("Error al guardar la tarea:", error);
      }
    };

    onMounted(() => {
      fetchSectores();
    });

    return { localTask, sectores, handleSubmit };
  },
});
</script>

<style scoped>
textarea {
  resize: vertical;
}

input, select, textarea {
  transition: border-color 0.2s, box-shadow 0.2s;
}

.bg-white {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
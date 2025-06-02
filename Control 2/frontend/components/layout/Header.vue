<template>
  <header class="border-b bg-[var(--primary)] text-white">
    <nav class="mx-auto flex justify-between items-center px-6 py-3">
      <div class="flex items-center space-x-4">
        <button class="text-white hover:underline flex items-center" @click="router.push('/')">
          <House class="w-5 h-5 mr-2" />
          <span>Home</span>
        </button>
        <button class="text-white hover:underline" @click="router.push('/gestor')">Gestor</button>
        <button class="text-white hover:underline" @click="router.push('/estadisticas')">Estadísticas</button>
      </div>

      <ClientOnly>
        <div v-if="authStore.authenticated" class="flex items-center gap-4">
          <!-- Notification Bell Icon -->
          <div class="relative">
            <button
                @click="(event) => toggleNotifications(event)"
                class="relative flex items-center text-white hover:text-blue-200"
                ref="notificationButton"
            >
              <Bell class="w-6 h-6" />
              <span
                  v-if="notificationCount > 0"
                  class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center"
              >
                {{ notificationCount }}
              </span>
            </button>

            <!-- Notification Panel -->
            <div
                v-if="showNotifications"
                class="absolute right-0 mt-2 z-50"
                ref="notificationsPanel"
            >
              <NotificationPanel @close="showNotifications = false" />
            </div>
          </div>

          <div class="relative flex items-center">
            <button
                @click="router.push('/carrito')"
                class="relative flex items-center"
            >
              <ShoppingCart class="w-6 h-6 text-white" />
              <span
                  v-if="totalCartItems > 0"
                  class="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center"
              >
                {{ totalCartItems }}
              </span>
            </button>
          </div>

          <span class="text-sm font-medium">{{
              authStore.currentUser?.username
            }}</span>

          <button
              class="bg-blue-500 text-white flex items-center px-3 py-2 space-x-2 hover:bg-blue-600"
              @click="router.push('/profile')"
          >
            <User class="h-5 w-5" />
          </button>

          <button
              class="bg-red-500 text-white flex items-center px-3 py-2 space-x-2 hover:bg-red-600"
              @click="handleLogout"
          >
            <LogOut class="h-5 w-5" />
          </button>
        </div>

        <div v-else>
          <button
              class="flex items-center space-x-2 bg-[var(--primary)] drop-shadow-black text-white px-4 py-2 transition-colors duration-200 hover:bg-[var(--secondary)]/20"
              @click="router.push('/login')"
          >
            <LogIn class="h-5 w-5" />
            <span>Ingresar</span>
          </button>
        </div>
      </ClientOnly>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { LogIn, LogOut, House, User, ShoppingCart, Bell } from "lucide-vue-next";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import { computed, ref, onMounted, onBeforeUnmount } from "vue";
import { useCartStore } from "@/stores/cartStore";
import NotificationPanel from "@/components/NotificationPanel.vue";
import TareaService from "@/api/services/tareaService";

const authStore = useAuthStore();
const router = useRouter();
const cartStore = useCartStore();

const totalCartItems = computed(() =>
    cartStore.items.reduce((total, item) => total + item.cantidad, 0)
);

// Notification system
const showNotifications = ref(false);
const notificationCount = ref(0);
const notificationsPanel = ref<HTMLElement | null>(null);
const checkInterval = ref<number | null>(null);

const toggleNotifications = (event: MouseEvent) => {
  event.stopPropagation();
  showNotifications.value = !showNotifications.value;
};

const fetchNotificationCount = async () => {
  if (authStore.authenticated && authStore.currentUser?.id) {
    try {
      const tareas = await TareaService.getTareasPorVencerHoy(authStore.currentUser.id);
      notificationCount.value = tareas.length;
    } catch (error) {
      console.error("Error al obtener notificaciones:", error);
    }
  }
};

const notificationButton = ref<HTMLElement | null>(null);

const handleClickOutside = (event: MouseEvent) => {
  if (
      showNotifications.value &&
      notificationsPanel.value &&
      !notificationsPanel.value.contains(event.target as Node) &&
      !notificationButton.value?.contains(event.target as Node)
  ) {
    showNotifications.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
  fetchNotificationCount();
  // Check for new notifications every 5 minutes
  checkInterval.value = window.setInterval(fetchNotificationCount, 300000);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
  if (checkInterval.value) {
    clearInterval(checkInterval.value);
  }
});

async function handleLogout() {
  try {
    await authStore.logout();
    router.push("/login");
  } catch (err) {
    console.error("Logout error:", err);
  }
}
</script>
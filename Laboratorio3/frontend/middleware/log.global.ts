import { useAuthStore } from '@/stores/auth';
import apiClient from '@/api/axios';

export default defineNuxtRouteMiddleware(async (to, from) => {
  if (import.meta.server) {
    return;
  }

  const authStore = useAuthStore();
  const user = authStore.currentUser;

  if (user && user.id) {
    const payload = {
      from: from.fullPath,
      to: to.fullPath,
    };
    apiClient.post('/log', payload).catch(error => {
      console.error('Error logging navigation:', error);
    });
  }
});
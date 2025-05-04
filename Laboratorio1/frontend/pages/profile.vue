<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter }          from '#app'
import { Button }             from '@/components/ui/button'
import { useAuthStore }       from '@/stores/auth'

// 1. Controlamos renderizado sólo en cliente
const shouldRender = ref(false)
onMounted(() => {
  shouldRender.value = true
})

const authStore = useAuthStore()
const router    = useRouter()

// 2. Iniciales de usuario
const userInitials = computed(() => {
  const username = authStore.currentUser?.username || ''
  return username.slice(0, 2).toUpperCase()
})

// 3. Logout y redirección
async function logoutUser() {
  await authStore.logout()
  router.push('/login')
}
</script>

<template>
  <ClientOnly>
    <div v-if="shouldRender" class="container mx-auto px-4 py-8">
      <div class="max-w-md mx-auto bg-white rounded-lg shadow-md overflow-hidden">
        <div class="p-6">

          <h1 class="text-2xl font-bold mb-4">User Profile</h1>
          <div v-if="authStore.authenticated" class="space-y-4">
            <div class="flex items-center p-4 bg-gray-50 rounded-md">
              <div
                  class="h-12 w-12 rounded-full bg-blue-500 flex items-center justify-center text-white font-bold text-xl"
              >
                {{ userInitials }}
              </div>
              <div class="ml-4">
                <h2 class="text-lg font-semibold">{{ authStore.currentUser?.username }}</h2>
                <p class="text-gray-600">{{ authStore.currentUser?.email }}</p>
              </div>
            </div>

            <div class="border-t pt-4">
              <h3 class="text-lg font-medium mb-2">Account Details</h3>
              <div class="grid grid-cols-2 gap-2 text-sm">
                <div class="text-gray-500">Username:</div>
                <div>{{ authStore.currentUser?.username }}</div>
                <div class="text-gray-500">Email:</div>
                <div>{{ authStore.currentUser?.email }}</div>
                <div class="text-gray-500">User ID:</div>
                <div>{{ authStore.currentUser?.id }}</div>
              </div>
            </div>

            <div class="border-t pt-4">
              <Button variant="destructive" class="w-full" @click="logoutUser">
                Logout
              </Button>
            </div>
          </div>

          <div v-else class="text-center py-8">
            <p class="text-gray-500 mb-4">Please login to view your profile</p>
            <Button @click="router.push('/login')">Login</Button>
          </div>

        </div>
      </div>
    </div>
  </ClientOnly>
</template>

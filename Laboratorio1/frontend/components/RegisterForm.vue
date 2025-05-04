<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const error = ref('')
const success = ref('')

const form = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const isLoading = ref(false)

const handleRegister = async () => {
  error.value = ''
  success.value = ''
  
  if (!form.value.firstName || !form.value.email || !form.value.password) {
    error.value = 'Por favor completa todos los campos requeridos'
    return
  }
  
  if (form.value.password !== form.value.confirmPassword) {
    error.value = 'Las contraseñas no coinciden'
    return
  }
  
  try {
    isLoading.value = true
    // Create username from first name (you could use first+last if needed)
    const username = form.value.firstName.toLowerCase()
    
    await authStore.register(
      username,
      form.value.email,
      form.value.password
    )
    
    success.value = 'Cuenta creada exitosamente!'
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (err: any) {
    console.error('Register error:', err)
    error.value = err.response?.data?.message || 'Error al crear la cuenta'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <Card class="mx-auto max-w-sm">
    <CardHeader>
      <CardTitle class="text-xl">
        Sign Up
      </CardTitle>
      <CardDescription>
        Enter your information to create an account
      </CardDescription>
    </CardHeader>
    <CardContent>
      <!-- Error and success messages -->
      <div v-if="error" class="mb-4 p-3 rounded-md bg-red-50 text-red-600 text-sm">
        {{ error }}
      </div>
      <div v-if="success" class="mb-4 p-3 rounded-md bg-green-50 text-green-600 text-sm">
        {{ success }}
      </div>
      
      <form @submit.prevent="handleRegister" class="grid gap-4">
        <div class="grid grid-cols-2 gap-4">
          <div class="grid gap-2">
            <Label for="first-name">First name</Label>
            <Input id="first-name" v-model="form.firstName" placeholder="Max" required />
          </div>
          <div class="grid gap-2">
            <Label for="last-name">Last name</Label>
            <Input id="last-name" v-model="form.lastName" placeholder="Robinson" />
          </div>
        </div>
        <div class="grid gap-2">
          <Label for="email">Email</Label>
          <Input
              id="email"
              type="email"
              v-model="form.email"
              placeholder="m@example.com"
              required
          />
        </div>
        <div class="grid gap-2">
          <Label for="password">Password</Label>
          <Input id="password" type="password" v-model="form.password" required />
        </div>
        <div class="grid gap-2">
          <Label for="confirm-password">Confirm Password</Label>
          <Input id="confirm-password" type="password" v-model="form.confirmPassword" required />
        </div>
        <Button 
          type="submit" 
          class="w-full bg-[var(--primary)] transition-colors duration-200 ease-in-out hover:bg-[var(--secondary)]/90"
          :disabled="isLoading"
        >
          {{ isLoading ? 'Creating account...' : 'Create an account' }}
        </Button>
      </form>
      <div class="mt-4 text-center text-sm">
        Already have an account?
        <a href="/login" class="underline">
          Sign in
        </a>
      </div>
    </CardContent>
  </Card>
</template>

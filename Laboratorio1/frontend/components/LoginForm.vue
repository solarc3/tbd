<script lang="ts">
export const containerClass = 'w-full min-h-screen flex items-center justify-center px-4 py-8 bg-gray-50'
</script>

<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
</script>

<template>
  <div :class="containerClass">
    <div class="w-full max-w-sm sm:max-w-md md:max-w-lg lg:max-w-xl">
      <Card>
        <CardHeader>
          <CardTitle class="text-2xl">Login</CardTitle>
          <CardDescription>Ingresa tu correo y contrasenha para entrar a tu cuenta</CardDescription>
        </CardHeader>
        <CardContent>
          <div class="grid gap-4">
            <div class="grid gap-2">
              <Label for="email">Email</Label>
              <Input id="email" type="email" placeholder="m@example.com" required />
            </div>
            <div class="grid gap-2">
              <div class="flex items-center">
                <Label for="password">Password</Label>
                <a href="#" class="ml-auto text-sm underline">Olvidaste tu contrasenha?</a>
              </div>
              <Input id="password" type="password" required />
            </div>
            <Button type="submit" class="w-full bg-[var(--primary)] transition-colors duration-200 ease-in-out
                 hover:bg-[var(--secondary)]/20">Login</Button>
          </div>
          <div class="mt-4 text-center text-sm">
            No tienes una cuenta aun?
            <a href="/register" class="underline">Registrate</a>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

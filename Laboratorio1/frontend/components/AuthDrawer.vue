<script setup lang="ts">
import { ref } from 'vue'
import { cn } from '@/lib/utils'
import {
  Drawer,
  DrawerTrigger,
  DrawerContent,
  DrawerHeader,
  DrawerTitle,
  DrawerDescription,
  DrawerFooter,
  DrawerClose
} from '../src/components/ui/drawer'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

const isOpen = ref(false)
const view = ref<'login' | 'register'>('login')

function switchToRegister() {
  view.value = 'register'
}

function switchToLogin() {
  view.value = 'login'
}
</script>

<template>
  <Drawer v-model:open="isOpen" direction="bottom">
    <DrawerTrigger>
      <slot>
        <Button variant="outline">Login</Button>
      </slot>
    </DrawerTrigger>

    <DrawerContent class="bg-white text-black w-full">
      <div class="bg-muted mx-auto mt-4 h-2 w-[100px] rounded-full" />
      <div class="container mx-auto px-4 sm:px-6 md:max-w-2xl">
        <DrawerHeader>
          <DrawerTitle>{{ view === 'login' ? 'Login to your account' : 'Create an account' }}</DrawerTitle>
          <DrawerDescription>
            {{ view === 'login'
              ? 'Enter your email below to login to your account'
              : 'Enter your information to create a new account' }}
          </DrawerDescription>
        </DrawerHeader>

        <div class="px-0 sm:px-4">
          <form v-if="view === 'login'" @submit.prevent class="flex flex-col gap-6">
            <div class="grid gap-3">
              <Label for="email">Email</Label>
              <Input
                  id="email"
                  type="email"
                  placeholder="m@example.com"
                  required
              />
            </div>
            <div class="grid gap-3">
              <div class="flex items-center">
                <Label for="password">Password</Label>
                <a
                    href="#"
                    class="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                >
                  Forgot your password?
                </a>
              </div>
              <Input id="password" type="password" required />
            </div>
            <div class="flex flex-col gap-3">
              <Button type="submit" class="w-full bg-[var(--primary)] text-white">
                Login
              </Button>
            </div>
          </form>
          <form v-else @submit.prevent class="flex flex-col gap-6">
            <div class="grid gap-3">
              <Label for="register-name">Name</Label>
              <Input
                  id="register-name"
                  type="text"
                  placeholder="John Doe"
                  required
              />
            </div>
            <div class="grid gap-3">
              <Label for="register-email">Email</Label>
              <Input
                  id="register-email"
                  type="email"
                  placeholder="m@example.com"
                  required
              />
            </div>
            <div class="grid gap-3">
              <Label for="register-password">Password</Label>
              <Input id="register-password" type="password" required />
            </div>
            <div class="grid gap-3">
              <Label for="register-confirm">Confirm Password</Label>
              <Input id="register-confirm" type="password" required />
            </div>
            <div class="flex flex-col gap-3">
              <Button type="submit" class="w-full">
                Register
              </Button>
            </div>
          </form>
        </div>

        <div class="mt-4 pb-4 text-center text-sm">
          <span v-if="view === 'login'">
            Don't have an account?
            <Button variant="link" class="p-0 underline-offset-4 hover:underline" @click="navigateTo('/register')">
              Sign up
            </Button>
          </span>
          <span v-else>
            Already have an account?
            <Button variant="link" class="p-0 underline-offset-4" @click="switchToLogin">
              Login
            </Button>
          </span>
        </div>


      </div>
    </DrawerContent>
  </Drawer>
</template>
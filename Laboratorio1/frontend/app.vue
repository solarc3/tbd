<template>
	<NuxtLayout>
		<NuxtPage />
		<Toaster position="top-right" richColors closeButton/>
	</NuxtLayout>
</template>

<script setup lang="ts">
import { Toaster } from "vue-sonner";
</script>
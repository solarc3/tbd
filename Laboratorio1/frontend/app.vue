<template>
	<NuxtLayout>
		<NuxtPage />
		<Toaster position="top-right" richColors />
	</NuxtLayout>
</template>

<script setup lang="ts">
import { Toaster } from "vue-sonner";
</script>
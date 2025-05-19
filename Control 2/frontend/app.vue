<template>
	<Suspense>
		<template #default>
			<AuthInitializer>
				<NuxtLayout>
					<NuxtPage />
					<Toaster position="top-right" richColors closeButton />
				</NuxtLayout>
			</AuthInitializer>
		</template>
		<template #fallback>
			<div class="flex items-center justify-center min-h-screen">
				<span class="loading loading-spinner loading-lg"></span>
			</div>
		</template>
	</Suspense>
</template>

<script setup lang="ts">
import { Toaster } from "vue-sonner";
</script>

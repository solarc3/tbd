<template>
	<slot></slot>
</template>

<script setup lang="ts">
import { useAuthStore } from "@/stores/auth";

const auth = useAuthStore();
await auth.initAuth();
</script>

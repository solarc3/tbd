<template>
  <div>
    <Header />
    <main>
      <slot />
    </main>
  </div>
</template>

<script setup lang="ts">
import Header from "~/components/layout/Header.vue";
</script>
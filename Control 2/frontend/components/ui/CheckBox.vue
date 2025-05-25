<template>
  <input type="checkbox" v-bind="$attrs" v-on="$listeners" />
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "Checkbox",
});
</script>
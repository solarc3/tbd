<script setup lang="ts">
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from '@/components/ui/tabs'
</script>

<template>
  <div class="w-full p-6">
    <Tabs default-value="primero" class="w-full">
      <TabsList class="grid w-full grid-cols-3 h-10 bg-muted p-1 rounded-md">
        <TabsTrigger value="primero" class="rounded-sm">
          Primero
        </TabsTrigger>
        <TabsTrigger value="segundo" class="rounded-sm">
          Segundo
        </TabsTrigger>
        <TabsTrigger value="tercero" class="rounded-sm">
          Tercero
        </TabsTrigger>
      </TabsList>

      <TabsContent value="primero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <h3 class="text-lg font-semibold mb-3 text-foreground">Primera Sección</h3>
        <p class="text-muted-foreground leading-relaxed">
          Contenido de la pestaña PRIMERO. Aquí puedes agregar cualquier información
          relevante para esta sección.
        </p>
      </TabsContent>

      <TabsContent value="segundo" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <h3 class="text-lg font-semibold mb-3 text-foreground">Segunda Sección</h3>
        <p class="text-muted-foreground leading-relaxed">
          Contenido de la pestaña SEGUNDO. Esta área está diseñada para mostrar
          información específica de la segunda pestaña.
        </p>
      </TabsContent>

      <TabsContent value="tercero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <h3 class="text-lg font-semibold mb-3 text-foreground">Tercera Sección</h3>
        <p class="text-muted-foreground leading-relaxed">
          Contenido de la pestaña TERCERO. Aquí se muestra el contenido correspondiente
          a la última pestaña disponible.
        </p>
      </TabsContent>
    </Tabs>
  </div>
</template>

<style scoped>
/* Estilos adicionales si necesitas personalización extra */
</style>
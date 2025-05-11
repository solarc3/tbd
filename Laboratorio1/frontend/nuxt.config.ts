import { defineNuxtConfig } from "nuxt/config";
import tailwindcss from "@tailwindcss/vite";

export default defineNuxtConfig({
	compatibilityDate: "2024-11-01",
	devtools: { enabled: true },
	css: ["~/assets/css/tailwind.css"],
	plugins: [{ src: "~/plugins/init-auth.ts" }],
	vite: {
		plugins: [tailwindcss()],
		server: { allowedHosts: true },
	},
	modules: [
		"@nuxt/content",
		"@nuxt/eslint",
		"@nuxt/fonts",
		"@nuxt/icon",
		"@nuxt/image",
		"@nuxt/scripts",
		"@nuxt/test-utils",
		"@nuxt/ui",
		"@nuxtjs/color-mode",
		"shadcn-nuxt",
		"@pinia/nuxt",
	],
	pinia: {
		autoImports: ["defineStore", "storeToRefs"],
	},
	colorMode: { classSuffix: "" },
	shadcn: {
		prefix: "",
		componentDir: "./components/ui",
	},
});
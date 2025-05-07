import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import authService from './api/services/authService'


// Initialize authentication state
authService.initAuthFromSession();

const app = createApp(App)

app.use(router)

app.mount('#app')

// Save auth state to sessionStorage when page is unloaded
window.addEventListener('beforeunload', () => {
  authService.saveAuthToSession();
});

// Listen for auth logout events
window.addEventListener('auth:logout', () => {
  console.log('Auth logout event detected');
  // Optionally redirect to login page or show a message
  // router.push('/login');
});
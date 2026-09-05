/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        cyber: {
          bg: '#0A0A12',
          surface: '#12121E',
          'surface-variant': '#1A1A2E',
          border: '#2A2A48',
        },
        neon: {
          cyan: '#00F5FF',
          magenta: '#FF007A',
          violet: '#8B00FF',
          green: '#00FF87',
          amber: '#FFB800',
        }
      }
    },
  },
  plugins: [],
}

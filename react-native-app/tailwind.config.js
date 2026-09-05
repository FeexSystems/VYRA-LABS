/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./App.{js,jsx,ts,tsx}",
    "./src/**/*.{js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        cyber: {
          bg: "#020205", // Deep OLED Black
          card: "rgba(15, 15, 25, 0.6)", // Frosted glass surface
          teal: "#00FFCC", // Electric Teal
          amber: "#FF9900", // Warm Amber Accent
          magenta: "#FF007A", // Cyberpunk neon pink/magenta
          violet: "#8B00FF", // Quantum Violet
          gray: "#1a1a2e", // Medium dark base
          text: "#FFFFFF",
          subtext: "#A0A0C0"
        }
      },
      fontFamily: {
        denton: ["Denton", "serif"],
        inter: ["Inter", "sans-serif"]
      }
    },
  },
  plugins: [],
}

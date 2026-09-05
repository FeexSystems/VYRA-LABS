// Shared design system tokens for VYRA platform
// Used by both native (Android) and web platforms

export const designTokens: DesignTokens = {
  colors: {
    primary: '#00F5FF',
    secondary: '#FF007A',
    accent: '#8B00FF',
    background: '#0A0A12',
    surface: '#12121E',
    'surface-variant': '#1A1A2E',
    border: '#2A2A48',
    text: {
      primary: '#FFFFFF',
      secondary: '#A0A0B0',
      muted: '#606070'
    }
  },
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32
  },
  typography: {
    fontSize: {
      xs: 12,
      sm: 14,
      md: 16,
      lg: 18,
      xl: 24
    },
    fontWeight: {
      normal: 400,
      medium: 500,
      bold: 700
    }
  },
  borderRadius: {
    sm: 4,
    md: 8,
    lg: 12,
    xl: 16
  }
} as const;

// Cyberpunk-specific color palette
export const cyberpunkColors = {
  neonCyan: '#00F5FF',
  neonMagenta: '#FF007A',
  neonViolet: '#8B00FF',
  neonGreen: '#00FF87',
  neonAmber: '#FFB800',
  cyberBg: '#0A0A12',
  cyberSurface: '#12121E',
  cyberSurfaceVariant: '#1A1A2E',
  cyberBorder: '#2A2A48'
} as const;

// Animation durations
export const animationDurations = {
  fast: 150,
  normal: 300,
  slow: 500
} as const;

// Easing functions
export const easingFunctions = {
  easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
  easeOut: 'cubic-bezier(0, 0, 0.2, 1)',
  easeInOut: 'cubic-bezier(0.4, 0, 0.2, 1)'
} as const;

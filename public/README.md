# VYRA Public Assets

This folder contains all public assets for the VYRA AI-Powered Creator Platform.

## Brand Assets

### Logos and Icons
- **`vyra-logo.svg`** - Main VYRA logo with cyberpunk aesthetic (200x80px)
- **`favicon.svg`** - Primary favicon for browser tabs (32x32px)
- **`favicon-16x16.svg`** - Small favicon for compatibility (16x16px)
- **`apple-touch-icon.svg`** - iOS home screen icon (180x180px)

### Social Media
- **`og-image.svg`** - Open Graph image for social media sharing (1200x630px)

## Configuration Files

### SEO and Search Engines
- **`robots.txt`** - Search engine crawling instructions
  - Allows all major search engines
  - Blocks sensitive directories (/api/, /admin/, /_debug/, /test/)
  - Includes sitemap reference

### Progressive Web App (PWA)
- **`manifest.json`** - PWA configuration
  - App name: "VYRA - AI-Powered Creator Platform"
  - Theme colors: Gold (#FFD700) and Dark (#0A0A0A)
  - Shortcuts to Dashboard and Chat
  - Full standalone display mode

## Design System

### Color Palette
- **Primary Gold**: #FFD700 (brand primary)
- **Orange Accent**: #FF8C00 (gradients)
- **Cyan Neon**: #00FFFF (cyberpunk accents)
- **Dark Background**: #0A0A0A (main background)

### Typography
- **Font Family**: Arial, sans-serif (system fonts for compatibility)
- **Brand Font Weight**: Bold for logos, Normal for descriptions

### Visual Style
- **Cyberpunk aesthetic** with neon glows and circuit patterns
- **Gradient effects** using gold-to-orange transitions
- **Rounded corners** for modern iOS/Android compatibility
- **Filter effects** for neon glow appearance

## Usage in HTML

These assets are automatically referenced in `index.html`:

```html
<!-- Favicon and App Icons -->
<link rel="icon" type="image/svg+xml" href="/favicon.svg" />
<link rel="apple-touch-icon" href="/apple-touch-icon.svg" />

<!-- PWA Manifest -->
<link rel="manifest" href="/manifest.json" />

<!-- Social Media -->
<meta property="og:image" content="/og-image.svg" />
<meta name="twitter:image" content="/og-image.svg" />
```

## File Formats

All assets use **SVG format** for:
- **Scalability**: Vector graphics work at any resolution
- **Performance**: Small file sizes
- **Flexibility**: Easy to modify colors and effects
- **Compatibility**: Wide browser support

## Brand Guidelines

When using these assets:
1. Maintain the gold (#FFD700) primary color
2. Keep the cyberpunk aesthetic with neon accents
3. Use the glow effects for digital presentations
4. Ensure sufficient contrast for accessibility
5. Keep the "V" letterform recognizable in icons
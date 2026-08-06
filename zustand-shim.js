// Zustand compatibility shim for @react-three/fiber
// @react-three/fiber expects a default export from zustand (legacy behavior)
// This shim provides both named and default exports for compatibility

// Import the actual create function from zustand
import zustandCreate from 'zustand';

// Re-export everything from zustand
export * from 'zustand';

// Also export create as named export
export { zustandCreate as create };

// Provide the default export that @react-three/fiber expects
export default zustandCreate;

import React, { useRef, useMemo } from 'react';
import { View, StyleSheet, Platform } from 'react-native';
import { Canvas, useFrame } from '@react-three/fiber';
import * as THREE from 'three';

function GlobeWireframe() {
  const meshRef = useRef();
  const pointsRef = useRef();

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.y += 0.003;
      meshRef.current.rotation.x = Math.sin(state.clock.elapsedTime * 0.3) * 0.1;
    }
    if (pointsRef.current) {
      pointsRef.current.rotation.y += 0.003;
      pointsRef.current.rotation.x = Math.sin(state.clock.elapsedTime * 0.3) * 0.1;
    }
  });

  const dotPositions = useMemo(() => {
    const positions = [];
    const count = 200;
    for (let i = 0; i < count; i++) {
      const phi = Math.acos(-1 + (2 * i) / count);
      const theta = Math.sqrt(count * Math.PI) * phi;
      const x = 1.52 * Math.cos(theta) * Math.sin(phi);
      const y = 1.52 * Math.sin(theta) * Math.sin(phi);
      const z = 1.52 * Math.cos(phi);
      positions.push(x, y, z);
    }
    return new Float32Array(positions);
  }, []);

  return (
    <group>
      {/* Wireframe sphere */}
      <mesh ref={meshRef}>
        <sphereGeometry args={[1.5, 24, 24]} />
        <meshBasicMaterial
          color="#6C63FF"
          wireframe
          transparent
          opacity={0.15}
        />
      </mesh>

      {/* Surface dots */}
      <points ref={pointsRef}>
        <bufferGeometry>
          <bufferAttribute
            attach="attributes-position"
            count={dotPositions.length / 3}
            array={dotPositions}
            itemSize={3}
          />
        </bufferGeometry>
        <pointsMaterial
          color="#00D4FF"
          size={0.03}
          transparent
          opacity={0.8}
          sizeAttenuation
        />
      </points>

      {/* Inner glow sphere */}
      <mesh>
        <sphereGeometry args={[1.48, 32, 32]} />
        <meshBasicMaterial
          color="#6C63FF"
          transparent
          opacity={0.05}
        />
      </mesh>
    </group>
  );
}

function GlobeScene() {
  return (
    <Canvas
      camera={{ position: [0, 0, 4], fov: 45 }}
      style={{ background: 'transparent' }}
      gl={{ alpha: true, antialias: true }}
    >
      <ambientLight intensity={0.5} />
      <pointLight position={[10, 10, 10]} intensity={0.8} color="#6C63FF" />
      <pointLight position={[-10, -10, -10]} intensity={0.4} color="#00D4FF" />
      <GlobeWireframe />
    </Canvas>
  );
}

export default function Globe3D({ size = 300 }) {
  if (Platform.OS !== 'web') {
    return <View style={[styles.fallback, { width: size, height: size }]} />;
  }

  return (
    <View style={[styles.container, { width: size, height: size }]}>
      <GlobeScene />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    overflow: 'hidden',
  },
  fallback: {
    borderRadius: 150,
    backgroundColor: 'rgba(108,99,255,0.1)',
    borderWidth: 1,
    borderColor: 'rgba(108,99,255,0.3)',
  },
});

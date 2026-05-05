import React, { useEffect, useRef } from 'react';
import { View, Animated, Dimensions, StyleSheet } from 'react-native';

const { width, height } = Dimensions.get('window');
const PARTICLE_COUNT = 30;

function Particle({ delay }) {
  const opacity = useRef(new Animated.Value(0)).current;
  const translateY = useRef(new Animated.Value(height + 20)).current;
  const translateX = useRef(new Animated.Value(Math.random() * width)).current;
  const scale = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    const startAnimation = () => {
      translateY.setValue(height + 20);
      translateX.setValue(Math.random() * width);
      opacity.setValue(0);
      scale.setValue(0);

      Animated.parallel([
        Animated.timing(translateY, {
          toValue: -20,
          duration: 8000 + Math.random() * 6000,
          useNativeDriver: true,
        }),
        Animated.sequence([
          Animated.timing(opacity, {
            toValue: 0.6 + Math.random() * 0.4,
            duration: 1500,
            useNativeDriver: true,
          }),
          Animated.timing(opacity, {
            toValue: 0,
            duration: 6000 + Math.random() * 4000,
            useNativeDriver: true,
          }),
        ]),
        Animated.spring(scale, {
          toValue: 0.5 + Math.random() * 1.5,
          friction: 8,
          useNativeDriver: true,
        }),
      ]).start(() => startAnimation());
    };

    const timer = setTimeout(startAnimation, delay);
    return () => clearTimeout(timer);
  }, []);

  const size = 2 + Math.random() * 4;
  const color = Math.random() > 0.5 ? '#6C63FF' : '#00D4FF';

  return (
    <Animated.View
      style={[
        styles.particle,
        {
          width: size,
          height: size,
          borderRadius: size / 2,
          backgroundColor: color,
          opacity,
          transform: [{ translateX }, { translateY }, { scale }],
        },
      ]}
    />
  );
}

export default function ParticleBackground() {
  const particles = Array.from({ length: PARTICLE_COUNT }, (_, i) => (
    <Particle key={i} delay={i * 300} />
  ));

  return (
    <View style={styles.container}>
      {particles}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    overflow: 'hidden',
    pointerEvents: 'none',
  },
  particle: {
    position: 'absolute',
  },
});
